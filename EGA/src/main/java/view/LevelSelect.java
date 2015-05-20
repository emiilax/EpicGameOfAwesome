package view;

import java.awt.Point;

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
import controller.GameStateManager;
import controller.SaveHandler;

public class LevelSelect extends GameState implements IMenu {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private String title;

	private int titleFontSize = 50;
	private int menuFontSize = 50;

	private boolean rendered = false;


	private int currentRow = 0;
	private int currentCol = 0;

	private Point [][] menuItemPositions;
	private Point [][] menuItemEndPositions;

	private String menuItems [][];


	private GameStateManager gsm;

	public LevelSelect(GameStateManager gsm, Texture backgroundTexture){
		super(gsm);
		this.gsm = gsm;
		this.backgroundTexture = backgroundTexture;
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

	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentRow == 1){
				currentRow --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentRow == 0){ //needs to be changed if amount of rows is changed
				currentRow++;
			}
			break;
		case MyInput.BUTTON_BACKWARD:
			if(currentCol == 0){
				if(currentRow == 1){
					currentCol = 2;
					currentRow = 0;
				} else {
					currentCol = 2;
					currentRow = 1;
				}
			} else {
				currentCol--;
			}
			break;

		case MyInput.BUTTON_FORWARD:
			if(currentCol == 2){
				if(currentRow == 1){
					currentCol = 0;
					currentRow = 0;
				} else {
					currentCol = 0;
					currentRow = 1;
				}
			} else {
				currentCol++;
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
		String lvl = menuItems[currentRow][currentCol];
		if(lvl == "Level 1"){
			
			gsm.getGame().setLevel(new Level(gsm, gsm.getLevel(1)));
		}
	}

	@Override
	public void update(float dt) {}

	@Override
	public void render() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		renderBackground();

		layout.setText(titleFont, title);
		float width = layout.width;

		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 600);

		int x = menuItems[0].length;

		for (int row = 0; row < menuItems.length; row++){
			for (int col = 0; col < x; col++){
				layout.setText(font, menuItems[row][col]);

				int yPos = 350 - 180*row;

				if(currentRow == row && currentCol == col){
					font.setColor(Color.RED);
					
				}else{
					font.setColor(Color.WHITE);
				}



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
					int xPos1 = (int) (EGA.V_WIDTH - width - 2*70 );
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
