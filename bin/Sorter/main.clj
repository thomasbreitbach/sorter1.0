(ns Sorter.main (:gen-class))
(use 'Sorter.gui)
(use 'Sorter.messanges)
(use 'Sorter.readDir)
(use 'Sorter.exif)

(def factorial
  (fn [n]
    (loop [cnt n acc 1]
       (if (zero? cnt);Wenn cnt == zero
          acc
          (recur (dec cnt) (* acc cnt))))))

(defn main [n]
  (if (= n "go")
    (println "i do what i want! Exit!")

    ( (mStartscreen)
		  (let [userInput (clojure.string/lower-case(read-line))]
	    (case userInput
		       "help" (
                  (mHelp)
                  (main "")
                  )
		       "go" (main userInput)
		       "exit" (print userInput)
		       "gui" (celsius)
		       "fac" (factorial (bigdec (read-line)))
		       "Exit with Error : No match found!" ))
  )))

<<<<<<< HEAD
(compile 'Sorter.main)
=======
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

(do-picture-stuff pictures)
;eoNu

(loop [x countPics]
  (when (> x 0)
    (println x)
    (recur (- x 1))))

(for [x (range 1 10)] 
    (* x x))
>>>>>>> a2ff2ea7c10e1e1ce88015be733a92b02a924e81
