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

	private GameState curGame;
	
	private String volume;

	private int vol;
	
	private float fVol;

	public SettingsMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
		loadTextures();
		this.curGame = null;
	}

	public SettingsMenu(GameStateManager gsm, GameState curGame){
		this(gsm);
		this.curGame = curGame;
	}

	private void init(){
		sb = new SpriteBatch();


		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(menuFontSize);

		vol = 3;
		volume = "III";
		fVol = SaveHandler.getGameData().getSoundVolume();
		System.out.println("kom igen" + fVol);

		menuItems = new String[]{
				"Controls",
				"Volume    :",
				"Control",
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

	private void incrementVolume(){
		if(vol < 6){
			vol++;
			chooseVolume();
			fVol += 0.20f;
		}	
	}

	private void decrementVolume(){
		if(vol >= 0){
			vol--;
			chooseVolume();
			fVol -= 0.20f;
		}
	}

	private void chooseVolume(){
		switch(vol){
		case 0: volume = "";
		break;
		case 1: volume ="I";
		break;
		case 2: volume ="II";
		break;
		case 3: volume ="III";
		break;
		case 4: volume ="IIII";
		break;
		case 5: volume ="IIIII";
		break;
		}
		updateVolumeStatus();
		System.out.println("huh?" + fVol);
		SaveHandler.getGameData().setVolume(fVol);
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
			gsm.getGame().setLevel(new ChangeControlMenu(gsm, this));
		}
		if(currentItem == 1){
			changeVolume();
		}
		if (currentItem == 2){
			resetAll();
		}
		if (currentItem == 3){
			if(curGame instanceof PauseMenu){
				Level curLevel = (Level)((PauseMenu) curGame).getTheGame();
				curLevel.toggleDebug();
			} else {
				gsm.getGame().toggleDebug();
			}

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
		if(curGame != null){
			gsm.getGame().setLevel(curGame);
		}else{
			gsm.getGame().setLevel(new MenuState(gsm));
		}
	}

	private void setDebugStatus(){
		debugStatus = gsm.getGame().getDebugStatus();
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
