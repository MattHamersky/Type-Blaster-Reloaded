package tbr.game;

public class Level {

	public static final int MAX_STARS = 3;
	
	private String name;
	private boolean isLegendary;
	private boolean isUnlocked;
	private int stars;
	private int score;
	private int maxScore;
	private int devHighScore;
	
	private boolean[] starConditions;
	public static final int CONDITION_FINISH = 0;
	public static final int CONDITION_NO_DAMAGE = 1;
	public static final int CONDITION_POINTS_REACHED = 2;
	
	public Level(String name, boolean isLegendary, boolean isUnlocked, int stars, int score, boolean[] starConditions, int maxScore, int devHighScore) {
		this.name = name;
		this.isLegendary = isLegendary;
		this.isUnlocked = isUnlocked;
		this.stars = stars;
		this.score = score;
		this.starConditions = starConditions;
		this.maxScore = maxScore;
		this.devHighScore = devHighScore;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isLegendary() {
		return isLegendary;
	}
	
	public boolean isUnlocked() {
		return isUnlocked;
	}
	
	public void unlock() {
		isUnlocked = true;
	}
	
	public void lock() {
		isUnlocked = false;
	}
	
	public int getStars() {
		return stars;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getMaxScore() {
		return maxScore;
	}
	
	public int getDevHighScore() {
		return devHighScore;
	}
	
	public boolean[] getStarConditions() {
		return starConditions;
	}
	
	public boolean getStarCondition(int condition) {
		if(condition < 0)
			return starConditions[0];
		else if(condition >= starConditions.length)
			return starConditions[starConditions.length-1];
		else
			return starConditions[condition];
	}
	
	public void setStarCondition(int condition, boolean status) {
		if(condition < 0)
			condition = 0;
		else if(condition >= starConditions.length)
			condition = starConditions.length-1;
		
		//add a star to the level total if the condition was previously false (first time unlocking this)
		if(!starConditions[condition])
			addStars(1);
		
		starConditions[condition] = status;
	}
	
	public void maxOutStars() {
		for(int i = 0; i < starConditions.length; i++)
			starConditions[i] = true;
		setStars(MAX_STARS);
	}
	
	public void addStars(int stars) {
		this.stars += stars;
		if(this.stars > MAX_STARS)
			this.stars = MAX_STARS;
		Profile.currentUser.addStars(stars);
	}
	
	public void setStars(int stars) {
		if(stars > MAX_STARS)
			this.stars = MAX_STARS;
		else if(stars < 0)
			this.stars = 0;
		else
			this.stars = stars;
	}
	
	public void resetLevel() {
		isUnlocked = false;
		stars = 0;
	}
	
}
