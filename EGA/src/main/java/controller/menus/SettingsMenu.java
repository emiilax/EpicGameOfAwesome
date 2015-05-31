package controller.menus;

import io.SaveHandler;

import java.awt.Point;

import view.renders.MenuRender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.MenuModel;
import model.MyInput;
import model.Variables;
import controller.EGA;
import event.EventSupport;
/**
 * Settings menu class. Extends the Menu class.
 * @author Hampus R�nstr�m
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SettingsMenu extends Menu { 
	private String debugStatus;

	private String volume;
	private float fVol;

	
	public SettingsMenu() {
		super();
		init();
	}
	
	/**
	 * Initiates all variables
	 */
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
	
	/**
	 * Sets the string visible in the menu for how high the volume is. 
	 */
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
	
	/**
	 * Increases the volume of the game. 
	 */
	private void incrementVolume(){
		if(fVol < 1.0f){
			fVol += 0.20f;
			SaveHandler.getGameData().setVolume(fVol);
			setVolume();
			updateVolumeStatus();
			SaveHandler.save();
		}	
	}

	/**
	 * Decreases the volume of the game
	 */
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
			EventSupport.getInstance().fireNewEvent("changecontroller");
			//gsm.pushState(new ChangeControlMenu(gsm));
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
	
	/**
	 * Resets everything stored in the gamedata.
	 */
	public void resetAll(){
		SaveHandler.init();
		init();
	}
	@Override
	public void update(float dt) {
		//handleInput();
	}

	private void backMenu(){
		//gsm.popState();
		EventSupport.getInstance().fireNewEvent("pop");
	}
	
	/**
	 * Sets the debugstatus to on or off, depending on the current status.
	 */
	private void setDebugStatus(){
		if(SaveHandler.getGameData().getIsDebug()){
			debugStatus = "On";
		}else{
			debugStatus = "Off";
		}
		
		menuItems[2] = "Dev mode: " + debugStatus;
	}
	
	/**
	 * Updates the volume status depending on the current volume. 
	 */
	private void updateVolumeStatus(){
		menuItems[3] = "Volume: " + volume;
	}
}
