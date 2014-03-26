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


(defn- copy-image-with-format
  "Function to read the exif data and write new pictures with new name"
  [theIn theOut tag nFolder]  
  
  ;(def tagList (split-whitespace tag)); only you come from repl
  ;(println tag)
  (if )
  (def theString "")
  (def res (list-images (str theIn)))
  (doseq [x res]
    (doseq [t tag]
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
     [help      "Shows exactly this menu"]
     [listtags? "Lists all useful tags" false]
     [in        "This specifies the input directory to the pictures"]
     [out       "This specifies the output directory for a new folder"]
     [tag       "To sort and rename the pictures by given tag/s"]
     [newFolder "Create a subfolder in the output directory"]
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
       listtags? ;BUG!
      (mTags)
      (
        (println "\n\nThe script is running with these configuration:\n\nTAG: " theTag "\nIN:  " theInput "\nOUT: " theOutput "\n\n")
        (println "Run this configuration? (J/N)")
        (def readCmd (clojure.string/lower-case (read-line)))
        (if (= readCmd "j")
          (
            (if (= theInput theOutput)
              (copy-image-with-format  theInput theOutput theTag theNewFolder)
              (copy-image-with-format  theInput theOutput theTag theNewFolder)
              )
            (println "Job done!")
            )
          (println "Nothing to do here!")
          )
        )
      )
    
    )
  )


(defn ^File file-str
  "Concatenates args as strings and returns a java.io.File.  Replaces
  all / and \\ with File/separatorChar.  Replaces ~ at the start of
  the path with the user.home system property."
  [& args]
  (let [^String s (apply str args)
        s (.replace s \\ File/separatorChar)
        s (.replace s \/ File/separatorChar)
        s (if (.startsWith s "~")
            (str (System/getProperty "user.home")
                 File/separator (subs s 1))
            s)]
    (File. s)))

(println file-str "./")

(defn check-line-seperator[path]
  (if 
    (or (re-matches #".(\/).*" path) (re-matches #"(\/).*" path))
    (str "UNIX FOUND::/")
    (if 
      (or (re-matches #"..(\\).*" path) (re-matches #"^(\\).*" path))
      (str "WIN FOUND::\\")
      )   
    )   
  )

(println (check-line-seperator "C:\\"))
(println (check-line-seperator "/USers/"))
(println (check-line-seperator "./"))
;(copy-image-with-format "C:\\Users\\vU\\Desktop\\TestImages" "C:\\Users\\vU\\Desktop\\TestImages" "Date/Time Model" "")

