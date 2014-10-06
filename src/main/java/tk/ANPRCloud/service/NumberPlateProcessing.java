package tk.ANPRCloud.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import tk.ANPRCloud.service.fliters.Binarization;
import tk.ANPRCloud.service.fliters.Grayscale;
import tk.ANPRCloud.service.fliters.Sobel;

public class NumberPlateProcessing{
	private static List<NumberPlateFilter> FiltersChain = new ArrayList<NumberPlateFilter>();
	private Mat src;
	private Mat result;
	
	static {
		//Load the JNI library
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		//Append the filter chain
		FiltersChain.add((NumberPlateFilter)(new Grayscale(0)));
		FiltersChain.add((NumberPlateFilter)(new Sobel(0)));
		//FiltersChain.add((NumberPlateFilter)(new Grayscale(0)));
		FiltersChain.add((NumberPlateFilter)(new Binarization(127)));
	}
	
	public String doProc(String FilePath) {	

		//Routine for processing
		this.src = Highgui.imread(FilePath);
		this.result = src.clone();
		
		//Do the processing
		this.ChainProc();
		return "fuck";
	}
	
	public void ChainProc(){
		for (int i = 0; i < FiltersChain.size(); i++) {
			result = FiltersChain.get(i).proc(result);
		}
	}
}
