package tk.ANPRCloud.service;

import java.util.List;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.entity.NumberPlateFile;
import tk.ANPRCloud.exceptions.InvalidDataAccessException;
import tk.ANPRCloud.exceptions.InvalidFrontEndAccessException;

public interface NumberPlateManager {
	//This method will be called when a numberPlate object is added
    public void addNumberPlate(String username, NumberPlateFile numberPlateFile);
    //This method return list of numberPlates in database
    public List<NumberPlateEntity> getAllNumberPlates(String username);
    //Deletes a numberPlate by it's id
    public void deleteNumberPlate(String username, Integer numberPlateId) throws InvalidFrontEndAccessException;
	public String getDetailsOfNumberPlate(String username,Integer numberPlateId) throws InvalidFrontEndAccessException;
}
