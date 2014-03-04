(ns Sorter.seesawGui)
;FROM https://gist.github.com/daveray/1441520
(use 'seesaw.core)
(native!)
(def f (frame :title "Get to know Seesaw"))
(config! f :title "No RLY, get to know Seesaw!")
(config! f :content "This is some content")
(def lbl (label "I'm another label"))
(defn display [content]
  (config! f :content content)
  content)