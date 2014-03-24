(ns Sorter.messanges)

(defn mStartscreen 
  []
  (print "
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
"))

(defn mTags
 []
(print "\nAll avaleble Tags:\n
Marke      //Get the type of camera branding
Model      //Get the model of a camera
Date/Time  //Get the picture date and time
Height     //Get the dimension height from the picture
Width      //Get the dimension width from the picture\n\n"
))