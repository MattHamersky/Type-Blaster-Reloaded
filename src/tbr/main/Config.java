package tbr.main;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import tbr.game.Profile;

public class Config {

	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	public static int G_WIDTH = 1920;
	public static int G_HEIGHT = 1080;
	public static final int MIN_LOGIC_UPDATES = 5;
	public static boolean IS_FULLSCREEN = true;
	public static boolean VSYNC = false;
	public static boolean SHOW_FPS = false;
	public static boolean FIRST_LAUNCH = true;
	public static String USER = "default";
	
	public static Profile user;
	
	public static void loadConfig() {
		checkForValidPath();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("data/config.ini")));
			
			String line;
			String[] tokens;
			
			line = reader.readLine();
			tokens = line.split("=");
			G_WIDTH = Integer.parseInt(tokens[1]);
			
			line = reader.readLine();
			tokens = line.split("=");
			G_HEIGHT = Integer.parseInt(tokens[1]);
			
			line = reader.readLine();
			tokens = line.split("=");
			IS_FULLSCREEN = Boolean.parseBoolean(tokens[1]);
			
			line = reader.readLine();
			tokens = line.split("=");
			VSYNC = Boolean.parseBoolean(tokens[1]);
			
			line = reader.readLine();
			tokens = line.split("=");
			SHOW_FPS = Boolean.parseBoolean(tokens[1]);
			
			line = reader.readLine();
			tokens = line.split("=");
			FIRST_LAUNCH = Boolean.parseBoolean(tokens[1]);
			
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
	
	private static void checkForValidPath() {
		File data = new File("data");
		if(!data.exists())
			data.mkdir();
	}
	
	public static void saveConfig() {
		PrintWriter writer = null;
		File temp = new File("data/temp.ini");
		try {
			writer = new PrintWriter(new FileWriter(temp));
			
			writer.println("width="+G_WIDTH);
			
			writer.println("height="+G_HEIGHT);
			
			writer.println("fullscreen="+IS_FULLSCREEN);
			
			writer.println("vsync="+VSYNC);
			
			writer.println("show_fps="+SHOW_FPS);
			
			writer.println("first_launch="+FIRST_LAUNCH);
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
		File renameTo = new File("data/config.ini");
		renameTo.delete();
		temp.renameTo(renameTo);
	}
	
}
