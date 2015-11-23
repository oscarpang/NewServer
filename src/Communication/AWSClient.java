package Communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AWSClient extends Thread {
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Object object;
	public AWSClient() {
//		object = null;
//		try {
//			s = new Socket("ec2-54-183-193-30.us-west-1.compute.amazonaws.com", 6666);
//			oos = new ObjectOutputStream(s.getOutputStream());
//			ois = new ObjectInputStream(s.getInputStream());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
	}
	public void sendRequest(String msg) {
//		try {
//			oos.writeObject(msg);
//			oos.flush();
//			object = ois.readObject();
//		} catch (IOException | ClassNotFoundException e) {
//			e.printStackTrace();
//		}
	}
	public Object getResult() {
//		while (object == null) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}
}
