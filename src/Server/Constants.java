package Server;

public class Constants
{
	public static Signal LOGINSUCCESS = new Signal("LOGINSUCCESS");
	public static Signal REGISTERSUCCESS = new Signal("REGISTERSUCCESS");

	public static Signal AUTHENTICATEFAIL = new Signal("AUTHENTICATEFAIL");
	public static Signal USERNOTFOUND = new Signal("USERNAMENOTFOUND");
	public static Signal USERNAMEOCCUPIED = new Signal("USERNAMEOCCUPIED");
	
	public static Signal LOGIN = new Signal("LOGIN");
	public static Signal REGISTER = new Signal("REGISTER");
	
	public static Signal CANNOTFINDCHARACTER = new Signal("CANNOTFINDCHARACTER");
	public static Signal CREATNEWCHARACTER = new Signal("CREATNEWCHARACTER");
	
	public static Signal SAVECHARACTER = new Signal("SAVECHARACTER");
	
	public static Signal LEADERBOARD = new Signal("LEADERBOARD");

}
