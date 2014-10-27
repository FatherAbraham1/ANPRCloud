package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Scalar;
import org.opencv.core.CvType;

import tk.ANPRCloud.service.NumberPlateFilter;

public class MaskColor implements NumberPlateFilter {
	private double reserveColors[][] = 
		{	
			{140, 50, 50, 175, 255, 255},
			{0, 100, 100, 15, 255, 255},
			{80, 0, 0, 90, 255, 50},
			{160, 5, 25, 175, 20, 50},
			{160, 5, 100, 175, 20, 150},
			{160, 55, 12, 175, 60, 200},
			{0, 0, 250, 10, 10, 255}
		};
	private Mat result;
    ArrayList<Object> resultList = new ArrayList<Object>();
	public MaskColor(String arg){
	}

	// Input a binary image to apply the mask and a RGB image to calculate the mask
	@Override
	public ArrayList<Object> proc(ArrayList<Object> srcList) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
		Mat src = (Mat)srcList.get(1);
		Mat mask = new Mat(src.rows(), src.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2HSV);
		for (int i = 0; i < reserveColors.length; i++){
			Mat tmp = new Mat();
			Core.inRange(src, new Scalar(reserveColors[i][0], reserveColors[i][1], reserveColors[i][2]), 
						new Scalar(reserveColors[i][3], reserveColors[i][4], reserveColors[i][5]), tmp);
			Core.bitwise_or(mask, tmp, mask);
		}
		Core.bitwise_and((Mat)srcList.get(0), mask, result);
		//Highgui.imwrite("/tmp/MaskColor.png", result);
	    resultList.add(result);
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
