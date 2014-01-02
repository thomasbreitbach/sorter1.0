(ns Sorter.main)
(ns Sorter.gui)
(ns Sorter.messanges)

(load "gui")



(def factorial
  (fn [n]
    (loop [cnt n acc 1]
       (if (zero? cnt);Wenn cnt == zero
          acc
          (recur (dec cnt) (* acc cnt))))))


(defn main [n]
  (if (= n "go")
    (println "i do wat i want! Exit!")

    ( (mStartscreen)
		  (let [userInput (clojure.string/lower-case(read-line))]
	    (case userInput
		       "help" ((mHelp)(main ""))
		       "go" (main userInput)
		       "exit" (print userInput)
		       "gui" (celsius)
		       "fac" (factorial (bigdec (read-line)))
		       "Exit with Error : No match found!" ))
  )))


(main "")