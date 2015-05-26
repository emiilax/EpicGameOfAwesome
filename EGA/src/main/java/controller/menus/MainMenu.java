package controller.menus;

import java.awt.Point;

import view.MenuRender;
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
public class MainMenu extends Menu{

	private static Texture backgroundTexture;
	private GameStateManager gsm;
	
	private String subTitle;
	private int subTitleFontSize;

	public MainMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
	}

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
		//initModel();
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

	@Override
	public void update(float dt) {
		//handleInput();
	}


	@Override
	public void render() {
		view.render(currentItem, cam, true);
		rendered = true;
	}
}