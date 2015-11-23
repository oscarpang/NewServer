package Data;
public enum SkillList {
    Smash,
    CouterBack,
    Combo,
    IceBolt,
    FireBolt,
    Defense;
	public String getDiscription() {
		return discription[this.ordinal()];
	}
	static public String[] discription = {
			"Passively increase the character's damage and chance to knock down opponent when using smash skills",
			"Counterattack allows you to turn your opponent's strength against them. This skill will greatly increase your abilities in close-range combat, as you'll be able to reflect almost any attack. The stronger your enemy's attack, the stronger your counterattack becomes.",
			"Attack TWICE",
			"Encases the target in a solid block of ice, stunning the target",
			"A fireball is created through spinning, creating inertial gravity, flying through the air and creating vast damage to the enemy",
			"You can protect yourself from any attack of the enemy in close-quarter combat by using this Defense skill."
			};
}