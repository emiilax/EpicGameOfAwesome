package controller.entities;

import static model.Variables.PPM;
import io.Content;
import view.entities.CharacterView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.Variables;
import model.entities.CharacterModel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * The controller class for the Character. Extends EntityController 
 * 
 * @author Emil Axelsson
 *
 * 
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CharacterController extends EntityController{

	private PolygonShape shape;
	
	private FixtureDef fDef;
	
	/** A variable who tells whether the character is big or not */
	private boolean isBig;

	/** Variable who holds the velocity in the x direction */
	private float xVelocity;
	
	/** Variable who holds the velocity in the x direction */
	private float yVelocity;
	
	/** Variable for the current jumpforce, depends on whether its big or not*/
	private float currentJumpForce;
	
	/** Variable for the current speed, depends on whether its big or not*/
	private float currentSpeed;
	
	/** Variable for the current width of the character, depends on whether its big or not*/
	private float currentWidth;
	
	/** Variable for the current heigth of the character, depends on whether its big or not*/
	private float currentHeigth;
	
	/**
	 * Constructor who has a CharacterModel and CharacterView as inputs
	 * @param chModel, the CharacterModel
	 * @param chView, the CharacterView
	 */
	public CharacterController(CharacterModel chModel, CharacterView chView){
		
		super(chModel, chView);

		setIsBig(false);
		
		((CharacterView)getTheView()).setTexture(isBig);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
	
	}

	/**
	 * Sets the body of the character. The current velocity 
	 * is also applied to it
	 */
	@Override
	public void setBody(Body body){

		super.setBody(body);
		
		Body playerBody = getBody();
		
		playerBody.setLinearVelocity(xVelocity, yVelocity);
		setFixtureDef(currentWidth, currentHeigth);
	}
	
	/**
	 * Sets the hitbox for the player.
	 * @param width, the width of the hitbox
	 * @param heigth, the heigth of the hitbox
	 */
	public void setFixtureDef(float width, float heigth){
		shape = new PolygonShape();
		fDef = new FixtureDef();

		shape.setAsBox(width / PPM, heigth / PPM);
		fDef.shape = shape;
		fDef.filter.categoryBits = Variables.BIT_PLAYER;
		fDef.filter.maskBits = Variables.BIT_GROUND | Variables.BIT_PLATFORM | Variables.BIT_STAR 
				| Variables.BIT_SPIKE | Variables.BIT_DOOR | Variables.BIT_KEY;
		
		setSensor(fDef, "player");

		shape.setAsBox( (width-1)/PPM,  1 / PPM, new Vector2(0, -heigth/ PPM), 0);
		fDef.filter.categoryBits = Variables.BIT_PLAYER;
		fDef.filter.maskBits = Variables.BIT_GROUND | Variables.BIT_PLATFORM;
		fDef.isSensor = true;

		setSensor(fDef, "foot");
	}
	
	/**
	 * Sets the variables depending on if character is big or small
	 */
	public void setIsBig(boolean truFal){
		isBig = truFal;
		
		
		if(isBig){
			currentWidth = 18;
			currentHeigth = 25f;
			
			currentJumpForce = 350;
			currentSpeed = 1.3f;
		} else{
			currentWidth = 8f;
			currentHeigth = 13f;
			
			currentJumpForce = 250;
			currentSpeed = 2f;
		}
	}
	
	/**
	 * Method called when character collides with big-star
	 */
	public void collectGrowStar() { 
	
		setIsBig(true);
		setFixtureDef(currentWidth, currentHeigth);
		Content.getInstance().playSound("grow");
		
		((CharacterView)getTheView()).setTexture(isBig);
	}
	
	/**
	 * Method called when character collides with small-star
	 */
	public void collectShrinkStar() { 
		setIsBig(false);
		setFixtureDef(currentWidth, currentHeigth);
		Content.getInstance().playSound("shrink");
		
		((CharacterView)getTheView()).setTexture(isBig);

	}
	
	/**
	 * Sets current speed of Character-body
	 */
	public void setCurrentVelocity(){
		Body playerBody = this.getBody();
		
		yVelocity = playerBody.getLinearVelocity().y;
		xVelocity = playerBody.getLinearVelocity().x;
	}
	
	/**
	 * Makes the Character-body jump
	 */
	public void jump(){
		Content.getInstance().playSound("jump");
		Body playerBody = this.getBody();
		playerBody.applyForceToCenter(0, currentJumpForce, true);
	}
	
	/**
	 * Moves the Character-body forward
	 */
	int i = 1;
	public void moveForward(){	
		((CharacterView)super.getTheView()).setStopped(false);
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		
		playerBody.setLinearVelocity(currentSpeed, yVelocity);
		

	}
	
	/**
	 * Moves the Character-body backwards
	 */
	public void moveBackward(){
		((CharacterView)super.getTheView()).setStopped(false);
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		playerBody.setLinearVelocity(-currentSpeed, yVelocity);

	}
	
	/**
	 * Stops the Character-body from moving
	 */
	public void stop(){
		((CharacterView)super.getTheView()).setStopped(true);
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		playerBody.setLinearVelocity(0, yVelocity);
	}

}
