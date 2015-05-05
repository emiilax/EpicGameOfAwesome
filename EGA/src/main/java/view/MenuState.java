package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
	
	private final String title = "EGA";  
	
	private int currentItem;
	private String menuItems[];
	
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init(){
		sb = new SpriteBatch();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
		);
		
		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(50);
		
		menuItems = new String[]{
				"Play",
				"Level Select",
				"Settings"	
		};
	}
	
	
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(float dt) {
		handleInput();
	}
	@Override
	public void render() {
		
		cam.update();
		
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		
		layout.setText(titleFont, title);
		float width = layout.width;
		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 600);
		
		for(int i = 0; i < menuItems.length; i++){
			layout.setText(titleFont, menuItems[i]);
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			font.draw(
					sb,
					menuItems[i],
					(EGA.V_WIDTH - width) / 2,
					360 - 70 *i
					);
			}
		
		sb.end();
		
		
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}