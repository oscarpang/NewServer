package Server;
import java.io.Serializable;


public class Signal implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1565989324260953368L;

	
	private final String signalDescription;

	public Signal(String des) {
		signalDescription = des;
	}
	
	public String getMessage()
	{
		return signalDescription;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Signal)
		{
			Signal other = (Signal)obj;
			return (this.signalDescription).equals(other.signalDescription);
		}
		else{
			return false;
		}
	}
	
}
