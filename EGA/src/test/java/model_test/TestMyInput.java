package model_test;

import static org.junit.Assert.*;
import model.MyInput;

import org.junit.Test;

public class TestMyInput {

	@Test
	public void testSetKey() {
		int key = 2;
		MyInput.setKey(key, true);
		assertTrue(MyInput.isDown(key) == true);
		assertTrue(MyInput.isPressed(key) == true);
	}
	
	@Test
	public void someTest(){
		int key = 4;
		MyInput.setKey(key, true);
		assertTrue(MyInput);
	}

}
