package controller.menus;

import java.awt.Point;
import java.util.List;

import view.IMenu;
import view.MenuRender;
import model.GameData;
import model.MenuModel;
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
import controller.GameState;
import controller.GameStateManager;
import controller.MyInputProcessor;
import controller.SaveHandler;
import controller.Variables;

public class ChangeControlMenu extends GameState implements IMenu{

	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	private Sprite backgroundSprite;
	private Texture backgroundTexture; 

	private String title = "Settings";

	private int titleFontSize = Variables.subMenuTitleSize;
	private int menuFontSize = 30;
	private int titleHeight = 600;
	private int gap = 50;
	private int xPos = (EGA.V_WIDTH - (EGA.V_WIDTH/2));
	private int yPos = 450;
	
	private int currentItem;
	private String menuItems[];
	private String currentButtons[];
	private boolean changeMode = false;

	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;

	private boolean rendered = false;
	private String latestRemoved;
	private GameData gd;

	private GameStateManager gsm;
	
	private MenuModel model;
	private MenuRender view;


	public ChangeControlMenu(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		init();
	}


	private void init(){

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
		System.out.println(currentItem);
		if(!(keys.contains(key))){
			switch(currentItem){
			case 0: gd.up = key;
			System.out.println(Keys.toString(gd.up));
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
	
		updateModel();
		view.render(currentItem, cam, false);
		
		layout.setText(titleFont, title);
		float width = layout.width;

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
//			menuItemPositions[i] = new Point(xPosMenuItem,EGA.V_HEIGTH-yPosMenuItem);
//			menuItemEndPositions[i] = new Point(xPosButton+(int)width, 
//					EGA.V_HEIGTH-yPosButton+menuFontSize);

		}

		rendered = true;
	}

	private void updateModel(){
		model.setMenuItemEndPositions(menuItemEndPositions);
		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItems(getCurrentButtons());
		model.setTitleFontSize(titleFontSize);
		model.setMenuFontSize(menuFontSize);
		model.setTitle(title);
		model.setTitleHeight(titleHeight);
		model.setGap(gap);
		model.setXPos(xPos);
		model.setYPos(yPos);
	}

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
		updateModel();
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
		gsm.popState();	
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
