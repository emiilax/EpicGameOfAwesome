package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;



public class MyContactListener implements ContactListener{
	
	private boolean playerOnGround;
	// called when two fixures collides
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			playerOnGround = true;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			playerOnGround = true;
		}
		//System.out.println(fa.getUserData() + ", " + fb.getUserData());
	}
	
	// called when two fixures no longer collide
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			playerOnGround = false;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			playerOnGround = false;
		}
		
	}
	
	public boolean isPlayerOnGround(){return playerOnGround;}

	public void postSolve(Contact arg0, ContactImpulse arg1) {}

	public void preSolve(Contact arg0, Manifold arg1) {}

}
