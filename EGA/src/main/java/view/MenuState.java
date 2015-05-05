package view;

import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import controller.EGA;
import controller.GameStateManager;

public class MenuState extends GameState{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();
	
	public static Texture backgroundTexture;
    public static Sprite backgroundSprite;
	
	private final String title = "EGA";
	
	private int titleFontSize = 150;
	private int menuFontSize = 50;
	
	private int currentItem;
	private String menuItems[];
	
	private GameStateManager gsm;
	
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		this.gsm = gsm;
		init();
		loadTextures();
	}
	
	public void init(){
		sb = new SpriteBatch();

		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
		);
		
		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(menuFontSize);
		
		menuItems = new String[]{
				"Play",
				"Level Select",
				"Settings"	
		};

	}
	

    private void loadTextures() {
        backgroundTexture = new Texture("res/menu/blue-sky-background.jpg");
        backgroundSprite =new Sprite(backgroundTexture);
    }
    
    public void renderBackground() {
        backgroundSprite.draw(sb);
    }
	
	
	@Override
	public void handleInput() {
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			if(currentItem > 0){
				currentItem --;
			} 
		}
		if(MyInput.isPressed(MyInput.BUTTON_DOWN)){
			if(currentItem < menuItems.length -1){
				currentItem++;
			}
		}
		if(MyInput.isPressed(MyInput.BUTTON_ENTER)){
			select();
		}
	}
	
	private void select(){
		if (currentItem == 0){
			gsm.setState(new Level(gsm));
		}
		if (currentItem == 1){
			System.out.println("Level select!");
		}
		if (currentItem == 2){
			System.out.println("Settings!");
		}
	}
	
	@Override
	public void update(float dt) {
		handleInput();
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
		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 800);
		
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
					450 - 70 *i
					);
			}
		
		sb.end();
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}