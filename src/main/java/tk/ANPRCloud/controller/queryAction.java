package tk.ANPRCloud.controller;
import java.util.List;

import tk.ANPRCloud.entity.NumberPlateEntity;

@SuppressWarnings("serial")
public class queryAction extends EditNumberPlateAction {
	
	public List<String> getResult() {
		return this.result;
	}
	
	public NumberPlateEntity getNumberPlate() {
		return this.numberPlate;
	}
	
}
