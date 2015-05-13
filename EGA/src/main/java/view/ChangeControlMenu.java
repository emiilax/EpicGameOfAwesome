package view;

import model.GameData;
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
import controller.MyInputProcessor;
import controller.SaveHandler;

public class ChangeControlMenu extends GameState {

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Sprite backgroundSprite;

	private String title = "Settings";

	private int titleFontSize = 70;
	private int menuFontSize = 30;

	private int currentItem;
	private String menuItems[];
	private String currentButtons[];
	private boolean changeMode = false;

	private GameData gd;

	private GameStateManager gsm;

	public ChangeControlMenu(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		init();	
	}

	private void init(){

		gd = SaveHandler.getGameData();
		sb = new SpriteBatch();

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(menuFontSize);

		currentButtons = new String[]{
				Keys.toString(gd.enter),
				Keys.toString(gd.up),
				Keys.toString(gd.down),
				Keys.toString(gd.left),
				Keys.toString(gd.right),
				Keys.toString(gd.pause),
				Keys.toString(gd.restart),
				Keys.toString(gd.escape),
				""
		};

		currentItem = 0;
	}

	@Override
	public void handleInput(int i) {
		if(changeMode){
			changeButton();
		} else {
			if(i == MyInput.BUTTON_JUMP){
				if(currentItem > 0){
					currentItem--;
				}
			} else if(i == MyInput.BUTTON_DOWN){ 
				if(currentItem < menuItems.length-1){
					currentItem++;
				}
			}else if (i == MyInput.BUTTON_ESCAPE){
				gsm.getGame().setLevel(new MenuState(gsm));
			} else if (i == MyInput.BUTTON_ENTER) { 
				selectChange();
			}
		}
	}

	private void changeButton(){
		int key = MyInputProcessor.getPressed();
		if(key != gd.enter){
			switch(currentItem){
			case 0: gd.enter = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.enter));
			break;
			case 1: gd.up = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.up));
			break;
			case 2: gd.down = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.down));
			break;
			case 3: gd.left = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.left));
			break;
			case 4: gd.right = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.right));
			break;
			case 5: gd.pause = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.pause));
			break;
			case 6: gd.restart = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.restart));
			break;
			}
		}
		changeMode = false;
		SaveHandler.save();
	}


	private void selectChange(){
		if(currentItem == 8){
			gsm.getGame().setLevel(new MenuState(gsm));
		}
		setCurrentButtons(currentItem, "...");
		changeMode = true;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {

		menuItems = new String[]{
				"Enter: ",
				"Jump/Menu up: ",
				"Menu down: ",
				"Left: " ,
				"Right: " ,
				"Pause: " ,
				"Restart: ",
				"Escape: ",
				"BACK"
		};

		String[] buttons = getCurrentButtons();

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
					450 - 50 *i
					);
			font.draw(
					sb,
					buttons[i],
					(EGA.V_WIDTH - (EGA.V_WIDTH/2)),
					450 - 50 *i
					);
		}
		sb.end();

	}

	/*private void renderBackground(){
		backgroundSprite = new Sprite(new Texture("res/menu/emilsmamma.jpg"));
		backgroundSprite.draw(sb);
	}*/

	private String[] getCurrentButtons() {
		return currentButtons;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void setCurrentButtons(int index, String key){
		if(index < currentButtons.length){
			currentButtons[index] = key;
		}
	}

}
