package view;

import java.awt.Point;

import view.menus.PauseMenu;
import lombok.Data;
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
import controller.GameStateManager;
import controller.SaveHandler;
import controller.Variables;
import controller.menus.ChangeControlMenu;

@Data
public class SettingsMenu extends GameState implements IMenu {
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private final String title = "Settings";

	private int titleFontSize = Variables.subMenuTitleSize;
	private int menuFontSize = Variables.subMenuItemSize;

	private int currentItem;
	private String menuItems[];
	private String debugStatus;

	private GameStateManager gsm;

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	private boolean rendered;
	
	private String volume;

	private int vol;
	
	private float fVol;

	public SettingsMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
		loadTextures();
		
	}

	private void init(){
		sb = new SpriteBatch();


		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(menuFontSize);

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

	private void loadTextures() {
		backgroundTexture = new Texture("res/menu/skybackground_menu.jpg");
		backgroundSprite =new Sprite(backgroundTexture);
	}

	public void renderBackground() {
		backgroundSprite.draw(sb);
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
	
	public void changeVolume(){
		
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

	int titleHeight = 900; 
	boolean firstTime = true;
	@Override
	public void render() {

		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		sb.setProjectionMatrix(cam.combined);

		sb.begin();

		renderBackground();

		layout.setText(titleFont, title);
		float width = layout.width;

		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 650);

		for(int i = 0; i < menuItems.length; i++){
			layout.setText(font, menuItems[i]);
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}

			int yPos = 450 - 70*i;
			int xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
			font.draw(
					sb,
					menuItems[i],
					xPos,
					yPos
					);
			menuItemPositions[i] = new Point(xPos,EGA.V_HEIGTH-yPos);
			menuItemEndPositions[i] = new Point(xPos+(int)width, EGA.V_HEIGTH-yPos+menuFontSize);
			if(firstTime){
				firstTime = false;
			}
		}

		sb.end();

		rendered = true;

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
