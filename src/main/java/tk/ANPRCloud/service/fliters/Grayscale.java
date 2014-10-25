package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Grayscale implements NumberPlateFilter {
	private int mode;
	private Mat result;
	ArrayList<Object> resultList = new ArrayList<Object>();
	
	public Grayscale(String mode){
		if (mode != "default")  {
			this.mode = Integer.parseInt(mode);
		}
	}
	
	public ArrayList<Object> proc(ArrayList<Object> src) {
		// Convert RGB to gray
		result = new Mat();
	    Imgproc.cvtColor((Mat)src.get(0), result, Imgproc.COLOR_RGB2GRAY );
	    Highgui.imwrite("/tmp/gray.png", result);
	    resultList.add(result);
		return resultList;
	}
	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
