(ns Sorter.fsOperations
  ^{:doc "what is this ns about? todo: insert description"
    :author "Thomas Breitbach & André Wißner"}
  (:use [clojure.string :only [join]])
  (:import java.io.File)
  (:require [clojure.java.io :as io]))

;raw and image formats
(def rawFormats ["CR2" "NEF" "RAW" "DNG"])
(def imgFormats (conj rawFormats "jpg"))

;case insensitive regex pattern to retrieve only images
(def img-regex
  (re-pattern (str ".+\\.(?i:(" (join "|"
                                 imgFormats) "))")))

(defn list-files-as-file-seq [dir]
  "Lists all files in the given path incl. this folder (./)"
    (file-seq (io/file dir)))

;(defn files-of-as-string [dir]
;  "List all filenames in the given path as string array exl. this folder (./)"
;  (.list (io/file dir)))

;list only filenames
(defn list-filenames
  "gibt eine Liste aller Dateinamen im gegebenen Ordner zurück"
  [dir]
  (map #(.getName %) (list-files-as-file-seq dir)))

(defn list-filenames-with-path
  "gibt eine Liste aller Dateinamen (+ absoluter Pfad zur Datei) im gegebenen Ordner zurück"
  [dir]
  (map #(.getAbsolutePath %) (list-files-as-file-seq dir))
  )

(defn count-files [dir]
  "counts files of the given directory."
  (count (list-files-as-file-seq dir)))
  
;list images (incl.  in given directory
(defn list-images-with-path 
  "gibt eine Liste aller Bilddateien (+ absoluter Pfad zur Datei) im gegebenen Ordner zurück"
  [dir]
  (filter #(re-find img-regex  %) (list-filenames-with-path dir)))

(defn list-images
  "gibt eine Liste mit Dateinamen von Bilddateien im gegegeben Ordner zurück"
  [dir]
  (filter #(re-find img-regex  %) (list-filenames dir)))
  

;#####################################################################################################################
;#####################################################################################################################
;The code below comes form 
;https://bitbucket.org/tebeka/fs/src/9a5476217d7101b1e20bf62866e8c4f368175ba1/src/fs.clj?at=default#cl-49
;#####################################################################################################################
;#####################################################################################################################


(def ^:dynamic *cwd* (.getCanonicalPath (io/as-file ".")))

(defn- as-file [path]
  "The challenge is to work nicely with *cwd*"
  (cond
   (instance? File path) path
   (= path "") (io/as-file "")
   (= path ".") (io/as-file *cwd*)
   :else
   (let [ try (new File path) ]
         (if (.isAbsolute try)
           try
           (new File *cwd* path)))))

(defn- rename-file
  "Rename old-path to new-path."
  [old-path new-path]
  (.renameTo (as-file old-path) (as-file new-path)))

(defn- delete-file
  "Delete path. Returns path."
  [path]
  (.delete (as-file path))
  path)

