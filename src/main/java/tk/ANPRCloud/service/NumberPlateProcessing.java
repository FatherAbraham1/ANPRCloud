package tk.ANPRCloud.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.fliters.Binarization;
import tk.ANPRCloud.service.fliters.CharsIdentify;
import tk.ANPRCloud.service.fliters.Grayscale;
import tk.ANPRCloud.service.fliters.Locate;
import tk.ANPRCloud.service.fliters.Sobel;
import tk.ANPRCloud.util.Base64;
import tk.ANPRCloud.service.fliters.Crop;

public class NumberPlateProcessing{
	private static NumberPlateFiltersChain 
		PreProcessing, MorphProcessing, LocateProcessing, IdentifyProcessin;
	private Mat src;
	private ArrayList<Object> srcList;
	private ArrayList<Object> resultList;
	static private String[][][] options = 
		{
			{{"RangeColor", "default"}, {"GaussianBlur", "5"}, {"Grayscale", "default"}, {"Sobel", "default"}, {"Binarization", "default"}},
			{{"MaskColor","default"}, {"Morphology","default"}, {"Contours", "default"}},
			{{"Locate", "default"}},
			{{"CharsIdentify", "default"}}
		};
	
	static {
		//Load the JNI library
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		// Load the configuration of svm system
		// Note: When work with Tomcat put the file to here by runing 
		// System.out.println(System.getProperty("user.dir"));
		Locate.SVMLoadModel("svm.xml");
		
		// Load the configuration of ann system
		// Note: When work with Tomcat put the file to here by runing 
		// System.out.println(System.getProperty("user.dir"));
		CharsIdentify.ANNLoadModel("ann.xml");
		CharsIdentify.LoadProvince();
	}
	
	// Constructor
	public NumberPlateProcessing(BufferedImage image) {	
		//Routine for processing
		srcList = new ArrayList<Object>();
		this.src = this.bufferedImage2Mat(image);
		this.srcList.add(this.src.clone());
		int indexOfFiltersChain = -1;
		
		//Append the PreProcessing chain
		PreProcessing = new NumberPlateFiltersChain(this.srcList);
		indexOfFiltersChain++;
		for (int i = 0; i < options[indexOfFiltersChain].length; i++){
			try {
				IdentifyProcessin.addFilter((NumberPlateFilter)(Class.forName("tk.ANPRCloud.service.fliters." +
						options[indexOfFiltersChain][i][0]).getConstructor(String.class).
						newInstance(options[indexOfFiltersChain][i][1])));
			} catch (Exception e) {} 
		}
		
		PreProcessing.chainProc();
		resultList = PreProcessing.getResult();
		
		//Append the MorphProcessing chain
		resultList.add(this.src.clone()); // Apply the src image
		MorphProcessing = new NumberPlateFiltersChain(resultList);
		indexOfFiltersChain++;
		for (int i = 0; i < options[indexOfFiltersChain].length; i++){
			try {
				IdentifyProcessin.addFilter((NumberPlateFilter)(Class.forName("tk.ANPRCloud.service.fliters." +
						options[indexOfFiltersChain][i][0]).getConstructor(String.class).
						newInstance(options[indexOfFiltersChain][i][1])));
			} catch (Exception e) {} 
		}
		MorphProcessing.chainProc();
		resultList = MorphProcessing.getResult();
		
		//Append the LocateProcessing chain
		resultList.set(0, this.src.clone()); // Apply the src image
		LocateProcessing = new NumberPlateFiltersChain(resultList);
		indexOfFiltersChain++;
		for (int i = 0; i < options[indexOfFiltersChain].length; i++){
			try {
				IdentifyProcessin.addFilter((NumberPlateFilter)(Class.forName("tk.ANPRCloud.service.fliters." +
						options[indexOfFiltersChain][i][0]).getConstructor(String.class).
						newInstance(options[indexOfFiltersChain][i][1])));
			} catch (Exception e) {} 
		}
		LocateProcessing.chainProc();
		
		//Append the LocateProcessing chain
		//resultList.set(0, this.src.clone()); // Apply the src image
		/*
		 *  Unit test
		 */
		resultList = new ArrayList<Object>();
		for (int i = 0; i < 7; i++){
			Mat tmp = Highgui.imread("debug_charseg_" + i + ".jpg");
			Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY); 
			resultList.add(tmp);
		}
		IdentifyProcessin = new NumberPlateFiltersChain(resultList);
		indexOfFiltersChain++;
		for (int i = 0; i < options[indexOfFiltersChain].length; i++){
			try {
				IdentifyProcessin.addFilter((NumberPlateFilter)(Class.forName("tk.ANPRCloud.service.fliters." +
						options[indexOfFiltersChain][i][0]).getConstructor(String.class).
						newInstance(options[indexOfFiltersChain][i][1])));
			} catch (Exception e) {} 
		}
		IdentifyProcessin.chainProc();
	}
	
	public String calculateNumber(){
		//Do the processing
		return "fuck";
	}
	
	public Mat bufferedImage2Mat(BufferedImage image)
    {
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
    }

	public String mat2Base64String(Mat src) {
		try {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", src, matOfByte);
		byte[] bytes = matOfByte.toArray();
			String base64 = new String( Base64.encode(bytes), "ASCII");
			return base64;
		} catch (Exception e) {
			return "";
		}
	}
	
	public String getThumbnail() {
		int w = this.src.width();
		int h =  this.src.height();
		ArrayList<Object> resultList = new ArrayList<Object>();
		resultList.add(this.src);
		Mat squareImage;
		if (w > h){
			squareImage = (Mat)(new Crop((w - h) / 2,0, h, h)).proc(resultList).get(0);
		} else {
			squareImage = (Mat)(new Crop(0, (h - w) / 2, w, w)).proc(resultList).get(0);
		}
		Mat result = new Mat();
		Imgproc.resize(squareImage, result, new Size(350, 350), 0, 0, Imgproc.INTER_LINEAR);
		return "data:image/jpg;base64," + mat2Base64String(result);
	}
	
	public String getDetails(){
		//String myString = new JSONObject().put("JSON", "Hello, World!").toString();
		JSONObject jSONObject = new JSONObject();
		for (int i = 0; i < PreProcessing.size(); i++) {
			jSONObject.put(options[0][i][0], "data:image/jpg;base64," + 
				mat2Base64String((Mat)PreProcessing.get(i).getResult().get(0)));
		}
		for (int i = 0; i < MorphProcessing.size(); i++) {
			jSONObject.put(options[1][i][0], "data:image/jpg;base64," + 
				mat2Base64String((Mat)MorphProcessing.get(i).getResult().get(0)));
		}
		for (int i = 0; i < LocateProcessing.size(); i++) {
			jSONObject.put(options[2][i][0], "data:image/jpg;base64," + 
				mat2Base64String((Mat)LocateProcessing.get(i).getResult().get(0)));
		}
		return jSONObject.toString();
	}
}