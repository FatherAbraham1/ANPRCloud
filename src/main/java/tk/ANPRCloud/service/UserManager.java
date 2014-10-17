package tk.ANPRCloud.service;
import java.io.UnsupportedEncodingException;

import tk.ANPRCloud.exceptions.InvalidCookiesException;
import tk.ANPRCloud.exceptions.InvalidDataAccessException;
import tk.ANPRCloud.exceptions.InvalidUsernameOrPasswordException;

public interface UserManager {
	public UserDetails userLoginWithUsernameAndPassword(String username,
			String password) throws InvalidUsernameOrPasswordException, UnsupportedEncodingException;
	public UserDetails userLoginWithCookies(String value) throws UnsupportedEncodingException, InvalidCookiesException, InvalidDataAccessException;
	public UserDetails getUserDetails();
	public void addUser(String username, String password) throws InvalidDataAccessException;
	public boolean usernameExist(String username);
}

