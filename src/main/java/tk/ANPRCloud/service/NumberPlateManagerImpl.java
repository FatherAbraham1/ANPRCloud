package tk.ANPRCloud.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tk.ANPRCloud.dao.NumberPlateDAO;
import tk.ANPRCloud.entity.NumberPlateEntity;

public class NumberPlateManagerImpl implements NumberPlateManager {
	//NumberPlate dao injected by Spring context
    private NumberPlateDAO numberPlateDAO;
	
	//This method will be called when a numberPlate object is added
	@Override
	@Transactional
	public void addNumberPlate(NumberPlateEntity numberPlate) {
		numberPlateDAO.addNumberPlate(numberPlate);
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
