package tk.ANPRCloud.dao;

import tk.ANPRCloud.entity.UserEntity;
import tk.ANPRCloud.exceptions.InvalidCookiesException;
import tk.ANPRCloud.exceptions.InvalidDataAccessException;

public interface UserDAO {

	String getPasswordsByUsername(String username);

	void createPersistentLoginsEntity(String username, String series, String token, String LastUsed);

	String getUsernameBySeriesAndToken(String series, String token) throws InvalidCookiesException;

	void setTokenBySeries(String series, String token) throws InvalidDataAccessException;

	void addUserEntity(UserEntity userEntity) throws InvalidDataAccessException;

}
