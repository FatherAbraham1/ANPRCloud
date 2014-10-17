package tk.ANPRCloud.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import tk.ANPRCloud.exceptions.InvalidUsernameOrPasswordException;
import tk.ANPRCloud.service.UserDetails;
import tk.ANPRCloud.service.UserManager;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;


public class UserManagerAction extends ActionSupport implements Preparable,ServletResponseAware, ServletRequestAware, SessionAware{

	//Employee manager injected by spring context; This is cool !!
	private UserManager userManager;
	private String username;
	private String password;
	private String confirmPassword;
	private boolean rememberMe;
	private HttpServletResponse response;
	private HttpServletRequest request;
	private Map session;
	private String goingToURL;

	@SuppressWarnings("unchecked")
	public String login() throws Exception{
		try {
			UserDetails userDetails = userManager.userLoginWithUsernameAndPassword(username, password);
			if (rememberMe) {
				Cookie cookie = new Cookie(LoginInterceptor.COOKIE_REMEMBERME_KEY, userDetails.getCookieString());
				cookie.setMaxAge(60 * 60 * 24 * 14);
				response.addCookie(cookie);
			}
			session.put(LoginInterceptor.USER_SESSION_KEY, userDetails);
			String goingToURL = (String) session.get(LoginInterceptor.GOING_TO_URL_KEY);
			if (StringUtils.isNotBlank(goingToURL)){
				setGoingToURL(goingToURL);
				session.remove(LoginInterceptor.GOING_TO_URL_KEY);
			} else {
				setGoingToURL("index.action");
			}
			return SUCCESS;
		} catch (InvalidUsernameOrPasswordException e) {
			addActionMessage("user name or password is not corrected.");
			return INPUT;
		} catch (Exception e){
			return INPUT;
		}
	}
	
	public String logout() throws Exception{
		HttpSession session = request.getSession(false);
		if (session!=null)
			session.removeAttribute(LoginInterceptor.USER_SESSION_KEY);
		
		Cookie[] cookies = request.getCookies();
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if (LoginInterceptor.COOKIE_REMEMBERME_KEY.equals(cookie
						.getName())) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					return "login";
				}
			}
		}
		return "login";
	}

	public String register() throws Exception{
		try {
			if(username.equals("") || password.equals("")){
				addActionMessage("Username or password is empty.");
				return INPUT;
			} else if(!password.equals(confirmPassword)){
				addActionMessage("Confirm password not matchs the password.");
				return INPUT;
			} else if (userManager.usernameExist(username)){
				addActionMessage("Username exist.");
				return INPUT;				
			} else {
				userManager.addUser(username, password);
				return SUCCESS;
			}
		} catch (Exception e){
			return INPUT;
		}
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public String getGoingToURL() {
		return goingToURL;
	}
	public void setGoingToURL(String goingToURL) {
		this.goingToURL = goingToURL;
	}
	public boolean isRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	public String getLoginName() {
		return username;
	}
	public void setLoginName(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
