import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import Data.Creature;
import Server.Constants;
import Server.LoginInfo;
import Server.Signal;

public class LoginServer
{
	private Vector<Communicator> ctVector = new Vector<Communicator>();

	public LoginServer()
	{
		new AWSServer();
		ServerSocket ss = null;
		try
		{
			System.out.println("Starting Chat Server");
			ss = new ServerSocket(6789);
			while (true)
			{
				System.out.println("Waiting for client to connect...");
				Socket s = ss.accept();
				System.out.println("Client " + s.getInetAddress() + ":" + s.getPort() + " connected");
				Communicator ct = new Communicator(s, this);
				ctVector.add(ct);
				ct.start();
			}
		}
		catch (IOException ioe)
		{
			System.out.println("IOE: " + ioe.getMessage());
		}
		finally
		{
			if (ss != null)
			{
				try
				{
					ss.close();
				}
				catch (IOException ioe)
				{
					System.out.println("IOE closing ServerSocket: " + ioe.getMessage());
				}
			}
		}
	}

	public static int hash(String password)
	{

		char[] charPass = password.toCharArray();
		long tran = 0;

		for (int i = 0; i < password.length(); i++)
		{
			//
			tran += ((int) charPass[i]) * ((long) (Math.pow(128, (password.length() - i - 1))));
			// cout << tran << endl;
		}

		long[] convert = new long[4];

		for (int i = 3; i >= 0; i--)
		{
			convert[i] = tran % 65521;
			tran /= 65521;
			// cout << convert[i] << endl;
		}

		int hashValue = (int) ((45912 * convert[0] + 35511 * convert[1] + 65169 * convert[2] + 4625 * convert[3])
				% 65521);

		// std::cout << hashValue << std::endl;
		return hashValue;

	}

	public void removeCommunicator(Communicator comm)
	{
		ctVector.remove(comm);
	}

	public static void main(String[] args)
	{
		new LoginServer();
	}
}

class Communicator extends Thread
{
	private LoginServer			ls;
	@SuppressWarnings("unused")
	private Socket				s;
	private ObjectOutputStream	oos;
	private ObjectInputStream	ois;

	private String username;

	public Communicator(Socket s, LoginServer ls)
	{
		this.ls = ls;
		this.s = s;
		username = null;

		try
		{
			this.oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			this.ois = new ObjectInputStream(s.getInputStream());
		}
		catch (IOException ioe)
		{
			System.out.println("IOE in ChatThread constructor: " + ioe.getMessage());
		}
	}

	public void sendObject(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public void run()
	{
		Object obj = null;
		try
		{
			while (true)
			{
				obj = ois.readObject();
				if (obj instanceof Signal)
				{
					Signal signal = (Signal) obj;
					if (signal.equals(Constants.LOGIN))
					{
						obj = ois.readObject();
						if (obj instanceof LoginInfo)
						{
							LoginInfo login = (LoginInfo) obj;
							System.out.println("Goood");

							MySQLDriver driver = new MySQLDriver();
							driver.connect();
							if (driver.doesExist(login.getUsername()))
							{
								System.out.println("Exist");
								int dbPassword = driver.getPassword(login.getUsername());
								int hash = LoginServer.hash(login.getPassword());
								if (dbPassword == hash)
								{
									System.out.println("Login Successfully");
									sendObject(Constants.LOGINSUCCESS);
									username = login.getUsername();

									if (driver.hasCharacter(login.getUsername()))
									{
										Creature userCharacter = driver.readCharacter(login.getUsername());
										sendObject(userCharacter);
									}
									else
									{
										sendObject(Constants.CANNOTFINDCHARACTER);
									}
								}
								else
								{
									System.out.println("Authenticate fails");
									sendObject(Constants.AUTHENTICATEFAIL);
								}
							}
							else
							{
								System.out.println("Cannot find user");
								sendObject(Constants.USERNOTFOUND);
							}
						}
					}
					else if (signal.equals(Constants.REGISTER))
					{
						obj = ois.readObject();
						if (obj instanceof LoginInfo)
						{
							LoginInfo login = (LoginInfo) obj;
							System.out.println("Good");

							MySQLDriver driver = new MySQLDriver();
							driver.connect();
							if (driver.doesExist(login.getUsername()))
							{
								System.out.println("Exist");
								sendObject(Constants.USERNAMEOCCUPIED);
							}
							else
							{
								System.out.println("Not Exist");
								int hash = LoginServer.hash(login.getPassword());
								driver.add(login.getUsername(), hash);

								sendObject(Constants.REGISTERSUCCESS);
								username = login.getUsername();
							}
						}
					}
					else if (signal.equals(Constants.CREATNEWCHARACTER))
					{
						obj = ois.readObject();

						Creature creature = (Creature) obj;
						if (username != null)
						{
							MySQLDriver driver = new MySQLDriver();
							driver.connect();
							driver.addNewCharacter(username, creature);
						}
						else
						{
							System.out.println("Server: Username is null when trying to create new character");
						}

					}
					else if(signal.equals(Constants.SAVECHARACTER))
					{
						System.out.println("Save character");
						obj = ois.readObject();
						Creature creature = (Creature)obj;
						
						if(username!=null)
						{
							System.out.println("Update character for" + username);
							MySQLDriver driver = new MySQLDriver();
							driver.connect();
							driver.updateCharacter(username, creature);
						}
						else
						{
							System.out.println("Server: Username is null when trying to update character");
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			ls.removeCommunicator(this);
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}

}
