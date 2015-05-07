package view;

import lombok.Data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import controller.Variables;

/**
 * 
 * @author 
 * 
 * Abstract class which is the superclass for all
 * the sprites on the map. Character, stars etc;
 */

@Data
public abstract class Entity {
	
	
	private Animation animation;
	private float width;
	private float height;
	private Body body;
	private Array<Fixture> fixtures; 
	
	/**
	 * Constructor for the abstract class. Initiate the body 
	 * and creates an Animation object. 
	 * 
	 * @param body, the body of the object 
	 * 
	 */
	public Entity(Body body){
		this.body = body;
		animation = new Animation();
		fixtures = body.getFixtureList();
	}
	
	/**
	 * Sets the animation.  
	 * @param reg, a list of the textures that will loop
	 * @param delay, how fast you loop the textures
	 */
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	/**
	 * Updates the animation. 
	 * @param dt, 
	 */
	public void update(float dt){
		animation.updtate(dt);
	}
	
	/**
	 * This method is looped in the level class. moves the 
	 * texture on the screen depending on where you have moved 
	 * the body.
	 * @param sb
	 */
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(), 
				body.getPosition().x * Variables.PPM - width / 2,
				body.getPosition().y * Variables.PPM - height / 2);
		sb.end();
	}
	
	/**
	 * 
	 * @return the position of the body
	 */
	public Vector2 getPosition(){ 
		return body.getPosition();
	}
	
	/**
	 * Sets the sensor for the body. 
	 * 
	 * @param fdef, the FixtureDef that should be applyed 
	 * @param userData, the name of the FixtureDef
	 */
	public void setSensor(FixtureDef fdef, String userData){
		
		body.createFixture(fdef).setUserData(userData);
		
		if(userData.equals("foot")){
			System.out.println(body.getFixtureList().size);	
		}
		
	}
	
	public void removeSensors(){
		body.getFixtureList().clear();
		
	}
}
