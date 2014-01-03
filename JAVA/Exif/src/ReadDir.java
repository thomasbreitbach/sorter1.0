import java.io.File;

/*
 * 
 * 	Adobe Systems Digital Negative: .dng
	Canon: .tif, .crw, .cr2
	Contax: .raw
	Epson: .erf
	Fujifilm: .raf
	Hasselblad: .3fr, .fff
	Kodak: .dcr, .dcs, .kdc (für EasyShare P850, Z990), .raw
	Leica Camera: .raw, .dng, .rwl
	Mamiya: .mef, .mfw, .iiq
	Minolta Raw: .mrw, .mdc
	Nikon: .nef, .nrw
	Olympus: .orf
	Panasonic: .raw, .rw2
	Pentax: .pef, .dng
	Phase One: .tif
	Ricoh: .dng
	Samsung: .srw, .dng
	Sigma: .x3f
	Sinar CaptureShop für Macintosh: .cs1, .cs4, .cs16
	Sony: .srf, .sr2, .arw (für Sony-DSLR/DSLT/NEX-α-Kameras)
 */


public class ReadDir {
//	public static final String[] rawFormat = 
//		{
//			".dng", ".crw", ".cr2", ".raw", 
//			".erf", ".raf", ".3fr",	".fff",
//			".dcr", ".dcs", ".kdc", ".rwl",
//			".mef", ".mfw", ".mdc", ".iig",
//			".nef", ".nrw", ".orf", ".rw2",
//			".pef", ".srw", ".cs1", ".cs4",
//			".cs16",".srf", ".arw"
//		};
	public static final String[] rawFormat = 
		{
			".cr2", ".nef", ".dng"
		};
	/**
	 * @param args
	 */
	public static int countImages = 0;
	public static void main(String[] args) {
		//final File folder = new File("C:/Users/Wissner/Desktop/TEST");
		final File folder = new File("D:/");
		listFilesInFolders(folder);
		System.out.print(countImages);
	}
	public static void listFilesInFolders( File folder) {
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesInFolders(fileEntry);
	        } else {
	            
	        	for(int i = 0; i < rawFormat.length; i++)
	        	{
	        		 if(fileEntry.getName().endsWith(rawFormat[i].toUpperCase()))
    				 {
	        			countImages++;
	 	            	//System.out.println(folder+"\\"+fileEntry.getName());
	 	            	
//	 	            	Path path = Paths.get(folder+"\\"+fileEntry.getName());
//	 	            	try {
//	 						byte[] data = Files.readAllBytes(path);
//	 						rawToJpeg(data,1024, 768, fileEntry);
//	 					} catch (IOException e) {
//	 						// TODO Auto-generated catch block
//	 						e.printStackTrace();
//	 					}
    				 }
	        	}
	        }
	    }
	    
	}

	public static void rawToJpeg(byte[] rawBytes, int width, int height, File outputFile)
	{
		
	}
}
