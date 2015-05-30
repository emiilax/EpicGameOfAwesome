package model_test;

import static org.junit.Assert.*;
import model.MyInput;

import org.junit.Before;
import org.junit.Test;

public class TestMyInput {

	@Before
	public void beforeTest(){
		MyInput.setAllKeysFalse();
	}
	
	@Test
	public void testSetKey() {
		int key = 2;
		MyInput.setKey(key, true);
		assertTrue(MyInput.keys[key] == true);
	}
	
	@Test
	public void testIsDown(){
		int key = 2;
		MyInput.setKey(key, true);
		assertTrue(MyInput.isDown(key) == true);
	}
	
	@Test
	public void testIsPressed(){
		int key = 4;
		MyInput.setKey(key, true);
		assertTrue(MyInput.isPressed(key) == true);
		MyInput.update();
		assertTrue(MyInput.isPressed(key) == false);
		MyInput.setKey(key, false);
		assertTrue(MyInput.isPressed(key) == false);
	}
	
	@Test
	public void testUpdateKeys(){
		int key = 3;
		MyInput.setKey(key, true);
		assertTrue(MyInput.isPressed(3) == true);
		
		MyInput.update();
		MyInput.setKey(key, false);
		assertTrue(MyInput.pkeys[3] == true);
	}

	
	
}
