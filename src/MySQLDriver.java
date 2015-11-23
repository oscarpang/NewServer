

//import java.sql.SQLException;

import com.mysql.jdbc.Driver;

import Data.Creature;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

public class MySQLDriver {
	
	private Connection con;
	private final static String selectName = "SELECT * FROM LoginInfo WHERE USERNAME=?";
	private final static String addUser = "INSERT INTO LoginInfo(USERNAME, userpasswordHash) VALUES(?,?)";
	
	private static final String addCharacter = "INSERT INTO characters(userID, characterObject) VALUES (?, ?)";
	private static final String readCharacter = "SELECT characterObject FROM characters WHERE userID = ?";
	private static final String updateCharacter = "UPDATE characters SET characterObject = ? WHERE userID = ?";
	
	private static final String getUserID = "SELECT * FROM LoginInfo WHERE USERNAME = ?";
	
	private static final String selectCharacter = "SELECT * FROM characters WHERE USERID = ?";
	private static final String getAllCharacter = "SELECT characterObject FROM characters WHERE userID > 0";

	
	
//	private final static String updateTable = "UPDATE FACTORYORDERS SET CREATED = ? WHERE NAME = ?";
	
	public MySQLDriver()
	{
		try{
			new Driver();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void connect()
	{
		try{
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EndLessBattle?user=root&password=");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String username, int passwordHash)
	{
		try{

			PreparedStatement ps = con.prepareStatement(addUser);
			ps.setString(1, username);
			ps.setInt(2, passwordHash);
			ps.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static Creature convertFromBytes(Object obj) throws IOException, ClassNotFoundException
	{
		byte[] st = (byte[]) obj;					
		ByteArrayInputStream baip = new ByteArrayInputStream(st);
	    ObjectInputStream ois = new ObjectInputStream(baip);
		Creature creature = (Creature) ois.readObject();
		
		return creature;
	}
	
	public Creature readCharacter(String username)
	{
		try{
			int userID = getUserID(username);
			
			Object obj = null;
			
			if(userID != -1)
			{
				PreparedStatement readCharacterPS = con.prepareStatement(readCharacter);
				readCharacterPS.setInt(1, userID);
				
				ResultSet characterResult = readCharacterPS.executeQuery();
				while(characterResult.next())
				{
					obj = characterResult.getObject(1);
					
					byte[] st = (byte[]) obj;					
					ByteArrayInputStream baip = new ByteArrayInputStream(st);
				    ObjectInputStream ois = new ObjectInputStream(baip);
					Creature creature = (Creature) ois.readObject();
					
					return creature;
				}
			}

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addNewCharacter(String username, Creature creature)
	{
		try{

			int userID = getUserID(username);
			
			if(userID != -1)
			{
				PreparedStatement addCharacterPS = con.prepareStatement(addCharacter);
				addCharacterPS.setInt(1, userID);
				addCharacterPS.setObject(2, creature);
				
				addCharacterPS.executeUpdate();
			}

		} catch(SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	
	public void updateCharacter(String username, Creature creature)
	{
		try
		{
			System.out.println(username + " try to save character");
			
			
			if(hasCharacter(username))
			{
				int userID = getUserID(username);
				
				System.out.println("Find user id");
				
				if(userID != -1)
				{
					PreparedStatement updateCharacterPS = con.prepareStatement(updateCharacter);
					updateCharacterPS.setObject(1, creature);
					updateCharacterPS.setInt(2, userID);
					
					updateCharacterPS.executeUpdate();
					
					System.out.println("Execute update");
					
				}
			}
			
			Creature cc = readCharacter(username);
			System.out.println("After update");
			System.out.println(cc.getGold());
			System.out.println(cc.getInventory().size());
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

	}
	
	
	private int getUserID(String username)
	{
		PreparedStatement getIDPS;
		try
		{
			getIDPS = con.prepareStatement(getUserID);
			getIDPS.setString(1, username);
			ResultSet result = getIDPS.executeQuery();
			
			int userID = -1;
			while(result.next())
			{
				userID = result.getInt(1);
				return userID;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return -1;
		
	}
	
	public boolean hasCharacter(String username)
	{
		try
		{
			int userID = getUserID(username);
			
			if(userID != -1)
			{
				PreparedStatement selectCharacterPS = con.prepareStatement(selectCharacter);
				selectCharacterPS.setInt(1, userID);
				
				ResultSet characterResult = selectCharacterPS.executeQuery();
				
				while(characterResult.next())
				{
					return true;
				}

			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public int getPassword(String username)
	{
		if(doesExist(username))
		{

			try
			{
				PreparedStatement ps = con.prepareStatement(selectName);

				ps.setString(1, username);
				ResultSet result = ps.executeQuery();
				while(result.next())
				{
					int hash = result.getInt(3);
					return hash;
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return 0;
	}
	
	public boolean doesExist(String username)
	{
		try{
			PreparedStatement ps = con.prepareStatement(selectName);
			ps.setString(1, username);
			ResultSet result = ps.executeQuery();
			while(result.next()) {
				return true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Vector<Creature> getSortedCreatures()
	{
		PriorityQueue<Creature> creatureList = new PriorityQueue<Creature>(10, new Comparator<Creature>()
		{

			@Override
			public int compare(Creature o1, Creature o2)
			{
				if(o1.getCP() >= o2.getCP())
					return 1;
				else
					return 0;
			}
			
		});
		
		
		Vector<Creature> sortedCreatureList = new Vector<>();

		
		try{
			PreparedStatement ps = con.prepareStatement(getAllCharacter);
			ResultSet result = ps.executeQuery();
			while(result.next()) 
			{
				byte[] data = (byte [])result.getObject(1);
				Creature aCreature = (Creature)Serializer.deserialize(data);
				
				sortedCreatureList.add(aCreature);
//				creatureList.add(aCreature);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		int counter = 0;
//		while(!creatureList.isEmpty())
//		{
//			if(counter > 9)
//			{
//				break;
//			}
//			sortedCreatureList.add(creatureList.poll());
//			counter++;
//		}
		
		return sortedCreatureList;
	}
	
	public void stop()
	{
		try{
			con.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
}
