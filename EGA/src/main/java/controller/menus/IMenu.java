package controller.menus;

/**
 * @author Hampus Rönström
 * 
 * interface for menus
 */
public interface IMenu {
	public void handleInput(int i);
	public void select(int x, int y);
	public void select();
	public void render();
	public void setCurrentItem(int x, int y);
	
}
