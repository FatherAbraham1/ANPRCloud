package tk.ANPRCloud.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import tk.ANPRCloud.entity.NumberPlateEntity;
import tk.ANPRCloud.exceptions.InvalidDataAccessException;

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
	public List<NumberPlateEntity> getAllNumberPlates(String username) throws InvalidDataAccessException {
		try{
			Query query = this.sessionFactory.getCurrentSession().createQuery("from NumberPlateEntity WHERE OWNER = :owner");
			query.setParameter("owner", username);
			return query.list();
		} catch (Exception e){
			throw new InvalidDataAccessException();
		}
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

	@Override
	public boolean authorizeUsernameAndId(String username, Integer numberPlateId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDetailsOfNumberPlates(Integer numberPlateId) {
		try{
			Query query = this.sessionFactory.getCurrentSession().createQuery("SELECT DETAILS from NumberPlateEntity WHERE Id = :id");
			query.setParameter("Id", numberPlateId);
			List<String> a = query.list();
		} catch (Exception e){
			try {
				throw new InvalidDataAccessException();
			} catch (InvalidDataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
}
