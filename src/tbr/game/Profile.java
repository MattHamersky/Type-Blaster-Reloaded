package tbr.game;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Profile {

	/////////////////////////
	//static fields
	////////////////////////
	public static Profile currentUser;
	
	
	
	///////////////////////
	//member fields
	///////////////////////
	private String name;
	
	//upgrades
	public static final int INCREASED_HEALTH = 0; //more health
	public static final int SHIELD = 1; //shield that can absorb a single word before having to recharge
	public static final int ADDITIONAL_SHIELD_CHARGE = 2; //shield can absorb an additional word before having to cooldown
	public static final int DECREASED_SHIELD_COOLDOWN = 3; //shield cooldown time is reduced
	public static final int SLOW_LASER = 4; //laser that slows down the word that is currently being typed
	public static final int MISSILE_UPPER = 5; //missile that destroys a word but then has to recharge
	public static final int MISSILE_LOWER = 6; //missile that destroys a word but then has to recharge
	public static final int INCREASE_SPECIAL_WORD_SPAWN = 7; //increases the chance that a special word will replace a normal word
	
	public static final int NUMBER_OF_UPGRADES = 8;
	public static final int NUMBER_OF_LEVELS = 10;
	
	private boolean[] upgrades = new boolean[8];
	
	private Level[] levels = new Level[10];
	private Level[] legendaryLevels = new Level[10];
	
	private int stars = 0;
	
	public Profile(String name) {
		this.name = name;
		checkForValidPath();
		constructorHelper(new File("data/profiles/"+name+".profile"));
	}
	
	public Profile(File file) {
		constructorHelper(file);
	}
	
	private void checkForValidPath() {
		File data = new File("data");
		if(!data.exists())
			data.mkdir();
		
		File profiles = new File("data/profiles");
		if(!profiles.exists())
			profiles.mkdir();
	}
	
	private void constructorHelper(File file) {
		//if the file exists try reading the contents
		if(file.exists()) {
			loadExistingProfile(file);
		}
		//if it does not, we are creating a new profile
		else {
			createAndLoadNewProfile(file.getName().substring(0, file.getName().length()-8));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Level getLevel(int level) {
		if(level >= levels.length || level < 0)
			return null;
		return levels[level];
	}
	
	public Level[] getAllLevels() {
		return levels;
	}
	
	public Level getLegendaryLevel(int level) {
		if(level >= legendaryLevels.length || level < 0)
			return null;
		return legendaryLevels[level];
	}
	
	public Level[] getAllLegendaryLevels() {
		return legendaryLevels;
	}
	
	public int getStars() {
		return stars;
	}
	
	public void setStars(int num) {
		stars = num;
		if(stars < 0)
			stars = 0;
	}
	
	public void addStars(int num) {
		stars += num;
	}
	
	public void subtractStars(int num) {
		stars -= num;
		if(stars < 0)
			stars = 0;
	}
	
	public void unlockAllLevels() {
		for(int i = 0; i < levels.length; i++) {
			levels[i].unlock();
			legendaryLevels[i].unlock();
		}
		
		saveCurrentProfile();
	}
	
	public void maxEverything() {
		for(int i = 0; i < upgrades.length; i++)
			unlockUpgrade(i);
		
		unlockAllLevels();
		
		for(int i = 0; i < levels.length; i++) {
			levels[i].maxOutStars();
			levels[i].setScore(99999);
			
			legendaryLevels[i].maxOutStars();
			legendaryLevels[i].setScore(99999);
		}
		
		setStars(99);
		
		saveCurrentProfile();
	}
	
	public void resetEverything() {
		File file = new File("data/profiles/"+name+".profile");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(file));
			writeNewData(writer);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		loadExistingProfile(file);
	}
	
	public void unlockUpgrade(int upgrade) {
		upgrades[upgrade] = true;
	}
	
	public void lockUpgrade(int upgrade) {
		upgrades[upgrade] = false;
	}
	
	public void unlockAllUpgrades() {
		for(int i = 0; i < upgrades.length; i++)
			upgrades[i] = true;
	}
	
	public void lockAllUpgrades() {
		for(int i = 0; i < upgrades.length; i++)
			upgrades[i] = false;
	}
	
	public boolean isUpgraded(int upgrade) {
		return upgrades[upgrade];
	}
	
	public boolean[] getAllUpgrades() {
		return upgrades;
	}
	
	public static void loadLastProfile() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("data/game_options.txt")));
			
			String line;
			String[] tokens;
			
			line = reader.readLine();
			tokens = line.split("=");
			currentUser = new Profile(tokens[1]);
		} catch(Exception e) {
			e.printStackTrace();
			currentUser = new Profile("default");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void saveLastProfile() {
		PrintWriter writer = null;
		File file = new File("data/temp.txt");
		try {
			writer = new PrintWriter(new FileWriter(file));
			writer.println("last_user="+currentUser.getName());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null)
				try {
					writer.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
		
		File rename = new File("data/game_options.txt");
		rename.delete();
		file.renameTo(rename);
	}
	
	public static void loadProfile(File file) {
		currentUser = new Profile(file.getName().substring(0, file.getName().length()-8));
	}
	
	public static void saveCurrentProfile() {
		PrintWriter writer = null;
		File file = new File("data/profiles/temp.profile");
		try {
			writer = new PrintWriter(new FileWriter(file));
			
			//write name
			writer.println("name="+currentUser.name);
			
			//write upgrades
			for(int i = 0; i < NUMBER_OF_UPGRADES; i++)
				writer.println(i+"="+currentUser.upgrades[i]);
			
			//write level info
			for(int i = 0; i < NUMBER_OF_LEVELS; i++)
				writer.println(
						currentUser.levels[i].getName()+
						"="+currentUser.levels[i].isUnlocked()+
						"="+currentUser.levels[i].getStars()+
						"="+currentUser.levels[i].getScore()+
						"="+currentUser.levels[i].getStarCondition(0)+
						"="+currentUser.levels[i].getStarCondition(1)+
						"="+currentUser.levels[i].getStarCondition(2)+
						"="+currentUser.levels[i].getMaxScore()+
						"="+currentUser.levels[i].getDevHighScore());
			
			//write legendary level info
			for(int i = 0; i < NUMBER_OF_LEVELS; i++)
				writer.println(
						currentUser.legendaryLevels[i].getName()+
						"="+currentUser.legendaryLevels[i].isUnlocked()+
						"="+currentUser.legendaryLevels[i].getStars()+
						"="+currentUser.legendaryLevels[i].getScore()+
						"="+currentUser.legendaryLevels[i].getStarCondition(0)+
						"="+currentUser.legendaryLevels[i].getStarCondition(1)+
						"="+currentUser.legendaryLevels[i].getStarCondition(2)+
						"="+currentUser.legendaryLevels[i].getMaxScore()+
						"="+currentUser.legendaryLevels[i].getDevHighScore());
			
			//write how many stars the user has
			writer.print("stars="+currentUser.getStars());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null)
				try {
					writer.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
		File rename = new File("data/profiles/"+currentUser.getName()+".profile");
		rename.delete();
		file.renameTo(rename);
		
		saveLastProfile();
	}
	
	private void createAndLoadNewProfile(String name) {
		createNewProfile(name);
		loadExistingProfile(new File("data/profiles/"+name+".profile"));
		
	}
	
	private void loadExistingProfile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			String tokens[];
			
			//read name
			line = reader.readLine();
			tokens = line.split("=");
			this.name = tokens[1];
			
			//check which upgrades are unlocked
			for(int i = 0; i < NUMBER_OF_UPGRADES; i++) {
				line = reader.readLine();
				tokens = line.split("=");
				upgrades[i] = Boolean.parseBoolean(tokens[1]);
			}
			
			//check status of each level
			for(int i = 0; i < NUMBER_OF_LEVELS; i++) {
				line = reader.readLine();
				tokens = line.split("=");
				levels[i] = new Level(
									tokens[0],
									false,
									Boolean.parseBoolean(tokens[1]),
									Integer.parseInt(tokens[2]),
									Integer.parseInt(tokens[3]),
									new boolean[] {
										Boolean.parseBoolean(tokens[4]),
										Boolean.parseBoolean(tokens[5]),
										Boolean.parseBoolean(tokens[6])
									},
									Integer.parseInt(tokens[7]),
									Integer.parseInt(tokens[8])
									);
			}
			
			//check status of each level on legendary difficulty
			for(int i = 0; i < NUMBER_OF_LEVELS; i++) {
				line = reader.readLine();
				tokens = line.split("=");
				legendaryLevels[i] = new Level(
											tokens[0],
											true,
											Boolean.parseBoolean(tokens[1]),
											Integer.parseInt(tokens[2]),
											Integer.parseInt(tokens[3]),
											new boolean[] {
												Boolean.parseBoolean(tokens[4]),
												Boolean.parseBoolean(tokens[5]),
												Boolean.parseBoolean(tokens[6])
											},
											Integer.parseInt(tokens[7]),
											Integer.parseInt(tokens[8])
											);
			}
			
			line = reader.readLine();
			tokens = line.split("=");
			stars = Integer.parseInt(tokens[1]);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	private void createNewProfile(String name) {
		PrintWriter writer = null;
		File file = new File("data/profiles/"+name+".profile");
		try {
			writer = new PrintWriter(new FileWriter(file));
			
			writeNewData(writer);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null)
				try {
					writer.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	private void writeNewData(PrintWriter writer) {
		//write name
		writer.println("name="+name);
		
		//write upgrades
		for(int i = 0; i < NUMBER_OF_UPGRADES; i++)
			writer.println(i+"=false");
		
		//write level info
		writer.println("States=true=0=0=false=false=false=1000=1588");
		writer.println("Websites=false=0=0=false=false=false=1500=1822");
		writer.println("Star Wars=false=0=0=false=false=false=3000=3644");
		writer.println("Lord of the Rings=false=0=0=false=false=false=2200=2840");
		writer.println("Mythology=false=0=0=false=false=false=4000=7645");
		writer.println("Comics=false=0=0=false=false=false=5000=7016");
		writer.println("Warhammer 40k=false=0=0=false=false=false=6000=8373");
		writer.println("Films=false=0=0=false=false=false=8000=10728");
		writer.println("Countries=false=0=0=false=false=false=7000=9584");
		writer.println("Video Games=false=0=0=false=false=false=25000=40038");
		
		//write legendary level info
		writer.println("States=true=0=0=false=false=false=2500=3727");
		writer.println("Websites=false=0=0=false=false=false=4500=7477");
		writer.println("Star Wars=false=0=0=false=false=false=6000=7363");
		writer.println("Lord of the Rings=false=0=0=false=false=false=10000=17856");
		writer.println("Mythology=false=0=0=false=false=false=1000=22365");
		writer.println("Comics=false=0=0=false=false=false=9000=12204");
		writer.println("Warhammer 40k=false=0=0=false=false=false=8500=11957");
		writer.println("Films=false=0=0=false=false=false=8500=10131");
		writer.println("Countries=false=0=0=false=false=false=9000=14306");
		writer.println("Video Games=false=0=0=false=false=false=25000=39103");
		
		//write how many stars the user has
		writer.print("stars=0");
	}
}
