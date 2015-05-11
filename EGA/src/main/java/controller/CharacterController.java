package controller;

import static controller.Variables.PPM;
import view.CharacterView;
import lombok.Data;
import model.CharacterModel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

@Data
public class CharacterController {
	
	
	private Body body;
	private PolygonShape shape;
	private FixtureDef fDef;

	private boolean isBig;
	
	private float xVelocity;
	private float yVelocity;
	
	private float currentJumpForce;
	private float currentSpeed;
	
	private float currentWidth;
	private float currentHeigth;
	
	private CharacterModel theModel;
	private CharacterView theView;
	
	
	public CharacterController(CharacterModel chModel, CharacterView chView){
		
		theModel = chModel;
		theView  = chView;
		
		theModel.addObserver(chView);
		
		setIsBig(false);
		
		theView.setTexture(isBig);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
		
		//setFixtureDef(currentWidth, currentHeigth);
		
	}
	
	public CharacterController(CharacterModel chModel, CharacterView chView, Body body){

		theModel = chModel;
		theView  = chView;
		
		theModel.addObserver(chView);
		
		this.body = body;
		
		setIsBig(false);
		
		theView.setTexture(isBig);
		shape = new PolygonShape();
		fDef = new FixtureDef();
		
		setFixtureDef(currentWidth, currentHeigth);
		
		
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		theView.setSpriteBatch(sb);
	};
	
	public void setBody(Body body){

		this.body = body;
		
		Body playerBody = this.getBody();
		playerBody.setLinearVelocity(xVelocity, yVelocity);
		setFixtureDef(currentWidth, currentHeigth);
	}
	
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
	
	public void removeSensors(){
		try{
			this.body.getFixtureList().clear();
		}catch(NullPointerException e){}
	}
	
	public void setSensor(FixtureDef fdef, String userData){
		body.createFixture(fdef).setUserData(userData);
	}
	
	public void setIsBig(boolean truFal){
		isBig = truFal;
		
		
		if(isBig){
			currentWidth = 17.5f;
			currentHeigth = 17.5f;
			
			currentJumpForce = 350;
			currentSpeed = 1.5f;
		} else{
			currentWidth = 10f;
			currentHeigth = 10f;
			
			currentJumpForce = 250;
			currentSpeed = 2f;
		}
	}
	
	public void collectGrowStar() { 
		//Ta bort?
		//numCrystals++; 
		
		setIsBig(true);
		setFixtureDef(currentWidth, currentHeigth);
		
		theView.setTexture(isBig);
		//setAnimation(sprites, 1 / 12f);
	}

	public void collectShrinkStar() { 
		//Ta bort?
		//numCrystals++; 
		setIsBig(false);
		setFixtureDef(currentWidth, currentHeigth);
		
		theView.setTexture(isBig);

		//setTexture("small");
		//setAnimation(sprites, 1 / 12f);
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public void setCurrentVelocity(){
		Body playerBody = this.getBody();
		
		yVelocity = playerBody.getLinearVelocity().y;
		xVelocity = playerBody.getLinearVelocity().x;
	}
	
	public void jump(){
		Body playerBody = this.getBody();
		playerBody.applyForceToCenter(0, currentJumpForce, true);
	}
	
	public void moveForward(){
		
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		
		playerBody.setLinearVelocity(currentSpeed, yVelocity);
		//theModel.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void moveBackward(){
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		playerBody.setLinearVelocity(-currentSpeed, yVelocity);
		//theModel.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void stop(){
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		playerBody.setLinearVelocity(0, yVelocity);
		//theModel.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void render(){
		theModel.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void update(float dt){
		theView.update(dt);
	}
	
}
