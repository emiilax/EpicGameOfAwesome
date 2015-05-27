package controller.menus;

import java.awt.Point;
import view.MenuRender;
import model.MenuModel;
import model.MyInput;
import controller.EGA;
import controller.GameStateManager;
import controller.Level;
import controller.SaveHandler;
import controller.Variables;

/**
 * 
 * @author Rebecka Reitmaier
 * This GameState is used to choose the level for the game.
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

	private GameStateManager gsm;
	private MenuModel model;
	private MenuRender view;

	/**
	 * LevelSelect constructor calls the superclass and initializes it
	 * @param gsm, the GameStateManeger
	 */
	public LevelSelect(GameStateManager gsm){
		super(gsm);
		this.gsm = gsm;
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
		String element = menuItems[currentRow][currentCol];
		if(element == "Level 1"){
			gsm.setState(new Level(gsm, gsm.getLevel(1)));
			gsm.setCurrentLevel(1);
		}
		if(element == "Level 2"){
			gsm.setState(new Level(gsm, gsm.getLevel(2)));
			gsm.setCurrentLevel(2);
		}
		if(element == "Level 3"){
			gsm.setState(new Level(gsm, gsm.getLevel(3)));
			gsm.setCurrentLevel(3);
		}
		if(element == "Level 4"){
			gsm.setState(new Level(gsm, gsm.getLevel(4)));
			gsm.setCurrentLevel(4);
		}
		if(element == "Level 5"){
			gsm.setState(new Level(gsm, gsm.getLevel(5)));
			gsm.setCurrentLevel(5);
		}
		if(element == "Level 6"){
			gsm.setState(new Level(gsm, gsm.getLevel(6)));
			gsm.setCurrentLevel(6);
		}
		if(element == "<--" || element == "-->"){
			// switch String array 
		}
		if(element == "Back"){
			//switch to main menu
			gsm.popState();
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
			gsm.setState(new MainMenu(gsm));
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
		view.renderMatrix(currentRow, currentCol, cam);
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
