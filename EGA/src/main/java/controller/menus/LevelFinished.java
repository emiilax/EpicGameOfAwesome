package controller.menus;

import java.awt.Point;

import view.renders.MenuRender;
import model.EGATimer;
import model.GameData;
import model.MenuModel;
import model.MyInput;
import model.Variables;
import controller.EGA;
import controller.savehandler.SaveHandler;
import event.EventSupport;

/**
 * A menu that is shown at the end of each completed level. It shows the time passed and let's you
 * replay the level, go back or move on to the next level.
 * @author Erik
 *
 */
public class LevelFinished extends Menu{

	private int level;
	//private GameStateManager gsm;
	private EGATimer timer;
	
	/*public LevelFinished(GameStateManager gsm, int level){
		super(gsm);
		this.gsm = gsm;
		this.level = level;
		init();
		
	}*/
	
	public LevelFinished(int level){
		super();
		this.level = level;
		init();
		
	}
	
	private void init(){
		titleFontSize = Variables.subMenuTitleSize - 20;
		menuFontSize = Variables.subMenuItemSize;
		titleHeight = 650;
		gap = 70;
		xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
		yPos = 450;

		menuItems = new String[]{
				"Next Level",
				"Replay",
				"Main Menu",
		};
		
		setTimeString();
		
		SaveHandler.save();
		
		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);
		
		rendered = false;
	}
	
	/**
	 * Sets the string which is printed out at the top.
	 * Checks if the time is a record or not.
	 */
	private void setTimeString(){
		timer = EGATimer.getTimer();
		Float timePassed = timer.getTimePassed();
		GameData gd = SaveHandler.getGameData();
		
		if(gd.isBetterTime(level, timePassed)){
			title = "New Record!" + "\n" + "Your time was: " +  Float.toString(timePassed);
		} else {
			title = "Your time: " + Float.toString(timePassed) 
					+ "\n" + "Your best time: " + Float.toString(gd.getTime(level));
		}
		gd.addTime(level, timePassed);
		SaveHandler.setGameData(gd);
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
		if(currentItem == 0){
			EventSupport.getInstance().fireNewEvent("nextlevel");
			//gsm.setState(new Level(gsm, gsm.getNextTiledMap()));
		}
		if(currentItem == 1){
			EventSupport.getInstance().fireNewEvent("level", 0);
			//gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		} else if(currentItem == 2){
			EventSupport.getInstance().fireNewEvent("main");
			//gsm.setState(new MainMenu(gsm));
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
}
