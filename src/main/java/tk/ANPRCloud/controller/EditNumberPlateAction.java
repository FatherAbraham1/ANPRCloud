package tk.ANPRCloud.controller;

import java.util.List;

import org.apache.log4j.Logger;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.service.NumberPlateManager;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EditNumberPlateAction extends ActionSupport implements Preparable	
{
	private File file;
	private String contentType;
	private String filename;
	private BufferedImage image;
	
	private static final long serialVersionUID = 1L;
	
	//Logger configured using log4j
	private static final Logger logger = Logger.getLogger(EditNumberPlateAction.class);
	//List of numberPlates; Setter and Getter are below
	private List<NumberPlateEntity> numberPlates;
	//NumberPlate object to be added; Setter and Getter are below
	private NumberPlateEntity numberPlate;
	
	//NumberPlate manager injected by spring context; This is cool !!
	private NumberPlateManager numberPlateManager;

	public void setUpload(File file) {
		this.file = file;
	}

	public void setUploadContentType(String contentType) {
	    this.contentType = contentType;
	}

	public void setUploadFileName(String filename) {
		this.filename = filename;
	}
	
	//This method return list of numberPlates in database
	public String listNumberPlates() {
		logger.info("listNumberPlates method called");
		numberPlates = numberPlateManager.getAllNumberPlates();
		return SUCCESS;
	}

	//This method will be called when a numberPlate object is added
	public String addNumberPlate() {
		logger.info("addNumberPlate method called");
		//numberPlateManager.addNumberPlate(numberPlate);
		try {
			image=ImageIO.read(file);
		    if (image == null) {
		        System.out.println("The file "+filename+" could not be opened , it is not an image");
		        return ERROR;
		    }
		} catch(IOException ex) {
		    System.out.println("The file "+filename+" could not be opened , an error occurred.");
		    return ERROR;
		}
		return SUCCESS;
	}

	//Deletes a numberPlate by it's id passed in path parameter
	public String deleteNumberPlate() {
		logger.info("deleteNumberPlate method called");
		numberPlateManager.deleteNumberPlate(numberPlate.getId());
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
}
