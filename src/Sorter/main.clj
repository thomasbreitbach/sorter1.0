(ns Sorter.main 
  (:gen-class)
  (:require [clojure.contrib.command-line :as ccl])
  (:require [clojure.java.io :as io]))

(use '[clojure.string :only (join split)])

(use 'Sorter.gui)
(use 'Sorter.messanges)
(use 'Sorter.readDir)
(use 'Sorter.exif)

(defn- get-root-path
  "Get the root path of the jar file"
  [& [ns]]
  (-> (or ns (class *ns*))
      .getProtectionDomain .getCodeSource .getLocation .getPath))

(defn- split-the-date 
  "Split the date/time from tag to a vector"
  [date]
    (split 
      date
      #"[:*\s*]+"))


(defn- create-new-date 
  "Create a new date by given YEAR:MONTH:DAY H:M:S"
  [theDate]
  (let [[year month day h m s] theDate]
    (str year "-" month "-" day "." h "-" m "-" s)))


(defn- copy-file
  "Copy files"
  [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))


(defn- rename-file-by-tag
  "Rename a bench of filenames by tag"
  [inPath outPath tag]
  (;BILDER LADEN!
  (doseq [x (list-images inPath)]
    (if (= tag "Date/Time") 
      (copy-file 
  (str inPath x) 
  (str outPath 
       (create-new-date 
         (split-the-date
           (exif-data (str inPath x) tag)
           )
         )
       "_" x)
  )
     
      (copy-file 
        (str inPath x) 
        (str outPath
             (exif-data (str inPath x) tag)
             "_" x)
        )
      )
)))

(defn -main [& args]
  (ccl/with-command-line args
    "Fotosort 1.0 - Commandline Tool"
    [
     [help  h       "Shows the help menu"]
     [listtags? b? "Lists all the tags to use for"]
     [in i          "This specifies the input directory to the pictures"]
     [out o         "This specifies the output directory for a new folder"]
     [tag t         "To sort and rename the pictures by given tag/s"]
     extras]
    
    (if 
      (not (clojure.string/blank? help)) 
      (println "Help is on the way..."))
    
    (if
      (not (clojure.string/blank? in))
      (def theInput in)
      (def theInput "./")
       )
    
    (if 
      (not (clojure.string/blank? out))
      (def theOutput out)
      (def theOutput "./")
      )
    
    (if
      (not (clojure.string/blank? tag))
      (def theTag tag)
      (def theTag "Date/Time")
      )
    
    (if (and
          (clojure.string/blank? tag)
          (clojure.string/blank? in)
          (clojure.string/blank? out))
      (
        (if listtags?
      (mTags)
      (mStartscreen)
      )
    
    (println "Default operation: \n IN is ./ \n OUT is ./ \n TAG is Date/Time\n\nStart the default operation? (J/N)")
   
         ;(rename-file-by-tag theInput theOutput theTag)

    (def runDef (read-line))
    (if (or (= runDef "j") (= runDef "J"))
      (
        (rename-file-by-tag theInput theOutput theTag)
        (println "Job done!")
        )
      ))
      (rename-file-by-tag theInput theOutput theTag)
      )   
)
)