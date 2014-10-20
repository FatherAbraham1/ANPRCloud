package tk.ANPRCloud.service;

import java.util.ArrayList;

import org.opencv.core.Mat;


// The type of Objects in ArrayList must be Mat or String
public interface NumberPlateFilter {
	public ArrayList<Object> proc ( ArrayList<Object>  src);
	public ArrayList<Object> getResult();
}
