(ns Sorter.main 
  (:gen-class)
  (:require [clojure.contrib.command-line :as ccl])
  (:require [clojure.java.io :as io]))

(import '(java.io File))

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

(defn- split-whitespace
  "Split wihtespace from given tags"
  [tags]
  (split 
      tags
      #"[\s*\s*]+"))


(defn- create-new-date 
  "Create a new date by given YEAR:MONTH:DAY H:M:S"
  [theDate]
  (let [[year month day h m s] theDate]
    (str year "-" month "-" day "." h "-" m "-" s)))

(defn- create-new-model 
  "Create a new model by given model"
  [theModel]
  (let [[first seconde last] theModel]
    (str first seconde last)))


(defn- copy-file
  "Copy files"
  [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))



(defn -main [& args]
  (ccl/with-command-line args
    "
///////////////////////////////////////////////////////////////////////////////
//                                                                           //
//                              Fotosort 1.0                                 //
//                                                                           //
//                          by Thomas Breitbach                              //
//                             Andre  Wissner                                //
//                                                                           //
//                 https://github.com/andrewissner/sorter1.0                 //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////
"
    [
     [help       "Shows exactly this menu"]
     [listtags?  "Lists all useful tags"]
     [in         "This specifies the input directory to the pictures"]
     [out        "This specifies the output directory for a new folder"]
     [tag        "To sort and rename the pictures by given tag/s"]
     [newFolder  "Create a subfolder in the output directory"]
     extras]
    
    (if
      (not (clojure.string/blank? newFolder))
      (def theNewFolder newFolder)
      (def theNewFolder "")
      )
    
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
      (def theTag (split-whitespace tag))
      (def theTag "Date/Time")
      )
    
    
    (if 
      (java.lang.Boolean/valueOf listtags?) 
      (mTags)
      (
        (println "\n\nThe script is running with these configuration:\n\nTAG: " theTag "\nIN:  " theInput "\nOUT: " theOutput "\n\n")
        (println "Run this configuration? (J/N)")
        (def readCmd (clojure.string/lower-case (read-line)))
        (if (= readCmd "j")
          (
            (if (= theInput theOutput)
              (copy-image-with-format theInput theOutput theTag theNewFolder)
              (copy-image-with-format theInput theOutput theTag theNewFolder)
              )
            
            (println "Job done!")
            )
          (println "Nothing to do here!")
          )
        )
      )
    
    )
  )

(defn copy-image-with-format
  "Function to read the exif data and write new pictures with new name"
  [theIn theOut tag nFolder]  
  
  (def tagList (split-whitespace tag))
  (def theString "")
  (def res (list-images (str theIn)))
  (doseq [x res]
    (doseq [t tagList]
      (if (= t "Date/Time")
        (def theString 
          (str theString
               (create-new-date
                 (split-the-date (exif-data (str theIn "\\" x) t))
                 )))
        
        (if (= t "Model")
          (def theString
            (str theString 
                 (create-new-model
                   (split-whitespace 
                     (exif-data (str theIn "\\" x) t)
                     ))))
          
          (if (= t "Make")
            (def theString
              (str theString
                   (create-new-model
                     (split-whitespace 
                       (exif-data (str theIn "\\" x) t)
                       ))))
            (def theString 
               (str theString
                   (exif-data (str theIn "\\" x) t)
                   ))
            )
          )
        )
      );EOF DOSEQ
  (if (not (clojure.string/blank? nFolder))
      (if (.mkdir (new File (str theOut "\\" nFolder)))
        (println "New folder created: " nFolder) 
        (def newOut (str theOut "\\" nFolder "\\" theString "_"))
        )
      (def newOut (str theOut "\\" theString "_"))
      )
    (copy-file 
      (str theIn "\\" x) 
      (str newOut x)
          )
    (def theString "")
    )
  )

(copy-image-with-format "C:\\Users\\vU\\Desktop\\TestImages" "C:\\Users\\vU\\Desktop\\TestImages" "Date/Time Model" "")

