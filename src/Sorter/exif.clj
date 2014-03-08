(ns Sorter.exif
  ^{:doc "Wrapper for https://code.google.com/p/metadata-extractor/"
    :author "Thomas Breitbach & André Wißner"}
  (:use [clojure.string :only [join]])
  (:require [clj-http.client :as http-client])
  (:import [java.io File BufferedInputStream FileInputStream]
           [java.net URL]
           [com.drew.imaging ImageMetadataReader]
           [com.drew.metadata.exif GpsDirectory ExifThumbnailDirectory]
           [com.drew.metadata Directory]))

; EXIF SCHEMA:
; [{DIRECTORY-NAME}] {TAG-NAME} - {TAG-DESCRIPTION} 
;
; EXIF EXAMPLES:
; [Exif IFD0] Model - Canon EOS 600D
; [Canon Makernote] Flash Mode - No flash fired
; [Exif Thumbnail] Thumbnail Image Width - 668 pixels

; Welche Directories sollen berücksichtigt werden?
(def exif-directory-regex
  (re-pattern (str "(?i)(" (join "|"
                                ["GPS" "Exif" "JPEG" "JFIF"
                                 "Agfa" "Canon" "Casio" "Epson"
                                 "Fujifilm" "Kodak" "Kyocera"
                                 "Leica" "Minolta" "Nikon" "Olympus"
                                 "Panasonic" "Pentax" "Sanyo"
                                 "Sigma/Foveon" "Sony"]) ")")))


(defn- extract-from-tag
  "Extracts a Tag object into its key-value representation"
  [tag]
  (into {} (map #(hash-map (keyword (.getTagName %)) (.getDescription %)) tag)))


;-------------------------------------------------
;              File (File)
;-------------------------------------------------
(defn- exif-for-file
  "Takes an image file (as a java.io.InputStream or java.io.File) and extracts exif information into a map"
  ([file]
    (exif-for-file file nil))
  
  ([file dir]
    (try 
      (if (nil? dir)
        ;then
        (let [metadata (ImageMetadataReader/readMetadata file)
              exif-dirs (filter #(re-find exif-directory-regex (.getName %)) (.getDirectories metadata))
              tags (map #(.getTags %) exif-dirs)]
          (into {} (map extract-from-tag tags)))
        ;else
        (let [metadata (ImageMetadataReader/readMetadata file)
              geo-tags (.getTags (.getDirectory metadata (class dir)))]
          (into {} (extract-from-tag geo-tags))))
      (catch Exception e
          (println (str "caught exception: " (.getMessage e)))
          nil))))

(defn- exif-tag-for-file
  ""
  [file tag]
  (get (exif-for-file file) (keyword tag)))

(defn- exif-tags-for-file
  ""
  [file tag-seq]
  (select-keys (exif-for-file file) tag-seq))

;-------------------------------------------------
;                Loading files
;-------------------------------------------------
(defn load-file-from-fs
  "loads a file from filesystem"
  [filename]
  (try
    (FileInputStream. filename)
    (catch Exception e
      (println (str "caught exception for file '" filename "': " (.getMessage e)))
      nil)))

(defn load-file-from-url
  "loads a file from the web by the given url"
  [url]
  (try
    (BufferedInputStream. (:body (http-client/get (.toString url) {:as :stream})))
    (catch Exception e
      (println (str "caught exception for url '" url "': " (.getMessage e)))
      nil)))

;-------------------------------------------------
;              Filename (String)
;-------------------------------------------------
(defn- exif-for-filename
  "Loads a file from a give filename and extracts exif information into a map"
  ([filename]
    (exif-for-filename filename nil))
  ([filename dir]
    (exif-for-file (load-file-from-fs filename) dir)))

(defn- exif-tag-for-filename
  "Returns the value of desired exif tag"
  [filename tag]
  (exif-tag-for-file (load-file-from-fs filename) tag))

(defn- exif-tags-for-filename
  "Returns a map containing only those entries in map whose key is in keys"
  [filename tag-seq]
  (exif-tags-for-file (load-file-from-fs filename) tag-seq))

;-------------------------------------------------
;                URL
;-------------------------------------------------
(defn- exif-for-url
  ""
  ([url]
    (exif-for-url url nil))
  ([url dir]
    (exif-for-file (load-file-from-url url) dir)))

(defn- exif-tag-for-url
  ""
  [url tag]
  (exif-tag-for-file (load-file-from-url url) tag))

(defn- exif-tags-for-url
  ""
  [url tag-seq]
  (exif-tags-for-file (load-file-from-url url) tag-seq))

;-------------------------------------------------
;                Protocol
;-------------------------------------------------
(defprotocol exif
  (exif-data 
    [x]
    [x tag-or-dir] 
    "Returns exif data (or if stated only the desired exif tag(s)/exif directory) of a java.io.File, file path (as java.lang.String) or java.net.URL"))

(extend-protocol exif
  File
  (exif-data 
    ([f tag-or-dir]
      (if (instance? String tag-or-dir)
        (exif-tag-for-file f tag-or-dir)
        (if (instance? Directory tag-or-dir)
          (exif-for-file f tag-or-dir)
          (exif-tags-for-file f tag-or-dir))))
    ([f]
      (exif-for-file f)))
  
  String
  (exif-data 
    ([s tag-or-dir]
      (if (instance? String tag-or-dir)
        (exif-tag-for-filename s tag-or-dir)
        (if (instance? Directory tag-or-dir)
          (exif-for-filename s tag-or-dir)
          (exif-tags-for-filename s tag-or-dir))))
    ([s]  
      (exif-for-filename s)))
  
   URL
  (exif-data
    ([url]
      (exif-for-url url))
    ([url tag-or-dir]
      (if (instance? String tag-or-dir)
        (exif-tag-for-url url tag-or-dir)
        (if (instance? Directory tag-or-dir)
          (exif-for-url url tag-or-dir)
          (exif-tags-for-url url tag-or-dir))))))