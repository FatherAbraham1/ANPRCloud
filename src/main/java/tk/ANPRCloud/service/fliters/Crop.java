package tk.ANPRCloud.service.fliters;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Crop implements NumberPlateFilter {
	private Mat result;
	ArrayList<Object> resultList = new ArrayList<Object>();
	private int x, y, width, height;
	public Crop(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public ArrayList<Object> proc( ArrayList<Object>  src) {
		result = new Mat((Mat)src.get(0), new Rect(this.x, this.y, this.width, this.height));
		resultList.add(result);
		return resultList;
	}
	
	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
