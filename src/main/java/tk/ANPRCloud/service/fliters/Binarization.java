package tk.ANPRCloud.service.fliters;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Binarization implements NumberPlateFilter {
	private int threshod = 127;
	private Mat result;
	public Binarization(int thresthod){
		this.threshod = thresthod;
	}

	@Override
	public Mat proc(Mat src) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
	    Imgproc.threshold(src, result, threshod, 255, Imgproc.THRESH_BINARY);
	    Highgui.imwrite("/tmp/bin.png", result);
		return result;
	}

}
