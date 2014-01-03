(ns Sorter.readDir)

(def rawFormats (vector ".CR2" ".NEF" ".RAW" ".DNG"))

(get rawFormats 0)
(get rawFormats 1)
(get rawFormats 2)
(get rawFormats 3)

(.lastIndexOf rawFormats)