(ns Sorter.main (:gen-class)
   (:require [clojure.contrib.command-line :as ccl]))
(use 'Sorter.gui)
(use 'Sorter.messanges)
(use 'Sorter.readDir)
(use 'Sorter.exif)

;(use 'fotosort.gui)
;(use 'fotosort.messanges)

(defn -main [& args]
  (ccl/with-command-line args
    "Fotosort 1.0 - Commandline Tool"
    [
     [help "Shows the help menu"]
     [in "This specifies the input directory to the pictures"]
     [out "This specifies the output directory for a new folder"]
     [tag "To sort and rename the pictures by given tag"]
     [tags "To sort and rename all pictures by more then one give tag"]
     [boolean? b? "BOOL"]
     extras]

    (println "Help input: " help)
    (println "input: " in)
    (println "output: " out)
    (println "boolean?: " boolean?)
    (println "extra args: " extras)
)
)

(def pathToPictureDir "Z:/")
(def pictures (list-images-with-path pathToPictureDir))
 (def pictureName (list-images pathToPictureDir))
(def countPics (count-files pathToPictureDir))

;Not usable
; (1 2 3) (a b c) => (1 a) (1b) (1c) (2a)...
(defn do-picture-stuff [theOldList]
  (doseq [x theOldList]
    (println (str x))
    (println (exif-data x))
    ;(rename-file picture "D:/Fotografien/HDR - Panoramen/HDR/Jahr 2014/18.01 - Gießen Lahn/HDR/Magaretenhütte/Feld3/asd.jpg")
    ))
(exif-data "Z:/asd.jpg")
(do-picture-stuff pictures)
;eoNu

(loop [x countPics]
  (when (> x 0)
    (println x)
    (recur (- x 1))))

(for [x (range 1 10)] 
    (* x x))