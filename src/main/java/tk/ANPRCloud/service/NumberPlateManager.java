package tk.ANPRCloud.service;

import java.util.List;

import tk.ANPRCloud.entity.NumberPlateEntity;

public interface NumberPlateManager {
	//This method will be called when a numberPlate object is added
    public void addNumberPlate(NumberPlateEntity numberPlate);
    //This method return list of numberPlates in database
    public List<NumberPlateEntity> getAllNumberPlates();
    //Deletes a numberPlate by it's id
    public void deleteNumberPlate(Integer numberPlateId);
}
