package controller.menus;

import java.awt.Point;

import view.MenuRender;
import lombok.Data;
import model.MenuModel;
import model.MyInput;
import controller.EGA;
import controller.GameState;
import controller.GameStateManager;
import controller.SaveHandler;
import controller.Variables;

@Data
public class SettingsMenu extends Menu { 
	private String debugStatus;

	private GameStateManager gsm;	
	private String volume;
	private float fVol;

	public SettingsMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

	private void init(){
		title = "Settings";
		titleFontSize = Variables.subMenuTitleSize;
		menuFontSize = Variables.subMenuItemSize;
		titleHeight = 650;
		gap = 70;
		xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
		yPos = 450;

		setVolume();

		menuItems = new String[]{
				"Controls",
				"Reset all",
				"Dev mode: " + debugStatus,
				"Volume: " + volume,
				"Back"
		};


		setDebugStatus();

		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];

		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);
		
		rendered = false;
	}
	
	private void setVolume(){
		fVol = SaveHandler.getGameData().getSoundVolume();
		if(fVol < 0.2f){
			volume = "mute";
		}else if(fVol < 0.4f){
			volume = "I";
		}else if(fVol < 0.6f){
			volume = "II";
		}else if(fVol < 0.8f){
			volume = "III";
		}else if(fVol < 1.0f){
			volume = "IIII";
		} else if(fVol == 1.0f){
			volume = "IIIII";
		}
	}

	private void incrementVolume(){
		if(fVol < 1.0f){
			fVol += 0.20f;
			SaveHandler.getGameData().setVolume(fVol);
			setVolume();
			updateVolumeStatus();
			SaveHandler.save();
		}	
	}

	private void decrementVolume(){
		if(fVol > 0.0f){
			fVol -= 0.20f;
			SaveHandler.getGameData().setVolume(fVol);
			setVolume();
			updateVolumeStatus();
			SaveHandler.save();
		}
	}

	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentItem > 0){
				currentItem --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentItem < menuItems.length-1){
				currentItem++;
			}
			break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		case MyInput.BUTTON_ESCAPE:
			backMenu();
			break;
		case MyInput.BUTTON_FORWARD:
			if(currentItem == 3){
				incrementVolume();
			}
			break;
		case MyInput.BUTTON_BACKWARD:
			if(currentItem == 3){
				decrementVolume();
			}
			break;
		}
	}

	@Override
	public void select(){
		if (currentItem == 0){
			gsm.pushState(new ChangeControlMenu(gsm));
		}
		if(currentItem == 1){
			resetAll();
		}
		if (currentItem == 2){
			SaveHandler.getGameData().toggleDebug();

			setDebugStatus();
		}
		if (currentItem == 4){
			backMenu();			
		}
	}

	public void resetAll(){
		SaveHandler.init();
		init();
	}
	@Override
	public void update(float dt) {
		//handleInput();
	}

	private void backMenu(){
		gsm.popState();
	}

	private void setDebugStatus(){
		if(SaveHandler.getGameData().getIsDebug()){
			debugStatus = "On";
		}else{
			debugStatus = "Off";
		}
		
		menuItems[2] = "Dev mode: " + debugStatus;
	}

	private void updateVolumeStatus(){
		menuItems[3] = "Volume: " + volume;
	}
}
