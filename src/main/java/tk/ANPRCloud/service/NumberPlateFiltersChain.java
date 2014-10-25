package tk.ANPRCloud.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

public class NumberPlateFiltersChain {
	private List<NumberPlateFilter> FiltersChain = new ArrayList<NumberPlateFilter>();
	ArrayList<Object> result = new ArrayList<Object>();
	
	public NumberPlateFiltersChain(ArrayList<Object> src){
		this.result = src;
	}
	
	public void addFilter(NumberPlateFilter filter){
		FiltersChain.add(filter);
	}
	
	public void chainProc(){
		for (NumberPlateFilter filter : FiltersChain ) {
			result = filter.proc(result);
		}
	}

	public ArrayList<Object> getResult(){
		return result;
	}
	
	public int size(){
		return FiltersChain.size();
	}
	
	public NumberPlateFilter get(int i){
		return FiltersChain.get(i);
	}
}
