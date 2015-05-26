package view.menus;

import java.awt.Point;

import view.IMenu;
import view.MenuRender;
import lombok.Data;
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
import com.badlogic.gdx.scenes.scene2d.Event;

import controller.EGA;
import controller.GameState;
import controller.GameStateManager;
import controller.Level;
import controller.SaveHandler;
import controller.Variables;
import controller.menus.MenuState;
import controller.menus.SettingsMenu;
import event.EventSupport;

@Data
public class PauseMenu extends GameState implements IMenu{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private final String title = "Pause";
	//private final String subTitle = "()";

	private int titleFontSize = Variables.subMenuTitleSize;
	private int menuFontSize = Variables.subMenuItemSize;
	private int titleHeight = 650;
	private int gap = 70;
	private int xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
	private int yPos = 450;

	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;
	
	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	
	private boolean rendered;
	private GameState theGame;
	
	private MenuModel model;
	private MenuRender view;


	public PauseMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

	private void init(){
		sb = new SpriteBatch();

		menuItems = new String[]{
				"Resume",
				"Restart",
				"Settings",
				"Quit to main menu"
		};
		
		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);
		
		rendered = false;
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
		
		case MyInput.BUTTON_PAUSE:
			unpauseTheGame();
			break;
		}	
	}

	private void select(){
		if (currentItem == 0){
			// resume
			unpauseTheGame();
			
		}
		if (currentItem == 1){
			// restart level
			gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));

		}
		if (currentItem == 2){
			gsm.pushState(new SettingsMenu(gsm));
			//gsm.getGame().setLevel(new SettingsMenu(gsm, this));
		}
		if(currentItem == 3){
			SaveHandler.save();
			gsm.setState(new MenuState(gsm));
			//Gdx.app.exit();
		}
	}
	
	public void unpauseTheGame(){
		gsm.popState();
		//EventSupport.getInstance().fireNewEvent("resumegame", theGame);
	}
	
	public void select(int x, int y){
		if(x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			select();
		}
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
