package controller.menus;

import java.awt.Point;

import view.IMenu;
import view.MenuRender;
import view.menus.LevelSelect;
import lombok.Data;
import model.MenuModel;
import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import controller.EGA;
import controller.GameState;
import controller.GameStateManager;
import controller.Level;
import controller.SaveHandler;
import controller.Variables;

@Data
public class MenuState extends GameState implements IMenu{

	private static Texture backgroundTexture;

	private final String title = "EGA";
	private final String subTitle = "Epic Game Of Awesome";

	private int titleFontSize = Variables.mainMenuTitleSize;
	private int menuFontSize = Variables.mainMenuItemSize;
	private int subTitleFontSize = 28;

	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	private int titleHeight = 900;
	private int xPos = (int)(EGA.V_WIDTH - 365)/2;
	private int yPos = 450;
	private int gap = 70;

	private boolean rendered;

	private MenuRender view;
	private MenuModel model;

	public MenuState(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

	private void init(){

		menuItems = new String[]{
				"Play",
				"Level Select",
				"Settings",
				"Quit"
		};

		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		model = new MenuModel();
		initModel();
		
		view = new MenuRender(model);

		rendered = false;

	}

	private void initModel(){
		model.setMenuItemEndPositions(menuItemEndPositions);
		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItems(menuItems);
		model.setTitleFontSize(titleFontSize);
		model.setMenuFontSize(menuFontSize);
		model.setSubTitleFontSize(subTitleFontSize);
		model.setTitle(title);
		model.setSubTitle(subTitle);
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
		}
	}

	private void select(){
		if (currentItem == 0){
			gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		}
		if (currentItem == 1){
			gsm.pushState(new LevelSelect(gsm));
		}
		if (currentItem == 2){
			gsm.pushState(new SettingsMenu(gsm));
		}
		if(currentItem == 3){
			SaveHandler.save();
			Gdx.app.exit();
		}
	}

	public void select(int x, int y){
		menuItemPositions = model.getMenuItemPositions();
		menuItemEndPositions = model.getMenuItemEndPositions();
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
		view.render(currentItem, cam, true);
		rendered = true;
	}

	@Override
	public void dispose() {

	}

	public void setCurrentItem(int x, int y){
		menuItemPositions = model.getMenuItemPositions();
		menuItemEndPositions = model.getMenuItemEndPositions();
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