package controller.menus;

import java.awt.Point;

import com.badlogic.gdx.Gdx;

import view.renders.MenuRender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.MenuModel;
import model.MyInput;
import model.Variables;
import controller.EGA;
import controller.savehandler.SaveHandler;
import event.EventSupport;

@Data
@EqualsAndHashCode(callSuper=false)
public class MainMenu extends Menu{

	//private GameStateManager gsm;
	
	private String subTitle;
	private int subTitleFontSize;
	
	/*
	public MainMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}*/
	
	public MainMenu(){
		super();
		init();
	}
	
	/**
	 * Initiates all variables
	 */
	private void init(){
		title = "EGA";
		subTitle = "Epic Game Of Awesome";

		titleFontSize = Variables.mainMenuTitleSize;
		menuFontSize = Variables.mainMenuItemSize;
		subTitleFontSize = 28;
		titleHeight = 900;
		xPos = (int)(EGA.V_WIDTH - 365)/2;
		yPos = 450;
		gap = 70;

		menuItems = new String[]{
				"Play",
				"Level Select",
				"Settings",
				"Quit"
		};

		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);

		rendered = false;

	}
	
	@Override
	protected void updateModel(){
		super.updateModel();
		model.setSubTitleFontSize(subTitleFontSize);
		model.setSubTitle(subTitle);	
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
		}
	}

	@Override
	public void select(){
		if (currentItem == 0){
			EventSupport.getInstance().fireNewEvent("level",0);
			
			//gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		}
		if (currentItem == 1){
			EventSupport.getInstance().fireNewEvent("levelselect");
			//gsm.pushState(new LevelSelect(gsm));
		}
		if (currentItem == 2){
			EventSupport.getInstance().fireNewEvent("settings");
			//gsm.pushState(new SettingsMenu(gsm));
		}
		if(currentItem == 3){
			SaveHandler.save();
			Gdx.app.exit();
		}
	}

	@Override
	public void update(float dt) {
		//handleInput();
	}


	@Override
	public void render() {
		view.render(currentItem, getCam(), true);
		rendered = true;
	}
}