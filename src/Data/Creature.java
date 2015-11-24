package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.xml.crypto.Data;

import Communication.AWSClient;

public class Creature extends AttributeHolder {
	private static final long serialVersionUID = 4355499790623010232L;
	static String[] vList = { "basicHP", "basicMP", "physicalAttack", "physicalDefence", "magicAttack", "magicDefence",
			"dexterity", "luck", "criticalHit", "exp" };
	static String[] nameList = { "Frightman", "Rotwings", "Phasesword", "Trancebody", "The Forsaken Abortion",
			"The Fickle Malformation", "The Disfigured Entity", "The Dark Thunder Hog", "The Painted Dire Hawk",
			"The Agile Flame Tiger", "Frosthound", "Cinderlich", "Poisontalon", "Stinkmutant", "The Mad Abortion",
			"The Bleak Behemoth", "The Wretched Face", "The Vicious Harlequin Bear", "The Horned Blaze Rhino",
			"The Cobalt Vampire Alligator", "Cryptfang", "Spitegirl", "Tangleseeker", "Chaostooth",
			"The Delirious Screamer", "The Bewitched Revenant", "The Bruised Tumor", "The Vicious Berserker Cat",
			"The Ebon Vampire Ape", "The Patriarch Berserker Elephant", "Vaporserpent", "Smogface", "Decaywings",
			"Decayman", "The Arctic Dweller", "The Hungry Abnormality", "The Black Horror", "The Grim Jester Frog",
			"The Chaotic Cave Lion", "The Chaotic Phantom Phoenix" };
	private int exp;
	private int expNeeded;
	private int gold;
	private Race race;
	private Equipment[] equipments;
	private Vector<Equipment> inventory;
	private Set<Skill> skillsPositive;
	private Set<Skill> skillsPassive;
	private static float[][][] data;
	private int ap;

	static {
		// data = new float[5][100][10];
		AWSClient client = new AWSClient();
		client.sendRequest("Creature");
		data = (float[][][]) client.getResult();
	}
	static public String getRandomName() {
		Random random = new Random();
		return nameList[Math.abs(random.nextInt(nameList.length))];
	}
	private Creature(Race race) {
		this.race = race;
		equipments = new Equipment[EType.values().length];
		skillsPositive = new HashSet<Skill>();
		skillsPassive = new HashSet<Skill>();
		
		learnSkill(new Skill(SkillList.Smash, true), true);
		learnSkill(new Skill(SkillList.FireBolt, true), true);
		learnSkill(new Skill(SkillList.IceBolt, true), true);
		learnSkill(new Skill(SkillList.CouterBack, false), false);
		learnSkill(new Skill(SkillList.Defense, false), false);
		
		inventory = new Vector<Equipment>();
		setName(getRandomName());
		exp = 0;
		gold = 0;
		ap = 0;
	}

	static public Creature generateRandomCreature() {
		Random rand = new Random();
		Race[] race = Race.values();
		return generateCreature(race[rand.nextInt(race.length)]);
	}

	static public Creature generateCreature(Race race) {
		Creature creature = new Creature(race);
		
		updateBasicInfo(creature);

		creature.updateAll();
		creature.resurrect();
		return creature;
	}

	private static void updateBasicInfo(Creature creature) {
		int raceIndex = creature.getRace().ordinal();
		int level = creature.getLevel();
		float[] info = Creature.data[raceIndex][level - 1];
		creature.setHP((int) info[0]);
		creature.setMP((int) info[1]);
		creature.setPhysicalAttack((int) info[2]);
		creature.setPhysicalDefence((int) info[3]);
		creature.setMagicAttack((int) info[4]);
		creature.setMagicDefence((int) info[5]);
		creature.setDexterity((int) info[6]);
		creature.setLuck((int) info[7]);
		creature.setCriticalHit(info[8]);
		creature.setExpNeeded((int) info[9]);
	}

	public void debuff() {
		double rate = 0.8;
		setHP((int) (getHP() * rate));
		setMP((int) (getMP() * rate));
		setPhysicalAttack((int) (getPhysicalAttack() * rate));
		setPhysicalDefence((int) (getPhysicalDefence() * rate));
		setMagicAttack((int) (getMagicAttack() * rate));
		setMagicDefence((int) (getMagicDefence() * rate));
		setDexterity((int) (getDexterity() * rate));
		setLuck((int) (getLuck() * rate));
		setCriticalHit(getCriticalHit() * rate);
	}
	
	public void bossbuff(){
		double rate = 1.1;
		setHP((int)(getHP()*rate));
		setMP((int)(getMP()*rate));
		setPhysicalAttack((int)(getPhysicalAttack()*rate));
		setPhysicalDefence((int)(getPhysicalDefence()*rate));
		setMagicAttack((int)(getMagicAttack()*rate));
		setMagicDefence((int)(getMagicDefence()*rate));
		setDexterity((int)(getDexterity()*rate));
		setLuck((int)(getLuck()*rate));
		setCriticalHit(getCriticalHit()*rate);
	}
	
	public void upgrade() {
		setLevel(getLevel() + 1);
		ap += 2;
		
		exp = exp - expNeeded;
		updateBasicInfo(this);

		updateAll();
		resurrect();
	}

	public void resurrect() {
		setHPCurrent(getHPBuff());
		setMPCurrent(getMPBuff());
	}

	public void equip(Equipment equipment) {
		int index = equipment.geteType().ordinal();
		
		inventory.remove(equipment);
		if (equipments[index] != null) {
			inventory.add(equipments[index]);
		}
		equipments[index] = equipment;
		updateAll();
	}

	public void unequip(Equipment equipment) {
		int index = equipment.geteType().ordinal();
		if (equipments[index] != equipment) {
			return;
		} else {
			inventory.add(equipments[index]);
			equipments[index] = null;
		}
		updateAll();
	}

	public void learnSkill(Skill skill, boolean isPossive) {
		if (isPossive) {
			skillsPositive.add(skill);
		} else {
			skillsPassive.add(skill);
		}
	}

	private void updateAll() {
		int buffHP = 0;
		int buffMP = 0;
		int buffPA = 0;
		int buffPD = 0;
		int buffMA = 0;
		int buffMD = 0;
		int buffDex = 0;
		int buffLuck = 0;
		double buffCH = 0;

		for (Equipment e : equipments) {
			if (e == null)
				continue;
			buffHP += e.getHP();
			buffMP += e.getMP();
			buffPA += e.getPhysicalAttack();
			buffPD += e.getPhysicalDefence();
			buffMA += e.getMagicAttack();
			buffMD += e.getMagicDefence();
			buffDex += e.getDexterity();
			buffLuck += e.getLuck();
			buffCH += e.getCriticalHit();
		}

		setHPBuff(getHP() + buffHP);
		setMPBuff(getMP() + buffMP);
		setPhysicalAttackBuff(getPhysicalAttack() + buffPA);
		setPhysicalDefenceBuff(getPhysicalDefence() + buffPD);
		setMagicAttackBuff(getMagicAttack() + buffMA);
		setMagicDefenceBuff(getMagicDefence() + buffMD);
		setDexterityBuff(getDexterity() + buffDex);
		setLuckBuff(getLuck() + buffLuck);
		setCriticalHitBuff(getCriticalHit() + buffCH);
	}

	public int getExp() {
		return exp;
	}

	public void addExp(int exp) {
		this.exp += exp;
		if (this.exp >= expNeeded) {
			upgrade();
		}
	}

	public int getGold() {
		return gold;
	}

	public void addGold(int gold) {
		setGold(getGold() + gold);
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Equipment[] getEquipments() {
		return equipments;
	}

	public void setEquipments(Equipment[] equipments) {
		this.equipments = equipments;
	}

	public Set<Skill> getPositiveSkills() {
		return skillsPositive;
	}
	public Set<Skill> getPassiveSkills() {
		return skillsPassive;
	}

	public int getExpNeeded() {
		return expNeeded;
	}

	public void setExpNeeded(int expNeeded) {
		this.expNeeded = expNeeded;
	}

	public static Creature getPVPPlayer() {
		AWSClient client = new AWSClient();
		client.sendRequest("PVP");
		return (Creature) client.getResult();
	}

	public Vector<Equipment> getInventory() {
		return inventory;
	}

	public void addEquipment(Equipment equipment) {
		inventory.add(equipment);
	}
	
	public void sell(Equipment e) {
		if (inventory.remove(e)) {
			addGold(e.getPrice());
		}
	}

	public double getSuccessRate(Equipment equipment) {
		int level = equipment.getLevel();
		int luck = getLuck();
		double base;
		switch (level) {
		case 1:
			base = 0.8;
			break;
		case 2:
			base = 0.67;
			break;
		case 3:
			base = 0.54;
			break;
		case 4:
			base = 0.44;
			break;
		case 5:
			base = 0.36;
			break;
		case 6:
			base = 0.30;
			break;
		case 7:
			base = 0.24;
			break;
		case 8:
			base = 0.20;
			break;
		case 9:
			base = 0.15;
			break;
		case 10:
			base = 0.13;
			break;
		case 11:
			base = 0.11;
			break;
		case 12:
			base = 0.09;
			break;
		case 13:
			base = 0.07;
			break;
		case 14:
			base = 0.06;
			break;
		case 15:
			base = 0.05;
			break;
		default:
			base = 0.04;
			break;
		}
		base *= (1+luck/200.0);
		if (base > 1) base = 1;
		return base;
	}
	public int getAP() {
		return ap;
	}
	public void comsumeAP(int point) {
		if (ap < point) return;
		ap -= point;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("\nName: " + getName());
		sb.append("\nRace: " + getRace().toString());
		sb.append("\nLevel: " + getLevel());
		sb.append("\nHP: " + getHPBuff());
		sb.append("\nMP: " + getMPBuff());
		sb.append("\nPhysicalAttack: " + getPhysicalAttackBuff());
		sb.append("\nPhysicalDefence: " + getPhysicalDefenceBuff());
		sb.append("\nMagicAttack: " + getMagicAttackBuff());
		sb.append("\nMagicDefence: " + getMagicDefenceBuff());
		sb.append("\nDexterity: " + getDexterityBuff());
		sb.append("\nLuck: " + getLuckBuff());
		sb.append("\nCriticalHit: " + String.format("%.2f", getCriticalHitBuff()));
		sb.append("\nExp: " + getExp());
		sb.append("\nGold: " + getGold());

		return sb.toString();
	}

}