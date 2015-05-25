package controller;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import view.GameState;
import view.Level;
import lombok.Data;
import model.EGATimer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import event.EventSupport;




@Data
public class MyContactListener implements ContactListener{
	
	
	private int numFootContacts;
	private Array<Body> bodiesToRemove;
	private Array<Body> keysToRemove;
	private Array<Body> doorsToRemove;
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
		keysToRemove = new Array<Body>();
		doorsToRemove = new Array<Body>();
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
		if(fa.getUserData() != null && fa.getUserData().equals("openDoor")){
			timer.stopTimer();
			EGA.res.getSound("finish").play();
			ega.setLevelFinished(gsm.getCurrentLevel());
		}
		if(fb.getUserData() != null && fb.getUserData().equals("openDoor")){
			timer.stopTimer();
			EGA.res.getSound("finish").play();
			ega.setLevelFinished(gsm.getCurrentLevel());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("lockedDoor")){
			//doorsToRemove.add(fa.getBody());
			if(lvl.getDoorIsOpen() == true){
				bodiesToRemove.add(fa.getBody());
			}

			
		}
		if(fb.getUserData() != null && fb.getUserData().equals("lockedDoor")){
			//doorsToRemove.add(fb.getBody());
			if(lvl.getDoorIsOpen() == true){
				bodiesToRemove.add(fb.getBody());
			}
		
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("spike")){
			contactWithSpike();
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("spike")){
			contactWithSpike();
		}
		
		//private boolean removeKey = false;
		if(fa.getUserData() != null && fa.getUserData().equals("key")){
			
			System.out.println(fa.getBody().getUserData().toString());
			bodiesToRemove.add(fa.getBody());
		
			
		}
		if(fb.getUserData() != null && fb.getUserData().equals("key")){
			
			bodiesToRemove.add(fb.getBody());
		
			//EGA.res.getSound("collectkey").play();
			EGA.res.playSound("collectkey");
			
		}
	}
	
	public void contactWithSpike(){
		//EGA.res.getSound("fail").play();
		EGA.res.playSound("fail");
		gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		//EventSupport.getInstance().fireNewEvent("spikehit");
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