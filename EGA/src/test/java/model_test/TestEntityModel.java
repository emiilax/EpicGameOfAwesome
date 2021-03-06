package model_test;

import static org.junit.Assert.*;
import model.entities.EntityModel;

import org.junit.Test;
/**
 * Test class for EntityModel
 * 
 * @author Hampus Rönström
 *
 */
public class TestEntityModel {
	
	@Test
	public void testSetPosition(){
		EntityModel em = new EntityModel();
		em.setPosition(3f, 4.5f);
		float a = em.getXPosition();
		float b = em.getYPosition();
		assertTrue(a == 3f && b == 4.5f);	
	}

}
