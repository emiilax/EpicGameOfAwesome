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

import controller.EGA;
import controller.GameStateManager;
import controller.SaveHandler;

@Data
public class SettingsMenu extends GameState implements IMenu {
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private final String title = "Settings";
	
	private int titleFontSize = 150;
	private int menuFontSize = 50;

	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;
	
	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	
	private boolean rendered;
	
	private GameState curGame;

	public SettingsMenu(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
		loadTextures();
	}
	
	public SettingsMenu(GameStateManager gsm, GameState curGame){
		super(gsm);
		this.gsm = gsm;
		this.curGame = curGame;
		
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

		menuItems = new String[]{
				"CONTROLS! bitch",
				"Reseta alla saker",
				"Back!"
		};
		
		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		rendered = false;
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
			gsm.getGame().setLevel(new MenuState(gsm));
			break;
		}
	}

	private void select(){
		if (currentItem == 0){
			gsm.getGame().setLevel(new ChangeControlMenu(gsm));
		}
		if (currentItem == 1){
			resetControls();
		}
		if (currentItem == 2){
			if(curGame != null){
			
				gsm.getGame().setLevel(curGame);
			}else{
				gsm.getGame().setLevel(new MenuState(gsm));
			}
		}
	}
	
	public void select(int x, int y){
		if(rendered && x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			select();
		}
	}

	public void resetControls(){
		
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
				
				firstTime = false;
			}
		}

		sb.end();
		
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
