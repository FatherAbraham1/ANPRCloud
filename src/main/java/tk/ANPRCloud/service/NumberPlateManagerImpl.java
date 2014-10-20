package tk.ANPRCloud.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tk.ANPRCloud.exceptions.InvalidDataAccessException;
import tk.ANPRCloud.exceptions.InvalidFrontEndAccessException;
import tk.ANPRCloud.controller.EditNumberPlateAction;
import tk.ANPRCloud.dao.NumberPlateDAO;
import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.entity.NumberPlateFile;
import tk.ANPRCloud.util.Base64;

public class NumberPlateManagerImpl implements NumberPlateManager {
	private static final Logger logger = Logger.getLogger(NumberPlateManagerImpl.class);
	
	//NumberPlate dao injected by Spring context
    private NumberPlateDAO numberPlateDAO;    
    
    //NumberPlateEntity converted from image file
    private NumberPlateEntity numberPlateEntity;
    
    //NumberPlateProcessing process the file
	private NumberPlateProcessing numberPlateProcessing;
    
	//This method will be called when a numberPlate object is added
	@Override
	@Transactional
	public int addNumberPlate(String username, NumberPlateFile numberPlateFile) {
		numberPlateEntity = new NumberPlateEntity();
		// Set the numberPlateEntity properties
		numberPlateProcessing = new NumberPlateProcessing(numberPlateFile.getImage());
		numberPlateEntity.setImage(numberPlateProcessing.getThumbnail());
		numberPlateEntity.setContenType(numberPlateFile.getContentType());
		numberPlateEntity.setFileName(Base64Encode(numberPlateFile.getFilename()));
		numberPlateEntity.setOwner(username);
		numberPlateEntity.setNumber(numberPlateProcessing.calculateNumber());
		numberPlateEntity.setDetails(numberPlateProcessing.getDetails());
		return numberPlateDAO.addNumberPlate(numberPlateEntity);
	}
	
	//This method return list of numberPlates in database
	@Override
	@Transactional
	public List<Integer> getAllNumberPlates(String username) {
		try {
			return numberPlateDAO.getAllNumberPlates(username);
		} catch (InvalidDataAccessException e){
			logger.info("No any number plate or data access error!");
			return null;
		}
	}
	
	//Deletes a numberPlate by it's id
	@Override
	@Transactional
	public void deleteNumberPlate(String username, Integer numberPlateId) throws InvalidFrontEndAccessException {
		try {
			if (numberPlateDAO.authorizeUsernameAndId(username, numberPlateId)){
				numberPlateDAO.deleteNumberPlate(numberPlateId);
			} else {
				throw new InvalidFrontEndAccessException();
			}
		} catch (InvalidDataAccessException e) {
			// TODO Auto-generated catch block
			throw new InvalidFrontEndAccessException();
		}
	}
	
	//Get details of numberPlate by it's id
	@Override
	@Transactional
	public NumberPlateEntity queryNumberPlate(String username, Integer numberPlateId) throws InvalidFrontEndAccessException {
		try {
			if (numberPlateDAO.authorizeUsernameAndId(username, numberPlateId)){
				NumberPlateEntity entity = numberPlateDAO.queryNumberPlate(numberPlateId);
				numberPlateDAO.deleteNumberPlate(entity.getId());
				return entity;
			} else {
				throw new InvalidFrontEndAccessException();
			}
		} catch (InvalidDataAccessException e){
			logger.info("No any number plate or data access error!");
			return null;
		}
	}
	
	//This setter will be used by Spring context to inject the dao's instance
	public void setNumberPlateDAO(NumberPlateDAO numberPlateDAO) {
		this.numberPlateDAO = numberPlateDAO;
	}
	
	private String Base64Encode(String value) {
		try {
			return new String( Base64.encode(value.getBytes("UTF-8")), "UTF-8" ) ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
