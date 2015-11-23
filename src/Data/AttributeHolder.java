package Data;
import java.io.Serializable;

public class AttributeHolder implements Serializable{
	private static final long serialVersionUID = -4188157438991597320L;
	private int HP;
	private int HPBuff;
	private int HPCurrent;
	private int MP;
	private int MPBuff;
	private int MPCurrent;
	private int physicalAttack;
	private int physicalAttackBuff;
	private int magicAttack;
	private int magicAttackBuff;
	private int physicalDefence;
	private int physicalDefenceBuff;
	private int magicDefence;
	private int magicDefenceBuff;
	private int dexterity;
	private int dexterityBuff;
	private int luck;
	private int luckBuff;
	private double criticalHit;
	private double criticalHitBuff;
	private String name;
	private int level;

	public AttributeHolder() {
		HP = 0;
		HPBuff = 0;
		HPCurrent = 0;

		MP = 0;
		MPCurrent = 0;
		MPBuff = 0;

		physicalAttack = 0;
		physicalDefence = 0;

		magicAttack = 0;
		magicDefence = 0;

		dexterity = 0;
		luck = 0;
		criticalHit = 0;

		level = 1;

		name = "";
	}

	public void setALL(float[] val) {
		// for equipment generation
		HP = (int) val[0];
		MP = (int) val[1];
		physicalAttack = (int) val[2];
		physicalDefence = (int) val[3];
		magicAttack = (int) val[4];
		magicDefence = (int) val[5];
		dexterity = (int) val[6];
		luck = (int) val[7];
		criticalHit = val[8];
	}
	public float[] getAll() {
		// for equipment upgrade
		float[] val = new float[9];
		val[0] = HP;
		val[1] = MP;
		val[2] = physicalAttack;
		val[3] = physicalDefence;
		val[4] = magicAttack;
		val[5] = magicDefence;
		val[6] = dexterity;
		val[7] = luck;
		val[8] = (float) criticalHit;
		return val;
	}
	public int getHP() {
		return HP;
	}

	public void setHP(int HPBasic) {
		this.HP = HPBasic;
	}

	public int getHPBuff() {
		return HPBuff;
	}

	public void setHPBuff(int HPMax) {
		this.HPBuff = HPMax;
	}

	public int getHPCurrent() {
		return HPCurrent;
	}

	public void setHPCurrent(int HPCurrent) {
		this.HPCurrent = HPCurrent;
	}

	public int getMP() {
		return MP;
	}

	public void setMP(int MPBasic) {
		this.MP = MPBasic;
	}

	public int getMPBuff() {
		return MPBuff;
	}

	public void setMPBuff(int MPMax) {
		this.MPBuff = MPMax;
	}

	public int getMPCurrent() {
		return MPCurrent;
	}

	public void setMPCurrent(int MPCurrent) {
		this.MPCurrent = MPCurrent;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getPhysicalDefence() {
		return physicalDefence;
	}

	public void setPhysicalDefence(int physicalDefence) {
		this.physicalDefence = physicalDefence;
	}

	public int getMagicDefence() {
		return magicDefence;
	}

	public void setMagicDefence(int magicDefence) {
		this.magicDefence = magicDefence;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		this.luck = luck;
	}

	public double getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(double criticalHit) {
		this.criticalHit = criticalHit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getDexterityBuff() {
		return dexterityBuff;
	}

	public void setDexterityBuff(int dexterityBuff) {
		this.dexterityBuff = dexterityBuff;
	}

	public int getLuckBuff() {
		return luckBuff;
	}

	public void setLuckBuff(int luckBuff) {
		this.luckBuff = luckBuff;
	}

	public double getCriticalHitBuff() {
		return criticalHitBuff;
	}

	public void setCriticalHitBuff(double criticalHitBuff) {
		this.criticalHitBuff = criticalHitBuff;
	}

	public int getPhysicalAttackBuff() {
		return physicalAttackBuff;
	}

	public void setPhysicalAttackBuff(int physicalAttackBuff) {
		this.physicalAttackBuff = physicalAttackBuff;
	}

	public int getMagicAttackBuff() {
		return magicAttackBuff;
	}

	public void setMagicAttackBuff(int magicAttackBuff) {
		this.magicAttackBuff = magicAttackBuff;
	}

	public int getPhysicalDefenceBuff() {
		return physicalDefenceBuff;
	}

	public void setPhysicalDefenceBuff(int physicalDefenceBuff) {
		this.physicalDefenceBuff = physicalDefenceBuff;
	}

	public int getMagicDefenceBuff() {
		return magicDefenceBuff;
	}

	public void setMagicDefenceBuff(int magicDefenceBuff) {
		this.magicDefenceBuff = magicDefenceBuff;
	}

}