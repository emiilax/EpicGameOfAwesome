package controller.menus;

import java.awt.Point;

import view.renders.MenuRender;
import event.TheEvent;
import model.MenuModel;

/**
 * @author Hampus R�nstr�m
 *
 * Abstract class which all menu classes extend. Implements the IMenu interface.
 * Contains methods which work with the general menus. Exceptions are for example
 * the menuItemPositions in LevelSelct being in Point[][], so almost all methods
 * need @Override.
 */
public abstract class Menu extends GameState implements IMenu {
	protected MenuModel model;
	protected MenuRender view;
	protected boolean rendered;
	protected Point[] menuItemPositions;
	protected Point[] menuItemEndPositions;
	protected int titleFontSize, menuFontSize, currentItem, titleHeight, gap, xPos, yPos;
	protected String menuItems[];
	protected String title;
	

	
	public Menu(){
		super();
	}
	
	/**
	 * handles key input. Needs to be implement in every class that extends this class.
	 */
	public abstract void handleInput(int i);

	/**
	 * Gets called on mouse press. Selects the current item if the mous press
	 * is at the same position as the item.
	 */
	public void select(int x, int y) {
		menuItemPositions = model.getMenuItemPositions();
		menuItemEndPositions = model.getMenuItemEndPositions();
		if(x > menuItemPositions[currentItem].getX() 
				&& y > menuItemPositions[currentItem].getY()
				&& x < menuItemEndPositions[currentItem].getX() 
				&& y < menuItemEndPositions[currentItem].getY()){
			select();
		}
	}

	public void render() {
		updateModel();
		view.render(currentItem, getCam(), false);
		rendered = true;
	}
	
	/**
	 * Updates the variables in the model this menu is using.
	 * @author Erik
	 */
	protected void updateModel(){
		model.setMenuItemEndPositions(menuItemEndPositions);
		model.setMenuItemPositions(menuItemPositions);
		model.setMenuItems(menuItems);
		model.setTitleFontSize(titleFontSize);
		model.setMenuFontSize(menuFontSize);
		model.setTitle(title);
		model.setTitleHeight(titleHeight);
		model.setGap(gap);
		model.setXPos(xPos);
		model.setYPos(yPos);
	}

	/**
	 * Gets called on mouse move. Updates the current item if the position
	 * of the mouse is at the same position as an item.
	 */
	public void setCurrentItem(int x, int y) {
		menuItemPositions = model.getMenuItemPositions();
		menuItemEndPositions = model.getMenuItemEndPositions();
		if(rendered){
			for(int i = 0; i < menuItemPositions.length; i++){
				if(x > menuItemPositions[i].getX() && y > menuItemPositions[i].getY()
						&& x < menuItemEndPositions[i].getX() &&
						y < menuItemEndPositions[i].getY()){
					currentItem = i;
				}
			}	
		}
	}
	
	/**
	 * Selects what label pressed
	 */
	public abstract void select();
	
	@Override
	public void perform(TheEvent evt){
		if(evt.getNameOfEvent().equals("selectMenuItem")){
			select(evt.getX(), evt.getY());
		}
		if(evt.getNameOfEvent().equals("currentMenuItem")){
			setCurrentItem(evt.getX(), evt.getY());
		}
	};

}
