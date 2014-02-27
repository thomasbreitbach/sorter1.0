(ns Sorter.exif
  ^"Wrapper for https://code.google.com/p/metadata-extractor/"
  (:use [clojure.string :only [join]])
  (:import [java.io BufferedInputStream FileInputStream]
           [com.drew.imaging ImageMetadataReader]))

; EXIF SCHEMA:
; [{DIRECTORY-NAME}] {TAG-NAME} - {TAG-DESCRIPTION} 
;
; EXIF EXAMPLES:
; [Exif IFD0] Model - Canon EOS 600D
; [Canon Makernote] Flash Mode - No flash fired
; [Exif Thumbnail] Thumbnail Image Width - 668 pixels

; Welche Directories sollen berücksichtigt werden?
(def exif-directory-regex
  (re-pattern (str "(?i)(" (join "|"
                                ["Exif" "JPEG" "JFIF"
                                 "Agfa" "Canon" "Casio" "Epson"
                                 "Fujifilm" "Kodak" "Kyocera"
                                 "Leica" "Minolta" "Nikon" "Olympus"
                                 "Panasonic" "Pentax" "Sanyo"
                                 "Sigma/Foveon" "Sony"]) ")")))


(defn- extract-from-tag
  [tag]
  (into {} (map #(hash-map (.getTagName %) (.getDescription %)) tag)))

; #(...) Kurzform zum Erzeugen einfacher anonymer Funktionen
;
;filter wendet alle Elemente der Collectiona auf die Funktion an und gibt die mit
;rückgabe "true" zurück
(defn exif-for-file
  "Takes an image file (as a java.io.InputStream or java.io.File) and extracts exif information into a map"
  [file]
  (let [metadata (ImageMetadataReader/readMetadata file)
        exif-dirs (filter #(re-find exif-directory-regex (.getName %)) (.getDirectories metadata))
        tags (map #(.getTags %) exif-dirs)]
    (into {} (map extract-from-tag tags))))
