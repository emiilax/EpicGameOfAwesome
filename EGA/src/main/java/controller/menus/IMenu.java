package controller.menus;

/**
 * interface for menus
 * 
 * @author Hampus Rönström
 * 
 * 
 */
public interface IMenu {
	
	public void handleInput(int i);
	public void select(int x, int y);
	public void select();
	public void render();
	public void setCurrentItem(int x, int y);
	
}
