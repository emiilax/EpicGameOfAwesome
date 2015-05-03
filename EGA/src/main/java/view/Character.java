package view;

import lombok.Data;
import model.MyContactListener;
import model.MyInput;
import static controller.Variables.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;
import controller.EGA;

@Data
public class Character extends Entity {

	//private Body body;
	//private Animation animation;
	//private float width;
	//private float height;
	private int numCrystals;
	private int totalCrystals;
	private TextureRegion[] stickman;
	private TextureRegion[] sprites;

	private PolygonShape shape;
	private FixtureDef fDef;

	private boolean isBig = false;
	
	private float xVelocity;
	private float yVelocity;
	
	
	
	public Character(Body body) {
		super(body);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();

		setTexture("small");

		setAnimation(sprites, 1 / 12f);

		
	}

	

	private void setTexture(String size){

		if(size.equals("small")){
			
			removeSensors();

			setFixtureDef(10, 10);

			Texture tex = EGA.res.getTexture("smallplayer");
			sprites = TextureRegion.split(tex, 20, 20)[0];
			isBig = false;

		}else {
			removeSensors();
			setFixtureDef(17.5f, 17.5f);

			Texture tex = EGA.res.getTexture("bigPlayer");
			sprites = TextureRegion.split(tex, 35, 35)[0];
			isBig = true;

		}
	}

	public void setFixtureDef(float width, float heigth){
		shape = new PolygonShape();
		fDef = new FixtureDef();

		shape.setAsBox(width / PPM, heigth / PPM);
		fDef.shape = shape;
		fDef.filter.categoryBits = Variables.BIT_PLAYER;
<<<<<<< HEAD
		fDef.filter.maskBits = Variables.BIT_GROUND | Variables.BIT_PLATFORM | Variables.BIT_STAR;

=======
		fDef.filter.maskBits = Variables.BIT_GROUND | Variables.BIT_PLATFORM | Variables.BIT_STAR 
				| Variables.BIT_SPIKE;
		
>>>>>>> spikes
		setSensor(fDef, "player");

		shape.setAsBox( width/PPM,  1 / PPM, new Vector2(0, -heigth/ PPM), 0);
		fDef.filter.categoryBits = Variables.BIT_PLAYER;
		fDef.filter.maskBits = Variables.BIT_GROUND | Variables.BIT_PLATFORM;
		fDef.isSensor = true;

		setSensor(fDef, "foot");
	}

	public void collectGrowStar() { 
		//Ta bort?
		numCrystals++; 

		setTexture("big");
		setAnimation(sprites, 1 / 12f);
	}

	public void collectShrinkStar() { 
		//Ta bort?
		numCrystals++; 

		setTexture("small");
		setAnimation(sprites, 1 / 12f);
	}
	
	public float getRadius(){
		return fDef.shape.getRadius();
	}
	
	@Override
	public void setBody(Body body){
		super.setBody(body);
		Body playerBody = this.getBody();
		playerBody.setLinearVelocity(xVelocity, yVelocity);
	}

	/**
	 * Used to set the current speed in both directions. They will 
	 * be used when stting new body.
	 */

	public void setCurrentVelocity(){
		Body playerBody = this.getBody();
		
		yVelocity = playerBody.getLinearVelocity().y;
		xVelocity = playerBody.getLinearVelocity().x;
	}
	
	public void handleInput(MyContactListener cl) {
		Body playerBody = this.getBody();
		yVelocity = playerBody.getLinearVelocity().y;
		int force;
		float speed;
		
		if(isBig){
			force = 350;
			speed = 1f;
		} else {
			force = 250;
			speed = 2f;
		}
		
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			if(cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(0, force, true);
			}
		}

		if(MyInput.isDown(MyInput.BUTTON_FORWARD)){

			playerBody.setLinearVelocity(speed, yVelocity);

		}else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			playerBody.setLinearVelocity(-speed, yVelocity);

		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			playerBody.setLinearVelocity(0, yVelocity);

		}
	}
}
