import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class ReadDir {

	/**
	 * @param args
	 */
	public static int countImages = 0;
	public static void main(String[] args) {
		final File folder = new File("C:/Users/Wissner/Desktop/TEST");
		listFilesInFolders(folder);
		System.out.print(countImages);
	}
	public static void listFilesInFolders( File folder) {
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesInFolders(fileEntry);
	        } else {
	            
	            if(fileEntry.getName().endsWith(".CR2") || fileEntry.getName().endsWith(".NEF"))
	            {
	            	countImages++;
	            	System.out.println(folder+"\\"+fileEntry.getName());
	            	
	            	Path path = Paths.get(folder+"\\"+fileEntry.getName());
	            	try {
						byte[] data = Files.readAllBytes(path);
						rawToJpeg(data,1024, 768, fileEntry);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        }
	    }
	    
	}

	public static void rawToJpeg(byte[] rawBytes, int width, int height, File outputFile)
	{
		
	}
}
