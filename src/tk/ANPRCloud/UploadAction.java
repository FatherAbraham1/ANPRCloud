package tk.ANPRCloud;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport {
   private File file;
   private String contentType;
   private BufferedImage image;
   private String filename;
   private String carNumber;

   public void setUpload(File file) {
      this.file = file;
   }

   public void setUploadContentType(String contentType) {
      this.contentType = contentType;
   }

   public void setUploadFileName(String filename) {
      this.filename = filename;
   }

   public String execute() {
	   Random rand = new Random();
	   int  n = (rand.nextInt(9) + 1) * 10000 + rand.nextInt(9999);
	   this.setCarNumber("京A " + Integer.toString(n));
	   System.out.println(file);
	   try {
		   image=ImageIO.read(file);
		    if (image == null) {
		        System.out.println("The file"+filename+"could not be opened , it is not an image");
		        return ERROR;
		    }
		} catch(IOException ex) {
		    System.out.println("The file"+filename+"could not be opened , an error occurred.");
		    return ERROR;
		}
	      System.out.println(contentType);
	      Process process = new Process();
	   return SUCCESS;
   }

public String getCarNumber() {
	return carNumber;
}

public void setCarNumber(String carNumber) {
	this.carNumber = carNumber;
}
}