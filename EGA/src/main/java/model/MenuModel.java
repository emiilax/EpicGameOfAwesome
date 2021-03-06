package model;

import java.awt.Point;

import lombok.Data;

/**
 * A class used to store all variables in every menu. Contains only get and set methods.
 * @author Erik
 *
 */
@Data
public class MenuModel{
	
	private String title;
	private String subTitle;
	private int titleFontSize;
	private int menuFontSize;
	private int subTitleFontSize;
	private int titleHeight;
	
	private Point[] menuItemPositions;
	private Point[] menuItemEndPositions;
	private String menuItems[];
	private String matrixMenuItems[][];
	private Point[][] matrixMenuItemPositions;
	private Point[][] matrixMenuItemEndPositions;
	
	private int xPos;
	private int yPos;
	private int gap;
}
