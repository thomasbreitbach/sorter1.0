Photsort
=========

Dies ist das Repo zur Masterveranstaltung "Programmieren in Clojure" an der Technischen Hochschule Mittelhessen.
Als Projektarbeit soll ein in Clojure geschriebener Fotosortierer geschrieben werden, der es ermöglicht alle Fotos eines Ordners anhand der EXIF-Metadaten chronologisch zu sortieren.
Dies ist vorallem sehr hilfreich, wenn man mit verschiedenen Kameras Urlaubfotos macht und diese nach den schönen Tagen in zeitlich korrekter Reihenfolge ansehen möchte.


Java-Lib:
http://code.google.com/p/metadata-extractor/


Exif-Wrapper
============

## Installation
Leider ist die aktuelle Version des Repos noch nicht als Maven-Repo verfügbar, was allerdings für Leiningen vorausgesetzt wird. Aus diesem Grund muss der 2.7er Branch des meta-extractor Repositoies geklont und als lokales Maven-Repository installiert werden:
```bash
$ git clone https://code.google.com/p/metadata-extractor/
$ cd metadata-extractor
$ mvn install
```

Es kann sein, dass während des Buildprozesses einige Tests fehlschlagen. In diesem Fall kann folgender Maven-Aufruf durchgeführt werden, der die Tests überspringt:
```bash
$ mvn -Dmaven.test.skip=true install
```

## Verwendung
Es folgen einige Beispielaufrufe. Der Wrapper wurde mithilfe von 'Protocols' umgesetzt.

### Allgemein
```bash
(exif-data param1 param2)
```
Wobei param1 ein Bild sein muss. Param2 ist optional und dient als Filter der Exif-Daten.

#### String
```bash
(exif-data "pfad/zum/bild/bild.jpg")

(exif-data "pfad/zum/bild/bild.jpg" "Key")

(exif-data "pfad/zum/bild/bild.jpg" [:Key1 :Key2])

(exif-data "pfad/zum/bild/bild.jpg" (DirectoryClass.))
```
#### File
```bash
(exif-data (File. "pfad/zum/bild/bild.jpg"))

(exif-data (File. "pfad/zum/bild/bild.jpg") "Key")

(exif-data (File. "pfad/zum/bild/bild.jpg") [:Key1 :Key2])

(exif-data (File. "pfad/zum/bild/bild.jpg") (DirectoryClass.))
```
#### URL
```bash
(exif-data (URL. "pfad/zum/bild/bild.jpg"))

(exif-data (URL. "pfad/zum/bild/bild.jpg") "Key")

(exif-data (URL. "pfad/zum/bild/bild.jpg") [:Key1 :Key2])

(exif-data (URL. "pfad/zum/bild/bild.jpg") (DirectoryClass.))
```
### Beispiele
#### String
```bash
=> (exif-data "/home/thomas/Downloads/IMG_20140302_160056.jpg" "Model")
"Nexus 4"


=> (exif-data "https://github.com/andrewissner/sorter1.0/blob/master/IMG_20140312_175020.jpg")
{:Thumbnail Compression "JPEG (old-style)", :GPS Altitude "0 metres", :Focal Length "4,6 mm", :Component 1 "Y component: Quantization table 0, Sampling factors 2 horiz/2 vert", :X Resolution "72 dots per inch", :Number of Components "3", :Component 2 "Cb component: Quantization table 1, Sampling factors 1 horiz/1 vert", :Exposure Time "1/154 sec", :Exif Image Width "3264 pixels", :FlashPix Version "1.00", :GPS Longitude "8° 40' 30,01\"", :GPS Processing Method "ASCII", :Resolution Unit "Inch", :GPS Img Direction Ref "Magnetic direction", :GPS Img Direction "197 degrees", :Make "LGE", :Model "Nexus 4", :GPS Latitude "50° 35' 15,36\"", :GPS Latitude Ref "N", :ISO Speed Ratings "100", :Data Precision "8 bits", :Exif Image Height "2448 pixels", :Component 3 "Cr component: Quantization table 1, Sampling factors 1 horiz/1 vert", :Thumbnail Length "36046 bytes", :Image Width "3264 pixels", :GPS Altitude Ref "Sea level", :Components Configuration "YCbCr", :YCbCr Positioning "Center of pixel array", :Date/Time Digitized "2014:03:02 16:00:56", :Date/Time Original "2014:03:02 16:00:56", :Thumbnail Offset "737 bytes", :F-Number "F2,6", :Image Height "2448 pixels", :GPS Longitude Ref "E", :Compression Type "Baseline", :Color Space "sRGB", :Exif Version "2.20", :GPS Date Stamp "2014:03:02", :GPS Time-Stamp "15:0:54 UTC", :Y Resolution "72 dots per inch"}
```

#### File (java.io.File)
```bash
=> (exif-data (File. "/home/thomas/Downloads/IMG_20140302_160056.jpg") [:Model :Make])
{:Make "LGE", :Model "Nexus 4"}

```

#### URL (java.net.URL)
```bash
=> (exif-data (URL. "https://github.com/andrewissner/sorter1.0/blob/master/IMG_20140312_175020.jpg") (GpsDirectory.))
{:GPS Altitude "0 metres", :GPS Longitude "8° 40' 40,73\"", :GPS Processing Method "ASCII", :GPS Img Direction Ref "Magnetic direction", :GPS Img Direction "157 degrees", :GPS Latitude "50° 35' 29,4\"", :GPS Latitude Ref "N", :GPS Altitude Ref "Sea level", :GPS Longitude Ref "E", :GPS Date Stamp "2014:03:12", :GPS Time-Stamp "16:50:15 UTC"}
```
