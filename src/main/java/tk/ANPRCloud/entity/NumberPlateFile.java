package tk.ANPRCloud.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NumberPlateFile {
	private File file;
	private String contentType;
	private String filename;
	private Image image;
	
	// Getter and setter
	public File getFile() {
		System.out.println(file);
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	// This method will validate whether this file is a image using java library
	public boolean isImageFile() {
		try {
			image = ImageIO.read(this.file);
		    if (image == null) {
		        System.out.println("WARING: The file " + this.filename + " could not be opened , it is not an image");
		        return false;
		    }
		} catch(IOException ex) {
		    System.out.println("WARING: The file " + this.filename +" could not be opened , an error occurred.");
		    return false;
		}
		return true;
	}
}
