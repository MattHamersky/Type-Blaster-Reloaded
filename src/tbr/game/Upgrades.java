package tbr.game;

public class Upgrades {

	private static boolean[] upgrades = new boolean[8];
	
	public static void loadUpgrades() {
		boolean[] temp = Profile.currentUser.getAllUpgrades();
		for(int i = 0; i < temp.length; i++)
			upgrades[i] = temp[i];
	}
	
	public static boolean isUpgraded(int upgrade) {
		return upgrades[upgrade];
	}
	
	public static void setUpgraded(int upgrade, boolean status) {
		upgrades[upgrade] = status;
	}
	
}
