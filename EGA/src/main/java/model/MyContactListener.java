package model;

import view.GameState;
import view.Level;
import lombok.Data;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import controller.EGA;
import controller.GameStateManager;
import event.EventSupport;




@Data
public class MyContactListener implements ContactListener{
	
	
	private int numFootContacts;
	private Array<Body> bodiesToRemove;
	private Level lvl;
	private GameStateManager gsm;
	private EGA ega;
	private EGATimer timer;
	
	public MyContactListener(GameState gs){
		super();
		lvl = (Level) gs;
		gsm = gs.getGsm();
		ega = gsm.getGame();
		bodiesToRemove = new Array<Body>();
		timer = EGATimer.getTimer();
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
		
		if(fa.getUserData() != null && fa.getUserData().equals("smallStar")){
			bodiesToRemove.add(fa.getBody());	
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("smallStar")){
			bodiesToRemove.add(fb.getBody());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("bigStar")){
			bodiesToRemove.add(fa.getBody());	
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("bigStar")){
			bodiesToRemove.add(fb.getBody());
		}
		if(fa.getUserData() != null && fa.getUserData().equals("bigdoor")){
			System.out.println("Ball in contact with the door!");
			timer.stopTimer();
			//ega.setLevelFinished(1);
			gsm.getGame().setLevel(new Level(gsm, gsm.getNextLevel()));
		}
		if(fb.getUserData() != null && fb.getUserData().equals("bigdoor")){
			System.out.println("Ball in contact with the door!");
			timer.stopTimer();
			gsm.getGame().setLevel(new Level(gsm, gsm.getNextLevel()));
			//behöver man uppdatera ngt?
			//ega.setLevelFinished(1);
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("spike")){
			contactWithSpike();
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("spike")){
			contactWithSpike();
		}
		if(fa.getUserData() != null && fa.getUserData().equals("key")){
			//fa.getBody().setActive(false);
			//setKey 	
			//lvl.getKey().getBody().setActive(false);
			System.out.print("in contact with key");
		}
		if(fb.getUserData() != null && fb.getUserData().equals("key")){
			//fa.getBody().setActive(false);
			//setKey 
			//lvl.getKey().getBody().setActive(false);
			System.out.print("in contact with key");
		}
	}
	
	public void contactWithSpike(){
		EventSupport.getInstance().fireNewEvent("spikehit");
		//ega = gsm.getGame();
		//ega.setLevel(new Level(gsm));
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
