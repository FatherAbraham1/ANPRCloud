package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Morphology implements NumberPlateFilter {
	private int morphSizeWidth = 17;
	private int morphSizeHeight = 3;
	private Mat result;
    ArrayList<Object> resultList = new ArrayList<Object>();
	public Morphology(String morphSizeWidth){
		if (morphSizeWidth != "default")  {
			this.morphSizeWidth = Integer.parseInt(morphSizeWidth);
		}
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(morphSizeWidth, morphSizeHeight) );
		Imgproc.morphologyEx((Mat)src.get(0), result, Imgproc.MORPH_CLOSE, element);
	    //Highgui.imwrite("/tmp/Morphology.png", result);
	    resultList.add(result);
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
