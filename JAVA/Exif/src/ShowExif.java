import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//Make=Marke, Model=Bestimmtes Model der Marke, ImageHeight=Höhe des Images

public class ShowExif {
	public static String tagName ="";
	public static HashMap<String, String> hmCameraInformation = new HashMap<String, String>();
	
	public static final String IMAGE_HEIGHT = "ExifImageHeight";
	public static final String IMAGE_WIDTH 	= "ExifImageWidth";
	public static final String MAKE			= "Make";
	public static final String MODEL		= "Model";
	public static final String DATE			= "Date/TimeOriginal";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File jpegFile = new File("D:/Fotografien/Verschieden/Jahr 2013/31.12 - Silvester 13/Andre/raw/IMG_8501.CR2");
		
		System.out.println(jpegFile);
		
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
			for (Directory directory : metadata.getDirectories()) {
			    for (Tag tag : directory.getTags()) {
			    	tagName = tag.getTagName();
			    	tagName = tagName.trim();
			    	tagName = tagName.replace(" ", "");
			    	
			    	if(tagName.equals(IMAGE_HEIGHT))	{	hmCameraInformation.put(IMAGE_HEIGHT, tag.getDescription()); }
			    	if(tagName.equals(IMAGE_WIDTH))		{	hmCameraInformation.put(IMAGE_WIDTH, tag.getDescription()); }
			    	if(tagName.equals(MAKE))			{	hmCameraInformation.put(MAKE, tag.getDescription()); }
			    	if(tagName.equals(MODEL))			{	hmCameraInformation.put(MODEL, tag.getDescription()); }
			    	if(tagName.equals(DATE))			{	hmCameraInformation.put(DATE, tag.getDescription()); }
			    	
//			    	System.out.println(tag);
			    }
			}
		} 
		catch (ImageProcessingException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		for( String name: hmCameraInformation.keySet() )
	    {
	       System.out.println(name + ": "+ hmCameraInformation.get(name));    
	    }
	}
	
	

}
