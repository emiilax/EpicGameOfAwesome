package controller.savehandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.Data;
import model.GameData;

import com.badlogic.gdx.Gdx;

/**
 * Handles different save options. All methods are static and this class never needs to be initialized.
 * Contains an instance of GameData which is written onto a .sav file on the computer when the game is saved. 
 * @author Erik
 *
 */
@Data
public class SaveHandler {
	
	private static GameData gd;
	
	/**
	 * Creates a .sav file in the source folder containing a copy of the GameData instance. 
	 */
	public static void save() {
		try{
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("highScores.sav")
					);
			out.writeObject(gd);
			out.close();
		}
		catch(IOException e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	/**
	 * Loads the .sav file containing the GameData instance from the source folder on the computer.
	 * If no .sav file exists, a new GameData file is created and saved.
	 */
	public static void load(){
		try{
			if(!saveFileExists()){
				init();
				return;
			}
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream("highScores.sav")
					);
			gd = (GameData)input.readObject();
			input.close();
		} 
		catch(IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	/**
	 * Checks if a .sav file exists in the source folder
	 * @return True if a save file exists, false otherwise
	 */
	public static boolean saveFileExists(){
		File f = new File ("highScores.sav");
		return f.exists();
	}
	
	/**
	 * Creates a new instance of GameData. Calling this method midgame will 
	 * reset all settings saved in GameData. 
	 */
	public static void init(){
		gd = new GameData();
		save();
	}
	
	/**
	 * @return GameData instance containing everything saved.
	 */
	public static GameData getGameData(){
		return gd;
	}
	
	/**
	 * Sets the gamedata to a new one
	 * @param gameData The new GameData instance
	 */
	public static void setGameData(GameData gameData){
		gd = gameData;
	}
	
}
