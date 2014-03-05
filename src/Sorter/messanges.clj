(ns Sorter.messanges)

(defn mHelp []
  (println "Use these commands:
 
--tag      //To rename and sort all the picture by an given tag
--tags     //To rename and short alle the pictures by given tags
             use \"tag1 tag2 ..\"
--in       //To give the source folder
--out      //To give a different output folder
--help     //Show the help dialog
--listtags //To show all avalable tags

")
  )

(defn mStartscreen []
(println "
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
")
(mHelp)
)