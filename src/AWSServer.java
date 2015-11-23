
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.util.Random;
import java.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AWSServer extends Thread {
	// private Vector<AWSThread> awsThreads = new Vector<AWSThread>();

	ServerSocket ss;

	public AWSServer() {
		this.start();
	}

	public void run() {
		try {
			ss = new ServerSocket(6666);
			while (true) {
				Socket s = ss.accept();
				AWSThread awsThread = new AWSThread(s, this);
				awsThread.start();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
			}
		}
	}
}

class AWSThread extends Thread {
	private Socket s;
	private AWSServer awsServer;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public AWSThread(Socket s, AWSServer awsServer) {
		this.awsServer = awsServer;
		this.s = s;
	}

	public void run() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost/EndLessBattle";
			String username = "root";
			String password = "";

			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);

			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			Object obj = ois.readObject();
			if (obj instanceof String) {
				String msg = (String) obj;
				if (msg.equals("Equipment")) {
					try {
						String sql = "SELECT * FROM equipment;";
						PreparedStatement ps = conn.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();

						float[][][] data = new float[4][5][19];
						String[] vList = {
								/* "equipmentID", "typeID", "rankID", */ "MinHP", "MaxHP", "MinMP", "MaxMP",
								"MinPhysicalAttack", "MaxPhysicalAttack", "MinPhysicalDefence", "MaxPhysicalDefence",
								"MinMagicAttack", "MaxMagicAttack", "MinMagicDefence", "MaxMagicDefence",
								"MinDexterity", "MaxDexterity", "MinLuck", "MaxLuck", "MinCriticalHit",
								"MaxCriticalHit", "growRate" };
						while (rs.next()) {
							for (int i = 0; i < vList.length; ++i) {
								data[rs.getInt(2)][rs.getInt(3)][i] = rs.getFloat(i + 4);
							}
						}

						oos.writeObject(data);
					} catch (SQLException sqle) {
						System.out.println("SQLException: " + sqle.getMessage());
					} 
				} else if (msg.equals("Creature")) {
					try {
						String sql = "SELECT * FROM creature;";
						PreparedStatement ps = conn.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();

						float[][][] data = new float[5][100][10];
						String[] vList = { "basicHP", "basicMP", "physicalAttack", "physicalDefence", "magicAttack",
								"magicDefence", "dexterity", "luck", "criticalHit", "exp" };
						while (rs.next()) {
							for (int i = 0; i < vList.length; ++i) {
								data[rs.getInt(3)][rs.getInt(2) - 1][i] = rs.getFloat(i + 4);
							}
						}

						oos.writeObject(data);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (msg.equals("PVP")) {
					try {
						String sql = "SELECT * FROM characters;";
						PreparedStatement ps = conn.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();

						Vector<Object> vc = new Vector<>();
						while (rs.next()) {
							vc.add(MySQLDriver.convertFromBytes(rs.getObject(3)));
						}
						Random random = new Random();

						// oos.writeObject(Creature.generatRandomCreature());
						oos.writeObject(vc.get(Math.abs(random.nextInt(vc.size()))));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else {
				oos.writeObject(null);
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
}