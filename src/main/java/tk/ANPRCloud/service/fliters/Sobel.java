package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Sobel implements NumberPlateFilter {
	private int mode;
	private Mat result;
	ArrayList<Object> resultList = new ArrayList<Object>();
	private static Mat grad_x = new Mat(), grad_y = new Mat();
	private static Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
	private static int scale = 1;
	private static int delta = 0;
	private static int ddepth = CvType.CV_16S;
	
	public Sobel(String mode){
		if (mode != "default")  {
			this.mode = Integer.parseInt(mode);
		}
	}
	
	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
		Imgproc.Sobel( (Mat)src.get(0), grad_x, ddepth, 1, 0, 3, scale, delta, Imgproc.BORDER_DEFAULT );
	    Core.convertScaleAbs( grad_x, abs_grad_x );
	    Imgproc.Sobel( (Mat)src.get(0), grad_y, ddepth, 0, 1, 3, scale, delta, Imgproc.BORDER_DEFAULT );
	    Core.convertScaleAbs( grad_y, abs_grad_y );
	    Core.addWeighted( abs_grad_x, 1, abs_grad_y, 0, 0, result );
	    //Highgui.imwrite("/tmp/sobel.png", result);
	    resultList.add(result);
	    return resultList;
	}
	
	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
