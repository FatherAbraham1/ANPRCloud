package tk.ANPRCloud.service;

import java.util.List;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.entity.NumberPlateFile;

public interface NumberPlateManager {
	//This method will be called when a numberPlate object is added
    public void addNumberPlate(NumberPlateFile numberPlateFile);
    //This method return list of numberPlates in database
    public List<NumberPlateEntity> getAllNumberPlates();
    //Deletes a numberPlate by it's id
    public void deleteNumberPlate(Integer numberPlateId);
}
