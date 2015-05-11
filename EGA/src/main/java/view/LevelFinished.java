package view;

import model.EGATimer;
import model.GameData;

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

public class LevelFinished extends GameState {
	
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
	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;
	private EGATimer timer;
	
	public LevelFinished(GameStateManager gsm, Texture backgroundTexture, int level){
		super(gsm);
		this.gsm = gsm;
		this.backgroundTexture = backgroundTexture;
		this.level = level;
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
				"Replay",
				"Next Level",
		};
		
		setTimeString();
		
		SaveHandler.save();
	}
	
	private void setTimeString(){
		timer = EGATimer.getTimer();
		Float timePassed = timer.getTimePassed();
		
		
		if(SaveHandler.gd.isBetterTime(level, timePassed)){
			title = "Nytt rekord! " + "\n" + "Din tid blev: " +  Float.toString(timePassed);
		} else {
			title = "Din tid blev: " + "\n" + Float.toString(timePassed) 
					+ "\n" + "Din bästa tid: " + "\n" + Float.toString(SaveHandler.gd.getTime(level));
		}
		SaveHandler.gd.addTime(level, timePassed);
		
		
	}

	@Override
	public void handleInput(int i) {
		// TODO Auto-generated method stub
		
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
			if(currentItem == i){
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
