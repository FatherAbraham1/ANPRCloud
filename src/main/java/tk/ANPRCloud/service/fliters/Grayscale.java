package tk.ANPRCloud.service.fliters;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Grayscale implements NumberPlateFilter {
	private int mode;
	private Mat result;
	
	public Grayscale(int mode){
		this.mode = mode;
	}
	
	public Mat proc(Mat src) {
		// Convert RGB to gray
		result = new Mat();
	    Imgproc.cvtColor(src, result, Imgproc.COLOR_RGB2GRAY );
	    Highgui.imwrite("/tmp/gray.png", result);
		return result;
	}
}
