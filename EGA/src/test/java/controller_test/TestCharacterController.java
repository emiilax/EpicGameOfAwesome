package controller_test;

import static org.junit.Assert.*;
import model.CharacterModel;

import org.junit.Test;

import view.CharacterView;
import controller.CharacterController;
/**
 * Test class for CharacterController
 * 
 * @author Hampus Rönström
 *
 */
public class TestCharacterController {

	@Test
	public void testMoveForwardAndBackward() {
		
		//This does not work atm
		
		CharacterController cc = new CharacterController(new CharacterModel(), new CharacterView());
		float xPosB4 = cc.getXPosition();
		
		cc.moveForward();
		
		float xPosAfter = cc.getXPosition();
		
		assertTrue(xPosB4 < xPosAfter);
		
		
	}

}
