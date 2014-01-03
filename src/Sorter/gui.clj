(ns Sorter.gui)

(import '(javax.swing JFrame JLabel JTextField JButton)
        '(java.awt.event ActionListener)
        '(java.awt GridLayout))

(defn celsius []
  (let [frame (JFrame. "Celsius Converter")
        ;; let is a Clojure special form, a fundamental building block of the language.
				;;
				;; In addition to parameters passed to functions, let provides a way to create
				;; lexical bindings of data structures to symbols. The binding, and therefore 
				;; the ability to resolve the binding, is available only within the lexical 
				;; context of the let. 
				;; 
				;; let uses pairs in a vector for each binding you'd like to make and the value 
				;; of the let is the value of the last expression to be evaluated. let also 
				;; allows for destructuring which is a way to bind symbols to only part of a 
				;; collection.

        temp-text (JTextField.)
        celsius-label (JLabel. "Celsius")
        convert-button (JButton. "Convert")
        fahrenheit-label (JLabel. "Fahrenheit")]
    (.addActionListener
     convert-button
     (reify ActionListener 
            (actionPerformed
             [_ evt]
             (let [c (Double/parseDouble (.getText temp-text))]
               (.setText fahrenheit-label
                         (str (+ 32 (* 1.8 c)) " Fahrenheit"))))))
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add temp-text)
      (.add celsius-label)
      (.add convert-button)
      (.add fahrenheit-label)
      (.setSize 600 160)
      (.setLocation 600 300)
      (.setVisible true))))