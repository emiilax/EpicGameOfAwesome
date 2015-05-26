package model_test;

import static org.junit.Assert.*;
import model.MenuModel;
import model.entities.EntityModel;

import org.junit.Test;

public class TestMenuModel {

	@Test
	public void testTitle() {
		MenuModel mm = new MenuModel();
		mm.setTitle("Test Title");
		String s = "Test Title";
		assertTrue(s.equals(mm.getTitle()));
	}
	
//	@Test
//	public void testSetPosition(){
//		EntityModel em = new EntityModel();
//		em.setPosition(3f, 4.5f);
//		float a = em.getXPosition();
//		float b = em.getYPosition();
//		assertTrue(a == 3f && b == 4.5f);	
//	}

}
