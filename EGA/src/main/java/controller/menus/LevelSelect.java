package controller.menus;

import java.awt.Frame;
import java.awt.Point;

import javax.swing.JOptionPane;

import view.renders.MenuRender;
import model.MenuModel;
import model.MyInput;
import model.Variables;
import controller.EGA;
import controller.savehandler.SaveHandler;
import event.EventSupport;

/**
 * This GameState is used to choose the level for the game.
 * @author Rebecka Reitmaier
 * 
 * 
 * @param gsm, GameStateManeger
 * @param backGroundTexture, Texture  
 */

public class LevelSelect extends Menu {

	private int currentRow = 0;
	private int currentCol = 0;

	private Point [][] menuItemPositions;
	private Point [][] menuItemEndPositions;

	private String menuItems [][];

	//private GameStateManager gsm;
	private MenuModel model;
	private MenuRender view;
	private int currentlyVisible = 0;

	/**
	 * LevelSelect constructor calls the superclass and initializes it
	 * @param gsm, the GameStateManeger
	 */
	/*
	public LevelSelect(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
		init();
	}*/

	public LevelSelect(){
		super();
		init();
	}

	/**
	 * Initializes how the visual elements in the menu should look like.
	 * It updates the model.
	 */
	private void init(){
		titleFontSize = Variables.subMenuTitleSize;
		menuFontSize = Variables.subMenuItemSize;
		titleHeight = 650;
		gap = 70;
		xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
		yPos = 450;

		menuItems = new String[][]{
				{"Level 1", "Level 2", "Level 3"}, //row 0 
				{"Level 4", "Level 5", "Level 6"}, //row 1
				{"<--", "Back", "-->"}, 

		};

		setTitle();

		SaveHandler.save();

		menuItemPositions = new Point[menuItems.length][menuItems[0].length];
		menuItemEndPositions = new Point[menuItems.length][menuItems[0].length];

		model = new MenuModel();
		updateModel();

		view = new MenuRender(model);

		rendered = false;
	}

	/**
	 * This method sets the title for the levelSelect menu.
	 */
	private void setTitle(){
		title = "Choose level to play";
	}

	/**
	 * This method is used to locate which level should be created 
	 * or if it should which to another menu.
	 * 
	 * Add more if-states if group add more levels.
	 */
	@Override
	public void select(){
		int levelIndex = 0;
		if(currentlyVisible == 0){
			levelIndex = 0;
		} else if (currentlyVisible == 1){
			levelIndex = 6;
		}
		if(currentRow == 0){
			if(currentCol == 0){
				levelIndex = levelIndex+1;
			}
			if(currentCol == 1){
				levelIndex = levelIndex+2;
			}
			if(currentCol == 2){
				levelIndex = levelIndex+3;
			}
		} else if(currentRow == 1){
			if(currentCol == 0){
				levelIndex = levelIndex+4;
			}
			if(currentCol == 1){
				levelIndex = levelIndex+5;
			}
			if(currentCol == 2){
				levelIndex = levelIndex+6;
			}
		} else {
			if(currentCol == 0 || currentCol == 2){
				changeMenuItems(getNext());
				return;
			}
			if(currentCol == 1){
				EventSupport.getInstance().fireNewEvent("pop");
				return;
			}
		}

		try{
			EventSupport.getInstance().fireNewEvent("level", levelIndex);
		}catch (NullPointerException e){
			JOptionPane.showMessageDialog(new Frame(), "Level doesn't exist");
		}
	}

	private void changeMenuItems(int index){
		if(index == 0){
			menuItems = new String[][]{
					{"Level 1", "Level 2", "Level 3"}, //row 0 
					{"Level 4", "Level 5", "Level 6"}, //row 1
					{"<--", "Back", "-->"}, 

			};
		} else if (index == 1){
			menuItems = new String[][]{
					{"Level 7", "Level 8", "Level 9"}, //row 0 
					{"Level 10", "Level 11", "Level 12"}, //row 1
					{"<--", "Back", "-->"}, 

			};
		}
		currentlyVisible = index;
		updateModel();
		System.out.println(model.getMatrixMenuItems()[0][0]);
	}

	private int getNext(){
		if(currentlyVisible == 0){
			return currentlyVisible+1;
		} else {
			return 0;
		}
	}

	/**
	 * This method handles the key input and decides which
	 * string is current in the matrix. 
	 */
	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentRow == 0){
				currentRow = 2;
			}
			else if(currentRow > 0){
				currentRow --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentRow == 2){
				currentRow = 0;
			}
			else if(currentRow < 2){ 
				currentRow++;
			}
			break;
		case MyInput.BUTTON_BACKWARD:
			if(currentCol == 0){
				if(currentRow == 0){
					currentCol = 2;
					currentRow = 2;
				}else{
					currentCol = 2;
					currentRow = currentRow - 1;
				}
			} else {
				currentCol--;
			}
			break;

		case MyInput.BUTTON_FORWARD:
			if(currentCol == 2){
				if(currentRow == 2){
					currentCol = 0;
					currentRow = 0;
				} else {
					currentCol = 0;
					currentRow = currentRow +1;
				}
			} else {
				currentCol++;
			}
			break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		case MyInput.BUTTON_ESCAPE:
			EventSupport.getInstance().fireNewEvent("main");
			//gsm.setState(new MainMenu(gsm));
			break;
		}

	}

	@Override
	public void update(float dt) {}

	/**
	 * this method sets the color and draw the letters in the menu
	 */
	@Override
	public void render() {
		updateModel();
		view.renderMatrix(currentRow, currentCol, getCam());
		rendered = true;

	}

	/**
	 * This method updates the model with all the current values.
	 */
	@Override
	public void updateModel(){
		model.setMatrixMenuItemEndPositions(menuItemEndPositions);
		model.setMatrixMenuItemPositions(menuItemPositions);
		model.setMatrixMenuItems(menuItems);
		model.setTitleFontSize(titleFontSize);
		model.setMenuFontSize(menuFontSize);
		model.setTitle(title);
		model.setTitleHeight(titleHeight);
		model.setGap(gap);
		model.setXPos(xPos);
		model.setYPos(yPos);
	}

	/**
	 * This method is used to select an object in the matrix with the mouse.
	 */
	@Override
	public void select(int x, int y) {
		if(rendered && x > menuItemPositions[currentRow][currentCol].getX() 
				&& y > menuItemPositions[currentRow][currentCol].getY()
				&& x < menuItemEndPositions[currentRow][currentCol].getX() 
				&& y < menuItemEndPositions[currentRow][currentCol].getY()){
			select();
		}
	}
	/**
	 * This method is used to select which object in the matrix is current.
	 */
	@Override
	public void setCurrentItem(int x, int y) {		
		if(rendered){
			for(int i = 0; i < menuItemPositions.length; i++){
				for(int j= 0; j < menuItemPositions[0].length; j++){
					if(x > menuItemPositions[i][j].getX() 
							&& y > menuItemPositions[i][j].getY()
							&& x < menuItemEndPositions[i][j].getX()
							&& y < menuItemEndPositions[i][j].getY()){
						currentRow = i;
						currentCol = j;
					}
				}
			}
		}
	}
}
