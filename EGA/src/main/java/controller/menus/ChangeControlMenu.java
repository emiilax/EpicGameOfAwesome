package controller.menus;

import java.awt.Point;
import java.util.List;

import view.renders.MenuRender;
import model.GameData;
import model.MenuModel;
import model.MyInput;
import model.Variables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import controller.EGA;
import controller.GameStateManager;
import controller.MyInputProcessor;
import controller.SaveHandler;

/**
 * The control class for the menu that handles and shows the buttonmapping for the game. When this
 * menu is visible it is possible to change the controls used to play and navigate menus.
 * @author Erik
 *
 */
public class ChangeControlMenu extends Menu{

	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();


	private String currentButtons[];
	private boolean changeMode = false;
	
	private String latestRemoved;
	private GameData gd;

	private GameStateManager gsm;

	public ChangeControlMenu(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		init();
	}

	@SuppressWarnings("deprecation")

	private void init(){
		title = "Settings";
		titleFontSize = Variables.subMenuTitleSize;
		menuFontSize = 30;
		titleHeight = 600;
		gap = 50;
		xPos = (EGA.V_WIDTH - (EGA.V_WIDTH/2));
		yPos = 450;
		

		gd = SaveHandler.getGameData();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);
		
		titleFont = gen.generateFont(titleFontSize);
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
		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);
		rendered = false;

		currentItem = 0;
	}

	/**
	 * @inherent
	 * If changemode is active, call the changebutton method instead.
	 */
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
				select();
			}
		} else {changeButton();}
	}
	
	/**
	 * Checks what button was previously pressed and what button is currently pressed.
	 * Changes the mapping of the previously pressed button, if it was a valid button. 
	 */
	private void changeButton(){
		int key = MyInputProcessor.getPressed();
		List<Integer> keys = gd.getKeysList();
		if(keys.contains(Keys.valueOf(latestRemoved))){
			keys.remove(keys.indexOf(Keys.valueOf(latestRemoved)));
		}
		if(!(keys.contains(key))){
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

	/**
	 * @inherent
	 * If anything but the back button was pressed, change the button-part of the selected
	 * item to "...".
	 */
	@Override
	public void select(){
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
	
		updateModel();
		view.render(currentItem, cam, false);
		
		layout.setText(titleFont, title);

		for(int i = 0; i < menuItems.length; i++){
			if(currentItem == i){
				font.setColor(Color.RED);
			} else {
				font.setColor(Color.WHITE);
			}
			layout.setText(font, menuItems[i]);
			float smallWidth = layout.width;

			int xPosMenuItem = (int) ((EGA.V_WIDTH - smallWidth) - (EGA.V_WIDTH)/2);
			int yPosMenuItem = 450 - 50 *i;

			view.drawFont(menuItems[i], font, xPosMenuItem, yPosMenuItem);
			menuItemPositions[i] = new Point(xPosMenuItem,EGA.V_HEIGTH-yPosMenuItem);
		}
		model.setMenuItemPositions(menuItemPositions);

		rendered = true;
	}

	@Override
	protected void updateModel(){
		super.updateModel();
		model.setMenuItems(getCurrentButtons());
	}
	
	/**
	 * 
	 * @return Currently used buttons in string format
	 */
	private String[] getCurrentButtons() {
		return currentButtons;
	}
	
	/**
	 * 
	 * @param index The index where the button is located in the arrey
	 * @param key The new button in string format
	 */
	private void setCurrentButtons(int index, String key){
		if(index < currentButtons.length){
			latestRemoved = currentButtons[index];
			currentButtons[index] = key;
		}
		updateModel();
	}
	
	private void menuBack(){
		gsm.popState();	
	}

}
