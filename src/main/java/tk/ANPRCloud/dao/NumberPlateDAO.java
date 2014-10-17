package tk.ANPRCloud.dao;

import java.util.List;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.exceptions.InvalidDataAccessException;

public interface NumberPlateDAO 
{
	//This method will be called when a numberPlate object is added
    public void addNumberPlate(NumberPlateEntity numberPlate);
    //This method return list of numberPlates in database
    public List<NumberPlateEntity> getAllNumberPlates(String username) throws InvalidDataAccessException;
    //Deletes a numberPlate by it's id
    public void deleteNumberPlate(Integer numberPlateId);
    //Authorize the id match the corresponding username
	public boolean authorizeUsernameAndId(String username, Integer numberPlateId);
	public String getDetailsOfNumberPlates(Integer numberPlateId);
}