package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Binarization implements NumberPlateFilter {
	private int threshod = 127;
	private Mat result;
    ArrayList<Object> resultList = new ArrayList<Object>();
	public Binarization(String thresthod){
		if (thresthod != "default")  {
			this.threshod = Integer.parseInt(thresthod);
		}
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
	    Imgproc.threshold((Mat)src.get(0), result, threshod, 255, Imgproc.THRESH_BINARY);
	    Highgui.imwrite("/tmp/bin.png", result);
	    resultList.add(result);
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}

}
