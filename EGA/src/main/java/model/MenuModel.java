package model;

import java.awt.Point;

import lombok.Data;
import controller.Variables;

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
	
	private int xPos;
	private int yPos;
}
