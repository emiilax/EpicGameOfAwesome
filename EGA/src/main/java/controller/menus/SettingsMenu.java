package controller.menus;

import java.awt.Point;

import view.IMenu;
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
public class SettingsMenu extends GameState implements IMenu {

	private final String title = "Settings";

	private int titleFontSize = Variables.subMenuTitleSize;
	private int menuFontSize = Variables.subMenuItemSize;
	private int titleHeight = 650;
	private int gap = 70;
	private int xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
	private int yPos = 450;
	
	private int currentItem;
	private String menuItems[];
	private String debugStatus;

	private GameStateManager gsm;

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	private boolean rendered;
	
	private String volume;
	private float fVol;
	
	private MenuModel model;
	private MenuRender view;


	public SettingsMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

	private void init(){

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

	private void select(){
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

	public void select(int x, int y){
		if(rendered && x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			select();
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
	
	@Override
	public void render() {
		updateModel();
		view.render(currentItem, cam, false);
		rendered = true;
	}
	
	private void updateModel(){
		model.setMenuItemEndPositions(menuItemEndPositions);
		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItems(menuItems);
		model.setTitleFontSize(titleFontSize);
		model.setMenuFontSize(menuFontSize);
		model.setTitle(title);
		model.setTitleHeight(titleHeight);
		model.setGap(gap);
		model.setXPos(xPos);
		model.setYPos(yPos);
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Point[] getMenuItemPositions(){
		return menuItemPositions;
	}

	public Point[] getMenuItemEndPositions(){
		return menuItemEndPositions;
	}

	public void setCurrentItem(int x, int y){
		if(rendered){
			for(int i = 0; i < menuItemPositions.length; i++){
				if(x > menuItemPositions[i].getX() && y > menuItemPositions[i].getY()
						&& x < menuItemEndPositions[i].getX() &&
						y < menuItemEndPositions[i].getY()){
					currentItem = i;
				}
			}	
		}
	}
}
