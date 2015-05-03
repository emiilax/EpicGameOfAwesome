package view_test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import view.Character;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import controller.DesktopLauncher;

public class CharacterTest {
	
	@Test
	public void testSetBody() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFixtureDef() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCollectGrowStar() {
		World w = new World(new Vector2(), true);
		BodyDef bdef = new BodyDef();
		
		Body body = w.createBody(bdef);
		
		Character player = new Character(body);
		
		float oldRadius = player.getRadius();
		
		player.collectGrowStar();
		
		float newRadius = player.getRadius();
		
		assertTrue(oldRadius < newRadius);
	}

	@Test
	public void testCollectShrinkStar() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetBig() {
		fail("Not yet implemented");
	}
	

}
