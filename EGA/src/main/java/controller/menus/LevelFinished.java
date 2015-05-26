package controller.menus;

import java.awt.Point;

import view.IMenu;
import view.MenuRender;
import model.EGATimer;
import model.GameData;
import model.MenuModel;
import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import controller.EGA;
import controller.GameState;
import controller.GameStateManager;
import controller.Level;
import controller.SaveHandler;
import controller.Variables;

public class LevelFinished extends GameState implements IMenu{

	private String title;
	

	private int titleFontSize = Variables.subMenuTitleSize - 20;
	private int menuFontSize = Variables.subMenuItemSize;
	private int level;
	private int titleHeight = 650;
	private int gap = 70;
	private int xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
	private int yPos = 450;
	
	private int currentItem;
	private String menuItems[];
	
	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	
	private boolean rendered = false;

	private GameStateManager gsm;
	private EGATimer timer;
	
	private MenuModel model;
	private MenuRender view;
	
	public LevelFinished(GameStateManager gsm, Texture backgroundTexture, int level){
		super(gsm);
		this.gsm = gsm;
		this.level = level;
		init();
		
	}
	
	private void init(){

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
	
	private void select(){
		if(currentItem == 0){
			
			gsm.setState(new Level(gsm, gsm.getNextTiledMap()));
		}
		if(currentItem == 1){
		
			gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		} else if(currentItem == 2){
		
			gsm.setState(new MenuState(gsm));
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void select(int x, int y) {
		if(rendered && x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			select();
		}
	}

	public Point[] getMenuItemPositions() {
		return menuItemPositions;
	}

	public Point[] getMenuItemEndPositions() {
		return menuItemEndPositions;
	}

	public void setCurrentItem(int x, int y) {
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
