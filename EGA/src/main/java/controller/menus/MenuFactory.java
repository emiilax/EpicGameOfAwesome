package controller.menus;




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
