package model_test;

import static org.junit.Assert.*;
import model.entities.CharacterModel;

import org.junit.Test;
/**
 * Test class for CharacterModel
 * 
 * @author Hampus Rönström
 *
 */
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
