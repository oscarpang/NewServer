package Data;

import java.sql.*;
import java.util.Random;

import Communication.AWSClient;

public class Equipment extends AttributeHolder {
	private static final long serialVersionUID = 4315258998095168332L;
	static float[][][] data;
	static String[] vList = { /* "equipmentID", "typeID", "rankID", */ "MinHP", "MaxHP", "MinMP", "MaxMP",
			"MinPhysicalAttack", "MaxPhysicalAttack", "MinPhysicalDefence", "MaxPhysicalDefence", "MinMagicAttack",
			"MaxMagicAttack", "MinMagicDefence", "MaxMagicDefence", "MinDexterity", "MaxDexterity", "MinLuck",
			"MaxLuck", "MinCriticalHit", "MaxCriticalHit", "growRate" };
	static String[][] nameList = {
			{ "Mask of Distant Dreams", "Bandana of Holy Worlds", "Scaled Cap of Hellish Illusions",
					"Wool Headpiece of Distant Nights", "Wrathful Cloth Cap", "Mourning Wool Headguard",
					"Warlord's Mask of the Prince", "Terror Hood of Fire", "Protection of Frost Power",
					"Gift of Suffering's End", "Casque of Binding Wars", "Faceguard of Unholy Protection",
					"Ivory Headcover of Infinite Worlds", "Ebon Headguard of Distant Nights",
					"Gladiator's Ebon Headguard", "Reincarnated Bronzed Casque", "Faith's Casque of Adventure",
					"Grieving Headcover of Wraiths", "Dawn of Redemption", "Steward of the Seer" },
			{ "Batteplate of Broken Magic", "Tunic of Immortal Misery", "Ivory Batteplate of Broken Bloodlust",
					"Bone Batteplate of Faded Whispers", "Warrior's Ivory Armor", "Banished Bone Cuirass",
					"Twilight's Vest of Deflection", "Frenzied Armor of the Crusader", "Legacy of Unholy Blight",
					"Soul of Dark Powers", "Tunic of Ancient Nightmares", "Vestment of Blessed Worlds",
					"Padded Wraps of Eternal Damnation", "Leather Jerkin of Eternal Fortune", "Vengeful Hide Wraps",
					"Whistling Heavy Leather Vestment", "Legionnaire's Raiment of Assassination",
					"Vengeful Wraps of Lost Voices", "Armament of Frost Power", "Vengeance of the Cataclysm" },
			{ "Birifle", "Broadkiller", "Cutshocker", "Dirksigil", "Drillwhipper", "Forklash", "Heatshiv",
					"Laserstabber", "Metalsword", "Oathchopper", "Poisonorb", "Powerpike", "Shurisigil", "Sigilfinder",
					"Slimesigil", "Sonifist", "Speedheater", "Truthcrusher", "Truthdisk", "Turbomaster", "Unisceptre",
					"Voidmace", "Whirllance", "Whirltracker", "Worldsmasher" },
			{ "Greaves of Damned Hope", "Leggings of Hellish Trials", "Steel Skirt of Fleeting Hell",
					"Chainmail Leggings of Fallen Justice", "Guardian's Bronzed Platelegs", "Chain Platelegs",
					"Skirt of Adventure", "Doom's Legplates of Desecration", "Guard of the Night",
					"Ward of the Harvest", "Breeches of Frozen Protection", "Kilt of Twisted Honor",
					"Heavy Hide Robes of Relentless Souls", "Linen Skirt of Fleeting Honor", "Inherited Silk Legguards",
					"Wretched Scaled Kilt", "Pride's Pants of the Isles", "Stormfury Skirt of the Lone Wolf",
					"Protection of Nature", "Deflector of Fire Protection" } };

	public static String getRandomName(EType eType) {
		Random random = new Random();
		return nameList[eType.ordinal()][Math.abs(random.nextInt(nameList[eType.ordinal()].length))];
	}
	
	static {
		// data = new float[4][5][19];
		AWSClient client = new AWSClient();
		client.sendRequest("Equipment");
		data = (float[][][]) client.getResult();
	}

	private Rank rank;
	private EType eType;
	private int price;

	private Equipment(Rank rank, EType etype) {
		this.rank = rank;
		
		this.eType = etype;
		setName(getRandomName(etype));
		price = 100;
	}

	public static Equipment generateEquipment(Rank rank, EType etype) {
		Equipment equipment = new Equipment(rank, etype);
		Random rand = new Random();
		float[] val = new float[9];
		for (int i = 0; i < vList.length - 1; i += 2) {
			int min = (int) data[etype.ordinal()][rank.ordinal()][i];
			int max = (int) data[etype.ordinal()][rank.ordinal()][i + 1];

			val[i / 2] = Math.abs(rand.nextInt() % (max - min + 1) + min);
		}
		equipment.setALL(val);
		return equipment;
	}

	public static Equipment generateRandomEquipment() {
		Rank[] rank = Rank.values();
		EType[] etype = EType.values();
		Random rand = new Random();
		double rankI=(double)rand.nextInt(125);
		int rankII=(int)Math.pow(rankI, 1/3);
		if (rankII>4) rankII=4;
		return Equipment.generateEquipment(rank[4-rankII], etype[rand.nextInt(etype.length)]);
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public EType geteType() {
		return eType;
	}

	public void seteType(EType eType) {
		this.eType = eType;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("\nName: " + getName());
		sb.append("\nType: " + geteType().toString());
		sb.append("\nRank: " + getRank().toString());
		sb.append("\nLevel: " + getLevel());
		sb.append("\nHP: " + getHP());
		sb.append("\nMP: " + getMP());
		sb.append("\nPhysicalAttack: " + getPhysicalAttack());
		sb.append("\nPhysicalDefence: " + getPhysicalDefence());
		sb.append("\nMagicAttack: " + getMagicAttack());
		sb.append("\nMagicDefence: " + getMagicDefence());
		sb.append("\nDexterity: " + getDexterity());
		sb.append("\nLuck: " + getLuck());
		sb.append("\nCriticalHit: " + String.format("%.2f", getCriticalHit()));

		return sb.toString();
	}

	static public void printData() {
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 5; ++j) {
				StringBuffer sb = new StringBuffer();
				for (int k = 0; k < vList.length; ++k) {
					sb.append(vList[k] + " : " + data[i][j][k] + "\n");
				}

				System.out.println(i + " " + j + " " + sb.toString());
			}
		}
	}

	public void upgrade() {
		setLevel(getLevel() + 1);
		double growRate = data[eType.ordinal()][rank.ordinal()][18];
		float[] val = getAll();
		for (int i = 0; i < val.length; ++i) {
			val[i] *= growRate;
		}
		setALL(val);
	}
	
	public int getUpgradeMoney() {
		return (int)Math.exp(getLevel());
	}
}