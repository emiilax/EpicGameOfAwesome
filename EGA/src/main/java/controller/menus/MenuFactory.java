package controller.menus;



/**
 * A menu factory that returns a menu
 * 
 * @author Emil Axelsson
 *
 */
public class MenuFactory {
	
	/**
	 * Returns a menu depending on the name
	 * 
	 * @param name, the name of the menu
	 * 
	 * @return the menu
	 */
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
	
	/**
	 * Called when a level is completed, shows the levelfinished menu
	 * 
	 * @param i, the level that have been finished
	 * 
	 * @return, levelfinished menu
	 */
	public Menu getLevelFinishedMenu(int i){
		return new LevelFinished(i);
	}
}
