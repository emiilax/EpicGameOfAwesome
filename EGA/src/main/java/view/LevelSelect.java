package view;

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

public class LevelSelect extends GameState {
	
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private String title;
	
	//private GameData gd;

	private int titleFontSize = 50;
	private int menuFontSize = 50;
	private int level;
	private int currentItem1;
	private String menuItems[];

	private GameStateManager gsm;
	private EGATimer timer;
	
	public LevelSelect(GameStateManager gsm, Texture backgroundTexture){
		super(gsm);
		this.gsm = gsm;
		this.backgroundTexture = backgroundTexture;
	//	gd = SaveHandler.getGameData();
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

		menuItems = new String[]{
				"Level 1",
				"Level 2",
				"Back",
		};
		
		setTitle();
		
		SaveHandler.save();
	}
	
	private void setTitle(){
		title = "This is Level Select!";
	}

	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentItem1 > 0){
				currentItem1 --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentItem1 < menuItems.length-1){
				currentItem1++;
			}
			break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		}
	}
	
	private void select(){
		if(currentItem1 == 0){ //this is selected directly if enter is pressed to long
			gsm.getGame().setLevel(new Level(gsm, gsm.getLevel(1)));
			System.out.print("Select level 1");
		}
		if(currentItem1 == 1){
			gsm.getGame().setLevel(new Level(gsm, gsm.getLevel(2)));
			System.out.print("Select level 2");
		} else if(currentItem1 == 2){
			gsm.getGame().setLevel(new MenuState(gsm));
			System.out.print("Menu");
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

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

		for(int i = 0; i < menuItems.length; i++){
			layout.setText(font, menuItems[i]);
			if(currentItem1 == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			font.draw(
					sb,
					menuItems[i],
					(EGA.V_WIDTH - width) / 2,
					300 - 70 *i
					);
		}
		sb.end();
		
	}
	
	private void renderBackground(){
		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.draw(sb);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
