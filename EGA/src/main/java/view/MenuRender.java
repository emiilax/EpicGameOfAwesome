package view;

import java.awt.Point;

import model.MenuModel;

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

public class MenuRender {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private BitmapFont subFont;
	private GlyphLayout layout = new GlyphLayout();

	private MenuModel model;

	private static Sprite backgroundSprite;

	private String title;
	private String subTitle;
	private int titleHeight;

	private String menuItems[];

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	boolean rendered;

	public MenuRender(MenuModel model){
		this.model = model;
		init();
	}

	private void init(){
		loadTextures();
		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(model.getTitleFontSize());
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(model.getMenuFontSize());
		subFont = gen.generateFont(model.getSubTitleFontSize());

		menuItems = model.getMenuItems();
		menuItemPositions = model.getMenuItemPositions();
		menuItemEndPositions = model.getMenuItemEndPositions();
		titleHeight = model.getTitleHeight();

		rendered = false;
	}

	private void loadTextures() {
		Texture backgroundTexture = new Texture("res/menu/skybackground_menu.jpg");
		backgroundSprite =new Sprite(backgroundTexture);
	}

	private void renderBackground() {
		backgroundSprite.draw(sb);
	}

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

			int yPos = 450 - 70*i;
			int xPos = (int)(EGA.V_WIDTH - 365) / 2;
			font.draw(
					sb,
					menuItems[i],
					xPos,
					yPos
					);
			menuItemPositions[i] = new Point(xPos,EGA.V_HEIGTH-yPos);
			menuItemEndPositions[i] = new Point(xPos+(int)width, EGA.V_HEIGTH-yPos+model.getMenuFontSize());
		}

		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItemEndPositions(menuItemEndPositions);
		sb.end();

		rendered = true;
	}
	
	public void draw(int xPos, int yPos){
		
	}

	private void animateTitle(Float width){	
		if(titleHeight > 650){
			titleHeight -= 2;
		} 
		titleFont.draw(sb, model.getTitle(), (EGA.V_WIDTH-width) / 2, titleHeight);
		subFont.draw(sb, model.getSubTitle(), (EGA.V_WIDTH-width) / 2, titleHeight-120);
	}
}
