package model_test;

import static org.junit.Assert.*;
import model.MyInput;

import org.junit.After;
import org.junit.Test;

public class TestMyInput {

	@Test
	public void testSetKey() {
		int key = 2;
		MyInput.setKey(key, true);
		assertTrue(MyInput.isDown(key) == true);
		assertTrue(MyInput.isPressed(key) == true);
	}

	@After
	public void afterTest(){
		MyInput.setAllKeysFalse();
	}
}
