package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class RangeColor implements NumberPlateFilter {
	private int[] color = { 100, 38, 27 };
	private double[] scale = { 2.0, 6.0, 7.0 };
  	private int threshod = 127;
	private Mat result;
    
	ArrayList<Object> resultList = new ArrayList<Object>();
	public RangeColor(String thresthod){
		if (thresthod != "default")  {
			this.threshod = Integer.parseInt(thresthod);
		}
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
	    // Split channels from src
		List<Mat> srcChannels = new ArrayList<Mat>();
		Mat[] resultChannels = new Mat[3];
		Core.split((Mat)src.get(0), srcChannels);
		result = new Mat(srcChannels.get(0).rows(), srcChannels.get(0).cols(), srcChannels.get(0).type());
		for (int i = 0; i < srcChannels.size(); i++){
			resultChannels[i] = new Mat();
			Core.absdiff(srcChannels.get(i), new Scalar(color[i]), resultChannels[i]);
			Core.divide(resultChannels[i], new Scalar(scale[i]), resultChannels[i]);
			resultChannels[i] = resultChannels[i].mul(resultChannels[i]);
			Core.absdiff(resultChannels[i], new Scalar(255), resultChannels[i]);
			Core.divide(resultChannels[i], new Scalar(3), resultChannels[i]);
			Core.add(result, resultChannels[i], result);
		}
		Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
		
		// For debug 
		Highgui.imwrite("/tmp/RangeColorproc.png", (Mat)resultChannels[1]);
		Highgui.imwrite("/tmp/RangeColor.png", result);
	    resultList.add(result);
		return resultList;
	}

	private void sqrtAndReverse(Mat src) {
		byte[] data = new byte[1];
		for (int row = 0; row < src.rows(); row++){
			for (int col = 0; col < src.cols(); col++ ){
				src.get(row, col, data);
				//short fuck = (short) (255 - data[0] * data[0]);
				int fuck = data[0];
				//fuck = fuck * fuck ;
				System.out.println(data[0]);
				data[0] = (byte)(fuck * 2);
				//System.out.println(255 - data[0] * data[0]);
				src.put(row, col, data);
			}
		}
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
