(ns Sorter.main (:gen-class)
   (:require [clojure.contrib.command-line :as ccl])
   (:require [clojure.java.io :as io]))
(use '[clojure.string :only (join split)])

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
     [help         "Shows the help menu"]
     [listtags? b? "Lists all the tags to use for"]
     [in           "This specifies the input directory to the pictures"]
     [out          "This specifies the output directory for a new folder"]
     [tag          "To sort and rename the pictures by given tag"]
     [tags         "To sort and rename all pictures by more then one give tag"]
     [boolean? b? "BOOL"]
     extras]

    (if 
      (not (clojure.string/blank? help)) 
      (println "Help is on the way..."))
    
    (if
      (not (clojure.string/blank? in))
      (println "This is the input:" in))
    
    (if
      (not (clojure.string/blank? out))
      (println "This is the output:" out))
    
    (if
      (not (clojure.string/blank? tag))
      (println "sort and rename by tag:" tag))
    
    (if
      (not (clojure.string/blank? tags))
      (println "sort and rename by tags:" tags))
    
    (if listtags?
      (mTags))

    ;(println boolean?)
    ;(println extras)
	(mStartscreen)

 
  (def pathToPictureDir in)
  (def pictures (list-images-with-path pathToPictureDir))
  (def pictureName (list-images pathToPictureDir))
  (def countPics (count-files pathToPictureDir))
  
  ;Fehlerhaft
  (defn do-test [theData theTag]
  (doseq [x theData]
    (
      (println (exif-data x theTag))
      )))
    
  
  (println pictureName) 
  (do-test pictures tag)
  ;(println (exif-data "Z:/asd.jpg" "Date/Time"))
  
)
)

;1.) einzelnes Bild ändern by Date

;Halte den alten Namen
(def picName "a.jpg")

;Halte den pfad zur Datei
(def path "C:/Users/vU/Pictures/") 

(def oldDate (exif-data (str path picName) "Date/Time"))

;Hole das Datum und zerlege es in ein Vector
(defn split-the-date 
  [date]
    (split 
      date
      #"[:*\s*]+"))

;Formatiere das Datum YEAR-MONTH-DAY.HOUR-MINUTE-SECOND
(defn create-new-date 
  "Create a new date by given YEAR:MONTH:DAY H:M:S"
  [theDate]
  (let [[year month day h m s] theDate]
    (str year "-" month "-" day "." h "-" m "-" s)))

;Erstelle einen neuen Namen mit Tag
(defn create-new-filename 
  [old-filename path-to-file tag]
    (str path-to-file tag "." old-filename))

;Show the new filename
(def newFile (create-new-filename picName path (create-new-date (split-the-date oldDate))))
(def oldFile (str path picName))

(rename-file oldFile newFile)

;##############################################################################################


(defn split-the-date 
  "Split the date/time from tag to a vector"
  [date]
    (split 
      date
      #"[:*\s*]+"))


(defn create-new-date 
  "Create a new date by given YEAR:MONTH:DAY H:M:S"
  [theDate]
  (let [[year month day h m s] theDate]
    (str year "-" month "-" day "." h "-" m "-" s)))


(defn copy-file
  "Copy files"
  [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))


(defn rename-file-by-tag
  "Rename a bench of filenames by tag"
  [filenames tag]
  (doseq [x filenames]
    (if (= tag "Date/Time") 
      (copy-file (str path x) (str path (create-new-date 
                                       (split-the-date 
                                         (exif-data (str path x) tag)
                                         )
                                       )
                "_" x)
             )
      (copy-file (str path x) (str path 
               (exif-data (str path x) tag)
                  "_" x)
               )
     )
))

(def pictureName (list-images path))
(rename-file-by-tag pictureName "Date/Time")

