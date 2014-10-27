package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Contours implements NumberPlateFilter {
	private int mode;
	private Mat result;
	ArrayList<Object> resultList = new ArrayList<Object>();
	
	public Contours(String mode){
		if (mode != "default")  {
			this.mode = Integer.parseInt(mode);
		}
	}
	
	public ArrayList<Object> proc(ArrayList<Object> src) {
		// Convert RGB to gray
		result = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours((Mat)src.get(0),
				contours, // a vector of contours
				hierarchy,
				Imgproc.RETR_EXTERNAL, // 提取外部轮廓
				Imgproc.CHAIN_APPROX_NONE); // all pixels of each contours
		
		// Draw blue contours on a white image
		result = (Mat) src.get(0);
		Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
		Imgproc.drawContours(result, contours,
			-1, // draw all contours
			new Scalar(0.0,0.0,255.0), // in blue
			1); // with a thickness of 1
		
	    //Highgui.imwrite("/tmp/Contours.png", result);
	    resultList.add(result); // 0 result Mat
	    resultList.add(contours); // 1 contours points
		return resultList;
	}
	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
