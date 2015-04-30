package model;

import lombok.Data;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import controller.NamedBody;


@Data
public class MyContactListener implements ContactListener{
	
	
	private int numFootContacts;
	private Array<NamedBody> bodiesToRemove;
	
	public MyContactListener(){
		super();
		bodiesToRemove = new Array<NamedBody>();
	}
	// called when two fixures collides
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("crystal")){
			// remove crystal
			bodiesToRemove.add((NamedBody)fa.getBody());
			
		}
		if(fb.getUserData() != null && fb.getUserData().equals("crystal")){
			bodiesToRemove.add((NamedBody)fb.getBody());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("bigStar")){
			// remove crystal
			bodiesToRemove.add((NamedBody)fa.getBody());
			
		}
		if(fb.getUserData() != null && fb.getUserData().equals("bigStar")){
			bodiesToRemove.add((NamedBody)fb.getBody());
		}
		
		//System.out.println(fa.getUserData() + ", " + fb.getUserData());
	}
	
	// called when two fixures no longer collide
	public void endContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts--;
		}
		
	}
	
	public boolean isPlayerOnGround(){return numFootContacts > 0;}

	public void postSolve(Contact arg0, ContactImpulse arg1) {}

	public void preSolve(Contact arg0, Manifold arg1) {}

}
