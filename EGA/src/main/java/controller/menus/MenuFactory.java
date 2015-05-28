package controller.menus;


import controller.GameStateManager;

public class MenuFactory {
	
	public Menu getMenu(String name, GameStateManager gsm){
		
		if(name.equalsIgnoreCase("main")){
			return new MainMenu(gsm);
		}
		
		if(name.equalsIgnoreCase("pause")){
			return new PauseMenu(gsm);
		}
		return null;
		
	}
}
