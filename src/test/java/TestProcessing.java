import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

import tk.ANPRCloud.service.NumberPlateProcessing;
public class TestProcessing {
    
	//NumberPlateProcessing process the file
	private static NumberPlateProcessing numberPlateProcessing;
	private static BufferedImage image = null;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Processing Unit Test");
		image = ImageIO.read(new File("src/test/testNumberPlateProcessing/001.jpg"));
		numberPlateProcessing =  new NumberPlateProcessing(image);
		System.out.println("Processing Unit Test Success");
	}
}
