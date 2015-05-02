package org.gradle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import view.Character;
import controller.EGA;

public class TestCharacter {

	@Test
	public void testGrowMethod() {
		World w = new World(new Vector2(), true);
		BodyDef bdef = new BodyDef();
		
		Body body = w.createBody(bdef);
		
		Character player = new Character(body);
		
		float oldRadius = player.getRadius();
		
		player.collectGrowStar();
		
		float newRadius = player.getRadius();
		
		assertTrue(oldRadius < newRadius);
		
	}
	
	

}
