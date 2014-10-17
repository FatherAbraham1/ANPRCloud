package tk.ANPRCloud.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.ObjectNotFoundException;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.entity.NumberPlateFile;
import tk.ANPRCloud.exceptions.InvalidFrontEndAccessException;
import tk.ANPRCloud.service.NumberPlateManager;
import tk.ANPRCloud.controller.LoginInterceptor;
import tk.ANPRCloud.service.UserDetails;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EditNumberPlateAction extends ActionSupport implements Preparable, SessionAware
{
	private NumberPlateFile numberPlateFile = new NumberPlateFile();
	
	private static final long serialVersionUID = 1L;
	
	//Logger configured using log4j
	private static final Logger logger = Logger.getLogger(EditNumberPlateAction.class);
	//List of numberPlates; Setter and Getter are below
	private List<NumberPlateEntity> numberPlates;
	//NumberPlate object to be added; Setter and Getter are below
	private NumberPlateEntity numberPlate;
	
	//NumberPlate manager injected by spring context; This is cool !!
	private NumberPlateManager numberPlateManager;

	private Map session;

	public void setUpload(File file) {
		this.numberPlateFile.setFile(file);
	}

	public void setUploadContentType(String contentType) {
	    this.numberPlateFile.setContentType(contentType);
	}

	public void setUploadFileName(String filename) {
		this.numberPlateFile.setFilename(filename);
	}
	
	//This method return list of numberPlates in database
	public String listNumberPlates() {
		logger.info("listNumberPlates method called");
		numberPlates = numberPlateManager.getAllNumberPlates(getUsernameFromCurrentSession());
		return SUCCESS;
	}

	//This method will be called when a numberPlate object is added
	public String addNumberPlate() {
		logger.info("addNumberPlate method called");
		if (numberPlateFile.storeImageFile()){
			numberPlateManager.addNumberPlate(getUsernameFromCurrentSession(), numberPlateFile);
			return SUCCESS;
		} else {
			return ERROR;
		}
	}	

	//Deletes a numberPlate by it's id passed in path parameter
	public String deleteNumberPlate() throws InvalidFrontEndAccessException {
		logger.info("deleteNumberPlate method called");
		numberPlateManager.deleteNumberPlate(getUsernameFromCurrentSession(), numberPlate.getId());
		return SUCCESS;
	}
	
	//Get details of a numberPlate by it's id passed in path parameter
	public String getDetailsOfNumberPlates() throws InvalidFrontEndAccessException {
		logger.info("deleteNumberPlate method called");
		numberPlateManager.getDetailsOfNumberPlate(getUsernameFromCurrentSession(), numberPlate.getId());
		return SUCCESS;
	}
	
	//This method will be called before any of Action method is invoked;
	//So some pre-processing if required.
	@Override
	public void prepare() throws Exception {
		numberPlate = null;
	}

	public void setNumberPlateManager(NumberPlateManager numberPlateManager) {
		this.numberPlateManager = numberPlateManager;
	}

	public List<NumberPlateEntity> getNumberPlates() {
		return numberPlates;
	}

	public void setNumberPlates(List<NumberPlateEntity> numberPlates) {
		this.numberPlates = numberPlates;
	}

	public NumberPlateEntity getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(NumberPlateEntity numberPlate) {
		this.numberPlate = numberPlate;
	}

	@Override
	public void setSession(Map session) {
		this.session = session;
	}
	
	private String getUsernameFromCurrentSession(){
		return ((UserDetails)this.session.get(LoginInterceptor.USER_SESSION_KEY)).getUsername();
	}
}
