package Data;

import java.util.*;

public class Skill extends AttributeHolder {
	private static final long serialVersionUID = 6354156715565842862L;
	private boolean isActivated;
	private boolean isPositive;
	private SkillList whichSkill;
	private String discription;
	public Skill(SkillList w, boolean flag) {
		whichSkill = w;
		isActivated = false;
		isPositive = flag;
		discription = whichSkill.getDiscription();
	}

	public boolean IsActivated() {
		return isActivated;
	}
	public boolean IsPositive() {
		return isPositive;
	}

	public void setActivated(boolean flag) {
		isActivated = flag;
	}

	public void upgrade() {
		setLevel(getLevel() + 1);
	}

	public SkillList getSkill() {
		return whichSkill;
	}

	public String getName() {
		return whichSkill.toString();
	}
	public String getDiscription() {
		return discription;
	}
	public int getUpgradeAP() {
		return getLevel();
	}
}