package view;

import java.awt.Point;

/**
 * @author Hampus Rönström
 * 
 * interface for menus
 */
public interface IMenu {
	public void handleInput(int i);
	public void select(int x, int y);
	public void render();
	public Point[] getMenuItemPositions();
	public Point[] getMenuItemEndPositions();
	public void setCurrentItem(int x, int y);
	
}
