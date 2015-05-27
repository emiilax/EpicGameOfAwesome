package model;

import lombok.Data;
import controller.SaveHandler;

@Data
public class LevelModel {
	
	private boolean debug = SaveHandler.getGameData().getIsDebug();
	
	public void update(){
		debug = SaveHandler.getGameData().getIsDebug();
	}
}
