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
import tk.ANPRCloud.service.fliters.Grayscale;
import tk.ANPRCloud.service.fliters.Sobel;
import tk.ANPRCloud.util.Base64;
import tk.ANPRCloud.service.fliters.Crop;

public class NumberPlateProcessing{
	private static NumberPlateFiltersChain PreProcessing;
	private Mat src;
	private Mat result;
	static private String[][][] options =  {{{"Grayscale","default"}, {"Sobel","default"}, {"Binarization","default"}}};
	
	static {
		//Load the JNI library
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
	}
	
	// Constructor
	public NumberPlateProcessing(BufferedImage image) {	
		//Routine for processing
		this.src = this.bufferedImage2Mat(image);
		PreProcessing = new NumberPlateFiltersChain(this.src);
		//Append the filter chain
		for (int i = 0; i < options.length; i++){
			//Class.forName(options[i][0]).getConstructor(String.class).newInstance(options[i][1]);
			try {
				PreProcessing.addFilter((NumberPlateFilter)(Class.forName("tk.ANPRCloud.service.fliters." + options[0][i][0]).getConstructor(String.class).newInstance(options[0][i][1])));
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PreProcessing.chainProc();
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
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", src, matOfByte);
		byte[] bytes = matOfByte.toArray();
		try {
			String base64 = new String( Base64.encode(bytes), "ASCII");
			return base64;
		} catch (UnsupportedEncodingException e) {
			return null;
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
			jSONObject.put(options[0][i][0], "data:image/jpg;base64," + mat2Base64String((Mat)PreProcessing.get(i).getResult().get(0)));
		}
		return jSONObject.toString();
	}
}
