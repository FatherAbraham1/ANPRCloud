package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class GaussianBlur implements NumberPlateFilter {
	private int m_GaussianBlurSize = 127;
	private Mat result;
    ArrayList<Object> resultList = new ArrayList<Object>();
	public GaussianBlur(String thresthod){
		if (thresthod != "default")  {
			this.m_GaussianBlurSize = Integer.parseInt(thresthod);
		}
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
	    // Apply threshold to gray and generate binary image
		result = new Mat();
		Imgproc.GaussianBlur( (Mat)src.get(0), result, new Size(m_GaussianBlurSize, m_GaussianBlurSize), 
				0, 0, Imgproc.BORDER_DEFAULT );
	    resultList.add(result);
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
