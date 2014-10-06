package tk.ANPRCloud.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tk.ANPRCloud.dao.NumberPlateDAO;
import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.entity.NumberPlateFile;

public class NumberPlateManagerImpl implements NumberPlateManager {
	//NumberPlate dao injected by Spring context
    private NumberPlateDAO numberPlateDAO;    
    
    //NumberPlateEntity converted from image file
    private NumberPlateEntity numberPlateEntity;
    
    //NumberPlateProcessing process the file
	private NumberPlateProcessing numberPlateProcessing = new NumberPlateProcessing();
    
	//This method will be called when a numberPlate object is added
	@Override
	@Transactional
	public void addNumberPlate(NumberPlateFile numberPlateFile) {
		String Number = numberPlateProcessing.doProc(numberPlateFile.getFile().getAbsolutePath());
		//System.out.println(Number);
		//System.out.println(numberPlateFile.getFile().getAbsolutePath());
		//numberPlateDAO.addNumberPlate(numberPlateEntity);
	}
	
	//This method return list of numberPlates in database
	@Override
	@Transactional
	public List<NumberPlateEntity> getAllNumberPlates() {
		return numberPlateDAO.getAllNumberPlates();
	}
	//Deletes a numberPlate by it's id
	@Override
	@Transactional
	public void deleteNumberPlate(Integer numberPlateId) {
		numberPlateDAO.deleteNumberPlate(numberPlateId);
	}
	
	//This setter will be used by Spring context to inject the dao's instance
	public void setNumberPlateDAO(NumberPlateDAO numberPlateDAO) {
		this.numberPlateDAO = numberPlateDAO;
	}
}
