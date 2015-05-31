package controller.entities;

import static model.Variables.PPM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import view.entities.EGATimerView;
import lombok.Data;
import model.EGATimer;
/**
 * Controller for the timer. Both for the visual and the model.
 * @author Hampus Rönström
 *
 */
@Data
public class EGATimerController {
	private EGATimer timer;
	private EGATimerView timerView;
	private Body body;
	private PolygonShape shape;
	private FixtureDef fDef;
	/**
	 * Constructor for the EGATimerController. 
	 * @param t, the EGATimer, or the actual timer
	 * @param tv, the EGATimerView, what's seen on the screen
	 */
	public EGATimerController(EGATimer t, EGATimerView tv){
		timer = t;
		timerView = tv;
		
		timer.addObserver(timerView);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
	}
	
	/**
	 * sets the body for the visual timer
	 * @param b, the timers body
	 */
	public void setBody(Body b){
		body = b;
		setFixtureDef(60, 20);
	}
	
	/**
	 * Sets the definitions of the timers fixture.
	 * @param width
	 * @param heigth
	 */
	public void setFixtureDef(float width, float heigth){
		shape = new PolygonShape();
		fDef = new FixtureDef();

		shape.setAsBox(width / PPM, heigth / PPM);
		fDef.shape = shape;
		fDef.isSensor = true;
		
		setSensor(fDef, "timer");
	}
	
	/**
	 * sets the timers position in model, which then notifies the view
	 */
	public void render(){
		timer.setPosition(getPosition().x, getPosition().y);
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	/**
	 * sets the SpriteBatch for the timer. Should be the same as all other controllers.
	 * @param sb, the SpritBatch used
	 */
	public void setSpriteBatch(SpriteBatch sb){
		timerView.setSpriteBatch(sb);
	}
	/**
	 * Sets the sensor for the body. 
	 * 
	 * @param fdef, the FixtureDef that should be applied 
	 * @param userData, the name of the FixtureDef
	 */
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
