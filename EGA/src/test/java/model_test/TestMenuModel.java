package model_test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;

import model.MenuModel;

import org.junit.Test;

/**
 * 
 * @author Rebecka Reitmaier
 *
 * This class test the MenuModel
 * It is only get and set methods
 */
public class TestMenuModel {

	@Test
	public void testTitle() {
		MenuModel mm = new MenuModel();
		mm.setTitle("Test Title");
		String s = "Test Title";
		assertTrue(s.equals(mm.getTitle()));
	}
	@Test
	public void testSubTitle() {
		MenuModel mm = new MenuModel();
		mm.setSubTitle("Test SubTitle");
		String s = "Test SubTitle";
		assertTrue(s.equals(mm.getSubTitle()));
	}
	@Test
	public void testTitleFontSize(){
		MenuModel mm = new MenuModel();
		mm.setTitleFontSize(10);
		assertTrue(mm.getTitleFontSize()==10);
	}
	@Test
	public void testMenuFontSize(){
		MenuModel mm = new MenuModel();
		mm.setMenuFontSize(10);
		assertTrue(mm.getMenuFontSize()==10);
	}
	@Test
	public void testSubTitleFontSize(){
		MenuModel mm = new MenuModel();
		mm.setSubTitleFontSize(10);
		assertTrue(mm.getSubTitleFontSize()==10);
	}
	@Test
	public void testTitleHeight(){
		MenuModel mm = new MenuModel();
		mm.setTitleHeight(10);
		assertTrue(mm.getTitleHeight()==10);
	}
	
	@Test 
	public void testMenuItemPosition(){
		MenuModel mm = new MenuModel();
		Point p = new Point();
		p.x = 10;
		p.y = 100;
		Point [] pointArray = new Point [10];
		pointArray[0]= p;
		mm.setMenuItemPositions(pointArray);
		Point [] testArray = mm.getMenuItemPositions();
		assertTrue(Arrays.equals(pointArray, testArray) && testArray[0].equals(pointArray[0]));
	}
	@Test 
	public void testMenuItemEndPosition(){
		MenuModel mm = new MenuModel();
		Point p = new Point();
		p.x = 10;
		p.y = 100;
		Point [] pointArray = new Point [10];
		pointArray[0]= p;
		mm.setMenuItemEndPositions(pointArray);
		Point [] testArray = mm.getMenuItemEndPositions();
		assertTrue(Arrays.equals(testArray, pointArray) && testArray[0].equals(pointArray[0]));
	}
	
	@Test 
	public void testMenuItems(){
		MenuModel mm = new MenuModel();
		String [] stringArray = new String [10];
		stringArray[0] = "testString";
		mm.setMenuItems(stringArray);
		String [] testArray = mm.getMenuItems();
		assertTrue(Arrays.equals(testArray, stringArray) && testArray[0].equals(stringArray[0]));
	}
	
	@Test 
	public void testXPos(){
		MenuModel mm = new MenuModel();
		mm.setXPos(10);
		assertTrue(mm.getXPos()==10);
	}
	@Test 
	public void testYPos(){
		MenuModel mm = new MenuModel();
		mm.setYPos(10);
		assertTrue(mm.getYPos()==10);
	}
	@Test 
	public void testGap(){
		MenuModel mm = new MenuModel();
		mm.setGap(10);
		assertTrue(mm.getGap()==10);
	}
}
