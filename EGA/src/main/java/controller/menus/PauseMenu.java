package controller.menus;

import java.awt.Point;

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
import event.EventSupport;

@Data
public class PauseMenu extends Menu{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private GameStateManager gsm;
	private GameState theGame;

	public PauseMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

	private void init(){
		sb = new SpriteBatch();
		title = "Pause";		
		titleFontSize = Variables.subMenuTitleSize;
		menuFontSize = Variables.subMenuItemSize;
		titleHeight = 650;
		gap = 70;
		xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
		yPos = 450;

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

	@Override
	public void select(){
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
		}
		if(currentItem == 3){
			SaveHandler.save();
			gsm.setState(new MainMenu(gsm));
		}
	}
	
	public void unpauseTheGame(){
		gsm.popState();
	}
	@Override
	public void update(float dt) {
		//handleInput();
	}
}
