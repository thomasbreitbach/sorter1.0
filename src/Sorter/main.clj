(ns Sorter.main 
  (:gen-class)
  (:require [clojure.contrib.command-line :as ccl])
  (:require [clojure.java.io :as io])
  (:require [clojure.contrib.java-utils]))

(import '(java.io File))

(use '[clojure.string :only (join split)])
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
      (if 
        (re-matches #"." path)
        (str "/")
        (if 
        (re-matches #"../" path)
        (str "/")
        )
        )
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
 (if (not (clojure.string/blank? nFolder))
   (if (.mkdir (new File (str theOut (check-line-seperator theOut) nFolder)))
     (println "New folder created: " nFolder) 
     (println "Folder already exists!")
     )
   )
 
  (def theString "")
  (def res (list-images (str theIn)))
  
  (doseq [x res]
    (doseq [t tag]
      (if (= t "Date/Time")
        (def theString 
          (str theString
               (let [date (exif-data (str theIn (check-line-seperator theIn) x) "Date/Time Original")] 
                 (if 
                   (not 
                     (nil? date)
                     )
                   (create-new-date
                     (split-the-date date)))
                 )))
        
        (if (= t "Model")
          (def theString
            (str theString 
                 (let [model (exif-data (str theIn (check-line-seperator theIn) x) t)]
                   (if 
                     (not (nil? model))
                     (create-new-model
                       (split-whitespace model)
                       )))))
          
          (if (= t "Make")
            (def theString
              (str theString
                   (let [make (exif-data (str theIn (check-line-seperator theIn) x) t)]
                     (if 
                       (not (nil? make))
                       (create-new-model
                         (split-whitespace make))           
                       ))))
            
            (if (= t "Width")
              (def theString
                (str theString
                     (let [width (exif-data (str theIn (check-line-seperator theIn) x) "Image Width")]
                       (if 
                         (not (nil? width))
                         (create-new-model
                           (split-whitespace width))           
                         ))))
              
              (if (= t "Height")
                (def theString
                  (str theString
                       (let [height (exif-data (str theIn (check-line-seperator theIn) x) "Image Height")]
                         (if 
                           (not (nil? height))
                           (create-new-model
                             (split-whitespace height))           
                           ))))
                
                (def theString 
                  (str theString
                       (exif-data (str theIn (check-line-seperator theIn) x) t)
                       ))
                )
              )
            )
          )
        )
      );EOF DOSEQ
    (if (not (clojure.string/blank? nFolder))
      (def newOut (str theOut nFolder (check-line-seperator theOut) theString "_"))
      (def newOut (str theOut theString "_"))
      )
    
    (copy-file 
      (str theIn (check-line-seperator theIn) x) 
      (str newOut x)
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
//                              Photosort 1.0                                //
//                                                                           //
//                          by Thomas Breitbach                              //
//                             Andre  Wissner                                //
//                                                                           //
//                 https://github.com/andrewissner/sorter1.0                 //
//                                                                           //
///////////////////////////////////////////////////////////////////////////////

Available useful Tags:

  Date/Time - Get the date and time from the metadata
  Model     - Get the model and the make of the camera
  Make      - Get the camera make
  Width     - Get the image width
  Height    - Get the image height

"
    [
     [help      "Shows exactly this menu"]
     [in        "This specifies the input directory for the pictures"]
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
      (def theTag ["Date/Time"])
      )
    
    (println "\n\nThe script is running with these configuration:\n\nTAG: " theTag "\nIN:  " theInput "\nOUT: " theOutput "\nNew Folder: " theNewFolder "\n\n")
    (println "Run this configuration? (J/N)")
    (let [input (clojure.string/lower-case(read-line))]
      (if (= input "j")
        (copy-image-with-format  theInput theOutput theTag theNewFolder)        
        (println "Nothing to do here!")
        )
      )
    )
  )