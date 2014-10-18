package tk.ANPRCloud.controller;
import tk.ANPRCloud.entity.NumberPlateEntity;

@SuppressWarnings("serial")
public class queryAction extends EditNumberPlateAction {
	public NumberPlateEntity getNumberPlates() {
		return this.numberPlate;
	}
}
