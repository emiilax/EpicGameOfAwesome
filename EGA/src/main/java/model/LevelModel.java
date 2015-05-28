package model;

import lombok.Data;


/**
 * 
 * @author Emil Axelsson
 * 
 * Model class for the level
 */
@Data
public class LevelModel {
	
	/** boolean whether the level should be shown in debug-mode or not */
	private boolean debug;
	
	/**
	 * Updates the debug variable
	 */
	/*public void update(){
		debug = SaveHandler.getGameData().getIsDebug();
	}*/
}
