(ns Sorter.main 
  (:gen-class)
  (:require [clojure.contrib.command-line :as ccl])
  (:require [clojure.java.io :as io]))

(import '(java.io File))

(use '[clojure.string :only (join split)])
(use 'Sorter.gui)
(use 'Sorter.messanges)
(use 'Sorter.fsOperations)
(use 'Sorter.exif)


;#####################################################################################################################
;#####################################################################################################################
;The code below comes form 
;https://bitbucket.org/tebeka/fs/src/9a5476217d7101b1e20bf62866e8c4f368175ba1/src/fs.clj?at=default#cl-49
;#####################################################################################################################
;#####################################################################################################################


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
;#####################################################################################################################
;#####################################################################################################################
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

(defn delete-files 
  "Delete files"
  [in files]
  (doseq [x files]
    (println (delete-file (str (file-str in "\\" x)))) 
    )
  )


(defn- check-line-seperator[path]
  (if 
    (or (re-matches #".(\/).*" path) (re-matches #"(\/).*" path))
    (str "/")
    (if 
      (or (re-matches #"..(\\).*" path) (re-matches #"^(\\).*" path))
      (str "\\")
      )   
    )   
  )

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
  ;ERST ORDNER ANLEGEN DANN EXIF LESEN UND KOPIEREN
  
  (if (not (clojure.string/blank? nFolder))
      (if (.mkdir (new File (str theOut (check-line-seperator theIn) nFolder)))
        (println "\n\nFolder created!\n\n")
        (println "\n\nFolder " nFolder " found!\n\n")
        )
      )
  
  (def theString "")
  (def res (list-images (str theIn)))
  (doseq [x res]
    (doseq [t tag]
      (if (= t "Date/Time")
        (def theString 
          (str theString
               (create-new-date
                 (split-the-date (exif-data (str theIn (check-line-seperator theIn) x) t))
                 )))
        
        (if (= t "Model")
          (def theString
            (str theString 
                 (create-new-model
                   (split-whitespace 
                     (exif-data (str theIn (check-line-seperator theIn) x) t)
                     ))))
          
          (if (= t "Make")
            (def theString
              (str theString
                   (create-new-model
                     (split-whitespace 
                       (exif-data (str theIn (check-line-seperator theIn) x) t)
                       ))))
            (def theString 
               (str theString
                   (exif-data (str theIn (check-line-seperator theIn) x) t)
                   ))
            )
          )
        )
      );EOF DOSEQ
    (if (not (clojure.string/blank? nFolder))
      (copy-file 
        (str theIn (check-line-seperator theIn) x)
        (str (str theOut (check-line-seperator theIn) nFolder (check-line-seperator theIn) theString "_") x)
        )
      (copy-file 
        (str theIn (check-line-seperator theIn) x)
        (str (str theOut (check-line-seperator theIn) theString "_") x)
        )
      )

    (def theString "")
    )
  (println "Job done!")
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
    
   (println "\n\nThe script is running with these configuration:\n\nTAG: " theTag "\nIN:  " theInput "\nOUT: " theOutput "\n\n")
   (println "Run this configuration? (J/N)")
   (let [input (clojure.string/lower-case(read-line))]
      (if (= input "j")
        (copy-image-with-format  theInput theOutput theTag theNewFolder)        
        (println "Nothing to do here!")
       )
     )
    )
  )