package view;

import java.awt.Point;

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
import com.badlogic.gdx.scenes.scene2d.Event;

import controller.EGA;
import controller.GameStateManager;
import controller.SaveHandler;
import event.EventSupport;

@Data
public class PauseMenu extends GameState implements IMenu{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private BitmapFont subFont;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private final String title = "Pause";
	//private final String subTitle = "()";

	private int titleFontSize = 150;
	private int menuFontSize = 50;
	private int subTitleFontSize = 28;

	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;
	
	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	
	private boolean rendered;
	public GameState theGame;


	public PauseMenu(GameStateManager gsm, GameState currentGame) {
		super(gsm);
		theGame = currentGame;
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
		subFont = gen.generateFont(subTitleFontSize);

		menuItems = new String[]{
				"Resume",
				"Restart",
				"Settings",
				"Quit to main menu"
		};
		
		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		
		rendered = false;
	}


	private void loadTextures() {
		//backgroundTexture = new Texture("res/menu/emilsmamma.jpg");
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
		
		case MyInput.BUTTON_PAUSE:
			((Level)theGame).setIsPaused(false);
			EventSupport.getInstance().fireNewEvent("resumegame", theGame);
			break;
		}	
	}

	private void select(){
		if (currentItem == 0){
			// resume
			((Level)theGame).setIsPaused(false);
			gsm.getGame().setLevel(theGame);
			
		}
		if (currentItem == 1){
			// restart level
			gsm.getGame().setLevel(new Level(gsm, gsm.getCurrentTiledMap()));

		}
		if (currentItem == 2){
			gsm.getGame().setLevel(new SettingsMenu(gsm));
		}
		if(currentItem == 3){
			SaveHandler.save();
			gsm.getGame().setLevel(new MenuState(gsm));
			//Gdx.app.exit();
		}
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

		animateTitle(width);

		for(int i = 0; i < menuItems.length; i++){
			layout.setText(font, menuItems[i]);
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			
			int yPos = 450 - 70*i;
			int xPos = (int)(EGA.V_WIDTH - width) / 2;
			font.draw(
					sb,
					menuItems[i],
					xPos,
					yPos
					);
			menuItemPositions[i] = new Point(xPos,EGA.V_HEIGTH-yPos);
			menuItemEndPositions[i] = new Point(xPos+(int)width, EGA.V_HEIGTH-yPos+menuFontSize);
			if(firstTime){
				System.out.println(font.getXHeight());
				firstTime = false;
			}
		}

		sb.end();
		
		rendered = true;

	}


	private void animateTitle(Float width){	
		if(titleHeight > 650){
			titleHeight -= 2;
		} 
		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, titleHeight);
		//subFont.draw(sb, subTitle, (EGA.V_WIDTH-width) / 2, titleHeight-120);
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