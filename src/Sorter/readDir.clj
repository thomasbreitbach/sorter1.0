(ns Sorter.readDir)
;;For NOT nil eq !=
(def not-nil? (complement nil?))

(def rawFormats [".CR2" ".NEF" ".RAW" ".DNG"])
(def imgFormats (conj rawFormats ".jpg" ".JPG"))


(defn files-of-as-file [dir]
  "Lists all files in the given path incl. this folder (./)"
    (file-seq (clojure.java.io/file dir)))

(defn files-of-as-string [dir]
  "List all filenames in the given path as string array exl. this folder (./)"
  (.list (clojure.java.io/file dir)))

(defn count-files-of [dir]
  "counts files of the given directory."
  (count (files-of-as-string dir)))

(defn check-extension
  "check-extention takes a prefix and returns a predicate 
  that checks a string if it ends with this"
  [p]
  (fn [s] (.endsWith s p)))
  
(defn list-images [dir formats]
  "gitb einen vector mit bildern zurück, die den gegebenen Dateiendungen entsprechen"
  (map (fn [p]
       (filterv (fn [s]
                  (.endsWith s p))
                (files-of-as-string dir)))
       formats))

;(get rawFormats 0)
;(get rawFormats 1)
;(;get rawFormats 2)
;(get rawFormats 3)

;(first rawFormats)
;(second rawFormats)
;(next rawFormats)

;(last rawFormats)

(defn doSomeFancyShit [rFormats]
  (
    (def value (first rFormats))
    (if (= not-nil? value)
      (
        (println value) 
        (doSomeFancyShit (next rFormats))
      )    
    )
    ))

(doSomeFancyShit rawFormats)