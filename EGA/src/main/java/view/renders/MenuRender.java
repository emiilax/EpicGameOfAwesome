package view.renders;

import java.awt.Point;

import model.MenuModel;
import model.Variables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import controller.EGA;

/**
 * A classed used for all menus to render the text accordingly.
 * @author Erik
 *
 */
public class MenuRender {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private BitmapFont subFont;
	private GlyphLayout layout = new GlyphLayout();

	private MenuModel model;

	private static Sprite backgroundSprite;

	private int titleHeight;

	private String menuItems[];
	private String matrixMenuItems[][];

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	
	private Point[][] matrixMenuItemPositions;
	private Point[][] matrixMenuItemEndPositions;

	boolean rendered;

	public MenuRender(MenuModel model){
		this.model = model;
		init();
	}

	@SuppressWarnings("deprecation")

	/**
	 * Initiates all variables. Generates the font from the font file. 
	 */
	private void init(){
		loadTextures();
		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(model.getTitleFontSize());
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(model.getMenuFontSize());

		if(model.getSubTitleFontSize() != 0){
			subFont = gen.generateFont(model.getSubTitleFontSize());
		}

		if(model.getMenuItems() != null){
			menuItems = model.getMenuItems();
			menuItemPositions = model.getMenuItemPositions();
			menuItemEndPositions = model.getMenuItemEndPositions();
		}
		if(model.getMatrixMenuItems() != null){
			matrixMenuItems = model.getMatrixMenuItems();
			matrixMenuItemPositions = model.getMatrixMenuItemPositions();
			matrixMenuItemEndPositions = model.getMatrixMenuItemEndPositions();
		}	
		
		titleHeight = model.getTitleHeight();

		rendered = false;
	}

	/**
	 * Loads the texture for the background
	 */
	private void loadTextures() {
		Texture backgroundTexture = new Texture("res/menu/skybackground_menu.jpg");
		backgroundSprite =new Sprite(backgroundTexture);
	}
	
	/**
	 * Renders the background with spritebatch
	 */
	private void renderBackground() {
		backgroundSprite.draw(sb);
	}
	
	/**
	 * Renders text from a array into a meny
	 * @param currentItem The item which will be marked with red labeltext
	 * @param cam The camera positions where the text will be rendered
	 * @param animatedTitle If the title should be animated or not
	 */
	public void render(int currentItem, OrthographicCamera cam, boolean animatedTitle) {

		menuItems = model.getMenuItems();

		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		sb.setProjectionMatrix(cam.combined);

		sb.begin();

		renderBackground();

		layout.setText(titleFont, model.getTitle());
		float width = layout.width;

		if(animatedTitle){
			animateTitle(width);
		}else {
			titleFont.draw(sb, model.getTitle(), (EGA.V_WIDTH-width) / 2, titleHeight);
		}

		for(int i = 0; i < menuItems.length; i++){
			layout.setText(font, menuItems[i]);
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}

			int yPos = model.getYPos() - model.getGap()*i;
			int xPos = model.getXPos();
			drawFont(menuItems[i],font,xPos, yPos);
			menuItemPositions[i] = new Point(xPos,EGA.V_HEIGTH-yPos);
			menuItemEndPositions[i] = new Point(xPos+(int)width, EGA.V_HEIGTH-yPos+model.getMenuFontSize());
		}

		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItemEndPositions(menuItemEndPositions);
		sb.end();

		rendered = true;
	}

	/**
	 * A rendermethod used only to loop in matrixes with labels into a
	 * menu. 
	 * @author Rebecka, Erik
	 * @param currentRow The row which will be marked with red labeltext
	 * @param currentCol The column which will be marked with red labeltext
	 * @param cam The camera positions where the text will be rendered
	 */
	public void renderMatrix(int currentRow, int currentCol, OrthographicCamera cam) {
		
		matrixMenuItems = model.getMatrixMenuItems();
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		renderBackground();

		layout.setText(titleFont, model.getTitle());
		float width = Variables.menuItemX + 240;
		float widthLay = layout.width;

		titleFont.draw(sb, model.getTitle(), (EGA.V_WIDTH-widthLay) / 2, titleHeight);

		int x = matrixMenuItems[0].length;

		for (int row = 0; row < matrixMenuItems.length; row++){
			for (int col = 0; col < x; col++){

				layout.setText(font, matrixMenuItems[row][col]);

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
							matrixMenuItems[row][col],
							xPos0,
							yPos
							);

					matrixMenuItemPositions[row][col] = new Point(xPos0,EGA.V_HEIGTH-yPos);
					matrixMenuItemEndPositions[row][col] = new Point(xPos0+(int)width, EGA.V_HEIGTH-yPos+model.getMenuFontSize());
				}	

				if(col == 1){
					int xPos1 = (int) (EGA.V_WIDTH - width- 2*70 );
					font.draw(
							sb,
							matrixMenuItems[row][col],
							xPos1,
							yPos
							);
					matrixMenuItemPositions[row][col] = new Point(xPos1,EGA.V_HEIGTH-yPos);
					matrixMenuItemEndPositions[row][col] = new Point(xPos1+(int)width, EGA.V_HEIGTH-yPos+model.getMenuFontSize());
				}
				if(col == 2){
					int xPos2 = (int) (EGA.V_WIDTH - width + 3*70);
					font.draw(
							sb,
							matrixMenuItems[row][col],
							xPos2,
							yPos
							);
					matrixMenuItemPositions[row][col] = new Point(xPos2,EGA.V_HEIGTH-yPos);
					matrixMenuItemEndPositions[row][col] = new Point(xPos2+(int)width, EGA.V_HEIGTH-yPos+model.getMenuFontSize());
				}
				//end draw letters
			}

		}

		sb.end();

		rendered = true;

	}
	public void drawFont(String item, BitmapFont myFont, int xPos, int yPos){
		boolean startedNow = false;
		if(!sb.isDrawing()){
			sb.begin();
			startedNow = true;
		}

		myFont.draw(
				sb,
				item,
				xPos,
				yPos
				);
		if(startedNow){
			sb.end();
		}
	}

	private void animateTitle(Float width){	
		if(titleHeight > 650){
			titleHeight -= 2;
		} 
		titleFont.draw(sb, model.getTitle(), (EGA.V_WIDTH-width) / 2, titleHeight);
		subFont.draw(sb, model.getSubTitle(), (EGA.V_WIDTH-width) / 2, titleHeight-120);
	}
}
