package tk.ANPRCloud.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import tk.ANPRCloud.entity.NumberPlateEntity;

@Repository
public class NumberPlateDaoImpl implements NumberPlateDAO  
{
	//Session factory injected by spring context
    private SessionFactory sessionFactory;
	
    //This method will be called when a numberPlate object is added
	@Override
	public void addNumberPlate(NumberPlateEntity numberPlate) {
		this.sessionFactory.getCurrentSession().save(numberPlate);
	}

	//This method return list of numberPlates in database
	@SuppressWarnings("unchecked")
	@Override
	public List<NumberPlateEntity> getAllNumberPlates() {
		return this.sessionFactory.getCurrentSession().createQuery("from NumberPlateEntity").list();
	}

	//Deletes a numberPlate by it's id
	@Override
	public void deleteNumberPlate(Integer numberPlateId) {
		NumberPlateEntity numberPlate = (NumberPlateEntity) sessionFactory.getCurrentSession()
										.load(NumberPlateEntity.class, numberPlateId);
        if (null != numberPlate) {
        	this.sessionFactory.getCurrentSession().delete(numberPlate);
        }
	}

	//This setter will be used by Spring context to inject the sessionFactory instance
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
