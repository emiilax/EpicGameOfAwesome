package model;

import lombok.Data;
import controller.SaveHandler;

/**
 * 
 * @author Emil Axelsson
 * 
 * Model class for the level
 */
@Data
public class LevelModel {
	
	/** boolean whether the level should be shown in debug-mode or not */
	private boolean debug = SaveHandler.getGameData().getIsDebug();
	
	/**
	 * Updates the debug variable
	 */
	public void update(){
		debug = SaveHandler.getGameData().getIsDebug();
	}
}
