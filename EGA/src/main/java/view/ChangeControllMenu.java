package view;

import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

public class ChangeControllMenu extends GameState {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Sprite backgroundSprite;

	private String title = "Settings";

	private int titleFontSize = 70;
	private int menuFontSize = 50;

	private int currentItem;
	private String menuItems[];

	private GameStateManager gsm;

	public ChangeControllMenu(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
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
				"Jump/Menu up: " + Keys.UP,
				"Menu down: ",
				"Left: ",
				"Right: ",
				"Pause: ",
		};

		currentItem = 0;
	}

	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP: 
			if(currentItem > 0){
				currentItem--;
			}
		break;
		case MyInput.BUTTON_DOWN: 
			if(currentItem < menuItems.length-1){
				currentItem++;
			}
		break;
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
		//renderBackground();

		layout.setText(titleFont, title);
		float width = layout.width;

		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 600);

		for(int i = 0; i < menuItems.length; i++){
			layout.setText(font, menuItems[i]);
			float smallWidth = layout.width;
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			font.draw(
					sb,
					menuItems[i],
					(EGA.V_WIDTH - smallWidth) - (EGA.V_WIDTH)/2,
					450 - 70 *i
					);
		}
		sb.end();

	}

	/*private void renderBackground(){
		backgroundSprite = new Sprite(new Texture("res/menu/emilsmamma.jpg"));
		backgroundSprite.draw(sb);
	}*/

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
