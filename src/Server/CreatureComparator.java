package Server;

import java.util.Comparator;

import Data.Creature;

public class CreatureComparator implements Comparator<Creature>
{

	public int compare(Creature o1, Creature o2)
	{
		if(o1.getCP() > o2.getCP())
			return 1;
		else
			return 0;
	}

}
