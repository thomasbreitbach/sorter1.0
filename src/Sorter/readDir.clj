(ns Sorter.readDir
  (:use [clojure.string :only [join]])
  )
;;For NOT nil eq !=
(def not-nil? (complement nil?))

(def rawFormats ["CR2" "NEF" "RAW" "DNG"])
(def imgFormats (conj rawFormats "jpg"))

;case insesitiv regex pattern to retrieve only images
(def img-regex
  (re-pattern (str ".+\\.(?i:(" (join "|"
                                 imgFormats) "))")))

(defn list-files-as-file-seq [dir]
  "Lists all files in the given path incl. this folder (./)"
    (file-seq (clojure.java.io/file dir)))

;(defn files-of-as-string [dir]
;  "List all filenames in the given path as string array exl. this folder (./)"
;  (.list (clojure.java.io/file dir)))

(defn count-files-of [dir]
  "counts files of the given directory."
  (count (list-files-as-file-seq dir)))

  
;list images in given directory
(defn list-images 
  "gitb einen vector mit bildern zurück, die den gegebenen Dateiendungen entsprechen"
  [dir]
  (filter #(re-find img-regex  %) (list-file-names dir)))
  
;list only file names
(defn list-file-names
  [dir]
  (map #(.getName %) (list-files-as-file-seq dir))
  )


;(get rawFormats 0)
;(get rawFormats 1)
;(;get rawFormats 2)
;(get rawFormats 3)

;(first rawFormats)
;(second rawFormats)
;(next rawFormats)

;(last rawFormats)

;(defn doSomeFancyShit [rFormats]
;  (
;    (def value (first rFormats))
;    (if (= not-nil? value)
;      (
;        (println value) 
;        (doSomeFancyShit (next rFormats))
;      )    
;    )
;    ))
;
;(doSomeFancyShit rawFormats)