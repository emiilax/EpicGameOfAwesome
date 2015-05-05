package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import controller.GameStateManager;

public class MenuState extends GameState{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title = "EGA";  
	
	private int currentItem;
	private String menuItems[];
	
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
		init();
	}

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton button;
	private BitmapFont white, black;
	private Label heading;
	
	
	public void init(){
		sb = new SpriteBatch();
		FreeTypeFontGenerator gsm = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/orbitron-black.otf"));
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}