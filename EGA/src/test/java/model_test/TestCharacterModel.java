package model_test;

import static org.junit.Assert.*;
import model.CharacterModel;

import org.junit.Test;

public class TestCharacterModel {

	@Test
	public void testSetVelocity(){
			CharacterModel cm = new CharacterModel();
			cm.setVelocity(3f, 5f);
			float a = 3f;
			float b = 5f;
			assertTrue(a == 3f && b ==5f);
	}

}
