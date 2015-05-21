package view;

import java.awt.Point;
import java.util.List;

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

public class ChangeControlMenu extends GameState implements IMenu{

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

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	private boolean gotInput = false;

	private boolean rendered = false;
	private String latestRemoved;
	private GameData gd;

	private GameStateManager gsm;
	private GameState curGame;
	
	public ChangeControlMenu(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		init();	
		this.curGame = null;
	}
	
	public ChangeControlMenu(GameStateManager gsm, GameState curGame){
		this(gsm);
		this.curGame = curGame;
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
				Keys.toString(gd.up),
				Keys.toString(gd.down),
				Keys.toString(gd.left),
				Keys.toString(gd.right),
				Keys.toString(gd.pause),
				Keys.toString(gd.restart),
				Keys.toString(gd.escape),
				""
		};

		menuItems = new String[]{
				"Jump/Menu up: ",
				"Menu down: ",
				"Left: " ,
				"Right: " ,
				"Pause: " ,
				"Restart: ",
				"Escape: ",
				"BACK"
		};

		int length = menuItems.length;
		menuItemPositions = new Point[length];
		menuItemEndPositions = new Point[length];
		rendered = false;

		currentItem = 0;
	}

	@Override
	public void handleInput(int i) {
		if(!changeMode){
			if(i == MyInput.BUTTON_JUMP){
				if(currentItem > 0){
					currentItem--;
				}
			} else if(i == MyInput.BUTTON_DOWN){ 
				if(currentItem < menuItems.length-1){
					currentItem++;
				}
			}else if (i == MyInput.BUTTON_ESCAPE){
				menuBack();				
			} else if (i == MyInput.BUTTON_ENTER) { 
				selectChange();
			}
		} else {changeButton();}
	}

	private void changeButton(){
		int key = MyInputProcessor.getPressed();
		List<Integer> keys = gd.getKeysList();
		if(keys.contains(Keys.valueOf(latestRemoved))){
			keys.remove(keys.indexOf(Keys.valueOf(latestRemoved)));
		}
		if(!keys.contains(key)){
			switch(currentItem){
			case 0: gd.up = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.up));
			break;
			case 1: gd.down = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.down));
			break;
			case 2: gd.left = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.left));
			break;
			case 3: gd.right = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.right));
			break;
			case 4: gd.pause = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.pause));
			break;
			case 5: gd.restart = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.restart));
			break;
			case 6: gd.escape = key; 
			setCurrentButtons(currentItem, Keys.toString(gd.escape));
			break;
			}
			changeMode = false;
			gd.updateList();
			SaveHandler.save();
		}
	}


	private void selectChange(){
		if(currentItem == 7){
			menuBack();
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

			int xPosMenuItem = (int) ((EGA.V_WIDTH - smallWidth) - (EGA.V_WIDTH)/2);
			int yPosMenuItem = 450 - 50 *i;

			font.draw(
					sb,
					menuItems[i],
					xPosMenuItem,
					yPosMenuItem
					);

			int xPosButton = (EGA.V_WIDTH - (EGA.V_WIDTH/2));
			int yPosButton = 450 - 50 *i;

			font.draw(
					sb,
					buttons[i],
					xPosButton,
					yPosButton
					);

			menuItemPositions[i] = new Point(xPosMenuItem,EGA.V_HEIGTH-yPosMenuItem);
			menuItemEndPositions[i] = new Point(xPosButton+(int)width, 
					EGA.V_HEIGTH-yPosButton+menuFontSize);

		}
		sb.end();

		rendered = true;

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
			latestRemoved = currentButtons[index];
			currentButtons[index] = key;
		}
	}

	public void select(int x, int y) {
		if(rendered && x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			selectChange();
		}

	}
	
	private void menuBack(){
		if(curGame != null){
			gsm.getGame().setLevel(curGame);
		}else{
			gsm.getGame().setLevel(new SettingsMenu(gsm));
		}
	}

	public Point[] getMenuItemPositions() {
		return menuItemPositions;
	}

	public Point[] getMenuItemEndPositions() {
		return menuItemEndPositions;
	}

	public void setCurrentItem(int x, int y) {
		if(rendered){
			for(int i = 0; i < menuItemPositions.length; i++){
				if(isHovered(x, y, i, menuItemPositions[i], menuItemEndPositions[i])){
					currentItem = i;
				}
			}	
		}		
	}

	private boolean isHovered(int x, int y, int i, Point point, Point pointEnd){
		return (x > point.getX() && y > point.getY()
				&& x < pointEnd.getX() &&
				y < pointEnd.getY());
	}

}
