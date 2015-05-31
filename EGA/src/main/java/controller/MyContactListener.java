package controller;

import io.Content;
import lombok.Data;
import model.EGATimer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import controller.menus.GameState;
import event.EventSupport;

/**
 * 
 * @author Emil Axelsson
 * 
 * Class that handles the collisions
 * 
 */
@Data
public class MyContactListener implements ContactListener{
	
	/** Used to see if character is on ground */
	private int numFootContacts;
	
	private Array<Body> bodiesToRemove;

	private Array<Body> doorsToRemove;
	private Level lvl;
	private EGA ega;
	private EGATimer timer;
	
	public MyContactListener(GameState gs){
		super();
		lvl = (Level) gs;

		ega = ((Level)gs).getGame();
		bodiesToRemove = new Array<Body>();
		doorsToRemove = new Array<Body>();
		timer = EGATimer.getTimer();
	}
	/**
	 * Called when two fixtures collides
	 */
	public void beginContact(Contact c) {
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("smallStar")){
			bodiesToRemove.add(fa.getBody());	
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("smallStar")){
			bodiesToRemove.add(fb.getBody());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("bigStar")){
			Content.getInstance().playSound("grow");
			bodiesToRemove.add(fa.getBody());	
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("bigStar")){
			Content.getInstance().playSound("grow");
			bodiesToRemove.add(fb.getBody());
		}
		if(fa.getUserData() != null && fa.getUserData().equals("openDoor")){
			timer.stopTimer();
			Content.getInstance().playSound("finish");
			EventSupport.getInstance().fireNewEvent("finish");
		}
		if(fb.getUserData() != null && fb.getUserData().equals("openDoor")){
			timer.stopTimer();
			Content.getInstance().playSound("finish");
			EventSupport.getInstance().fireNewEvent("finish");
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("spike")){
			contactWithSpike();
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("spike")){
			contactWithSpike();
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("key")){
			bodiesToRemove.add(fa.getBody());
			Content.getInstance().playSound("collectkey");
		
			
		}
		if(fb.getUserData() != null && fb.getUserData().equals("key")){
			bodiesToRemove.add(fb.getBody());
			Content.getInstance().playSound("collectkey");
			
		}
	}
	
	/**
	 * Called when in contact with spike
	 */
	public void contactWithSpike(){
		Content.getInstance().playSound("fail");
		EventSupport.getInstance().fireNewEvent("level", 0);
	}
	
	/**
	 *  called when two fixures no longer are in contact
	 */
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
	
	/**
	 * Tells if player is on ground
	 * @return true if on ground
	 */
	public boolean isPlayerOnGround(){return numFootContacts > 0;}
	
	public void postSolve(Contact arg0, ContactImpulse arg1) {}
	public void preSolve(Contact arg0, Manifold arg1) {}

}
