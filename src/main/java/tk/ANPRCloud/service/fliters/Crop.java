package tk.ANPRCloud.service.fliters;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Crop implements NumberPlateFilter {
	private Mat result;
	private int x, y, width, height;
	public Crop(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Mat proc(Mat src) {
		result = new Mat(src, new Rect(this.x, this.y, this.width, this.height));
		return result;
	}
	
	@Override
	public Mat getResult() {
		return result;
	}
}
