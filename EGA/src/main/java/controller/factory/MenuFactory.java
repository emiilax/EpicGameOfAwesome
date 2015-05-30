package controller.factory;

import controller.menus.ChangeControlMenu;
import controller.menus.LevelFinished;
import controller.menus.LevelSelect;
import controller.menus.MainMenu;
import controller.menus.Menu;
import controller.menus.PauseMenu;
import controller.menus.SettingsMenu;



public class MenuFactory {
	
	public Menu getMenu(String name){
		
		if(name.equalsIgnoreCase("main")){
			return new MainMenu();
		}
		
		if(name.equalsIgnoreCase("pause")){
			return new PauseMenu();
		}
		
		if(name.equalsIgnoreCase("changecontroller")){
			return new ChangeControlMenu();
		}
		
		if(name.equalsIgnoreCase("levelselect")){
			return new LevelSelect();
		}
		
		if(name.equalsIgnoreCase("settings")){
			return new SettingsMenu();
		}
		
		return null;
		
	}
	
	public Menu getLevelFinishedMenu(int i){
		return new LevelFinished(i);
	}
}
