package view.menus;

import java.awt.Point;

import view.IMenu;
import model.EGATimer;
import model.GameData;
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
import controller.menus.MenuState;

/**
 * 
 * @author Rebecka Reitmaier
 * This GameState is used to choose the level for the game.
 * 
 * @param gsm, GameStateManeger
 * @param backGroundTexture, Texture  
 */

public class LevelSelect extends GameState implements IMenu {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private String title;

	private int titleFontSize = Variables.subMenuTitleSize;
	private int menuFontSize = Variables.subMenuItemSize;

	private boolean rendered = false;


	private int currentRow = 0;
	private int currentCol = 0;

	private Point [][] menuItemPositions;
	private Point [][] menuItemEndPositions;

	private String menuItems [][];

	private GameStateManager gsm;
	private Texture background;

	public LevelSelect(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		background = new Texture("res/menu/skybackground_menu.jpg");
		this.backgroundTexture = background;
		init();
	}

	private void init(){
		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(menuFontSize);

		menuItems = new String[][]{
				{"Level 1", "Level 2", "Level 3"}, //row 0 
				{"Level 4", "Level 5", "Level 6"}, //row 1
				{"<--", "Back", "-->"}, 

		};

		setTitle();

		SaveHandler.save();

		menuItemPositions = new Point[menuItems.length][menuItems[0].length];
		menuItemEndPositions = new Point[menuItems.length][menuItems[0].length];

		rendered = false;
	}

	private void setTitle(){
		title = "Choose level to play";
	}

	private void select(){
		String element = menuItems[currentRow][currentCol];
		if(element == "Level 1"){
			gsm.setState(new Level(gsm, gsm.getLevel(1)));
			gsm.setCurrentLevel(1);
		}
		if(element == "Level 2"){
			gsm.setState(new Level(gsm, gsm.getLevel(2)));
			gsm.setCurrentLevel(2);
		}
		if(element == "Level 3"){
			gsm.setState(new Level(gsm, gsm.getLevel(3)));
			gsm.setCurrentLevel(3);
		}
		if(element == "Level 4"){
			gsm.setState(new Level(gsm, gsm.getLevel(4)));
			gsm.setCurrentLevel(4);
		}
		if(element == "Level 5"){
			gsm.setState(new Level(gsm, gsm.getLevel(5)));
			gsm.setCurrentLevel(5);
		}
		if(element == "Level 6"){
			gsm.setState(new Level(gsm, gsm.getLevel(6)));
			gsm.setCurrentLevel(6);
		}
		if(element == "<--" || element == "-->"){
			// switch String array 
			System.out.println("this is -->");
		}
		if(element == "Back"){
			//gsm.getGame().setLevel(new MenuState(gsm));
			//switch to main menu
			gsm.popState();
		}
		/*
		 * add more if-states if you add more levels
		 */
		
	}

	
	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentRow == 0){
				currentRow = 2;
			}
			else if(currentRow > 0){
				currentRow --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentRow == 2){
				currentRow = 0;
			}
			else if(currentRow < 2){ 
				currentRow++;
			}
			break;
		case MyInput.BUTTON_BACKWARD:
			if(currentCol == 0){
				if(currentRow == 0){
					currentCol = 2;
					currentRow = 2;
				}else{
					currentCol = 2;
					currentRow = currentRow - 1;
				}
			} else {
				currentCol--;
			}
			break;

		case MyInput.BUTTON_FORWARD:
			if(currentCol == 2){
				if(currentRow == 2){
					currentCol = 0;
					currentRow = 0;
				} else {
					currentCol = 0;
					currentRow = currentRow +1;
				}
			} else {
				currentCol++;
			}
			break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		case MyInput.BUTTON_ESCAPE:
			gsm.setState(new MenuState(gsm));
			break;
		}

	}

	@Override
	public void update(float dt) {}

	
	/**
	 * this method sets the color and draw the letters in the menu
	 */
	@Override
	public void render() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		renderBackground();

		layout.setText(titleFont, title);
		float width = Variables.menuItemX + 240;
		float widthLay = layout.width;

		titleFont.draw(sb, title, (EGA.V_WIDTH-widthLay) / 2, 600);

		int x = menuItems[0].length;

		for (int row = 0; row < menuItems.length; row++){
			for (int col = 0; col < x; col++){
				
				layout.setText(font, menuItems[row][col]);

				int yPos = 400 - 100*row;
				
				//set color on letters
				if(currentRow == row && currentCol == col){
					font.setColor(Color.RED);
					
				}else{
					font.setColor(Color.WHITE);
				}
				//end set color on letters 

				//draw letters
				if(col == 0){
					int xPos0 = (int) (EGA.V_WIDTH - width - 7*70);
					font.draw(
							sb,
							menuItems[row][col],
							xPos0,
							yPos
							);

					menuItemPositions[row][col] = new Point(xPos0,EGA.V_HEIGTH-yPos);
					menuItemEndPositions[row][col] = new Point(xPos0+(int)width, EGA.V_HEIGTH-yPos+menuFontSize);
				}	
				
				if(col == 1){
					int xPos1 = (int) (EGA.V_WIDTH - width- 2*70 );
					font.draw(
							sb,
							menuItems[row][col],
							xPos1,
							yPos
							);
					menuItemPositions[row][col] = new Point(xPos1,EGA.V_HEIGTH-yPos);
					menuItemEndPositions[row][col] = new Point(xPos1+(int)width, EGA.V_HEIGTH-yPos+menuFontSize);
				}
				if(col == 2){
					int xPos2 = (int) (EGA.V_WIDTH - width + 3*70);
					font.draw(
							sb,
							menuItems[row][col],
							xPos2,
							yPos
							);
					menuItemPositions[row][col] = new Point(xPos2,EGA.V_HEIGTH-yPos);
					menuItemEndPositions[row][col] = new Point(xPos2+(int)width, EGA.V_HEIGTH-yPos+menuFontSize);
				}
				//end draw letters
			}

		}

		sb.end();

		rendered = true;

	}

	private void renderBackground(){
		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.draw(sb);
	}

	@Override
	public void dispose() {}

	public void select(int x, int y) {
		if(rendered && x > menuItemPositions[currentRow][currentCol].getX() 
				&& y > menuItemPositions[currentRow][currentCol].getY()
				&& x < menuItemEndPositions[currentRow][currentCol].getX() 
				&& y < menuItemEndPositions[currentRow][currentCol].getY()){
			select();
		}
	}

	public Point[] getMenuItemPositions() {
		return null;
	}

	public Point[] getMenuItemEndPositions() {
		return null;
	}

	public void setCurrentItem(int x, int y) {		
		if(rendered){
			for(int i = 0; i < menuItemPositions.length; i++){
				for(int j= 0; j < menuItemPositions[0].length; j++){
					if(x > menuItemPositions[i][j].getX() 
							&& y > menuItemPositions[i][j].getY()
							&& x < menuItemEndPositions[i][j].getX()
							&& y < menuItemEndPositions[i][j].getY()){
						currentRow = i;
						currentCol = j;
					}
				}
			}
		}
	}
}
