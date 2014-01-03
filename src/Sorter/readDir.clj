(ns Sorter.readDir)
;;For NOT nil eq !=
(def not-nil? (complement nil?))

(def rawFormats [".CR2" ".NEF" ".RAW" ".DNG"])

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
    (if (= not-nil? value))
      (
        (println value) 
        (doSomeFancyShit (next rFormats))
      )    
    )
    )

(doSomeFancyShit rawFormats)