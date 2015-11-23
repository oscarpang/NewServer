package Server;
import java.io.Serializable;

public class LoginInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -972911362134671586L;
	private String username;
	private String password;
	public LoginInfo(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
}
