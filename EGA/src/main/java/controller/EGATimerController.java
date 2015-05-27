package controller;

import static controller.Variables.PPM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import view.entities.EGATimerView;
import lombok.Data;
import model.EGATimer;

@Data
public class EGATimerController {
	private EGATimer timer;
	private EGATimerView timerView;
	private Body body;
	private PolygonShape shape;
	private FixtureDef fDef;
	
	public EGATimerController(EGATimer t, EGATimerView tv){
		timer = t;
		timerView = tv;
		
		timer.addObserver(timerView);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
	}
	
	public void setBody(Body b){
		body = b;
		setFixtureDef(100, 100);
	}
	
	public void setFixtureDef(float width, float heigth){
		shape = new PolygonShape();
		fDef = new FixtureDef();

		shape.setAsBox(width / PPM, heigth / PPM);
		fDef.shape = shape;
		fDef.isSensor = true;
		
		setSensor(fDef, "timer");
	}
	
	public void render(){
		timer.setPosition(getPosition().x, getPosition().y);
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		timerView.setSpriteBatch(sb);
	}
	
	public void setSensor(FixtureDef fdef, String userData){
		body.createFixture(fdef).setUserData(userData);
	}
	
	/**
	 * Removes all the sensors and fixuredefs
	 */
	public void removeSensors(){
		
		body.getFixtureList().clear();
		
	}
	
	public void setRender(boolean b){
		timerView.setRender(b);
	}

}
