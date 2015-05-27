package view.entities;

import java.util.Observer;

import lombok.Data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import controller.Variables;

/**
 * 
 * @author Emil Axelsson
 *
 *Super class for all the view classes for the entities.
 */
@Data
public abstract class EntityView implements Observer {
	
	/** The animation */
	private Animation animation;
	
	/** The width of every texture region frame */
	private float width;
	
	/** The heigth of every texture region frame */
	private float height;
	
	/** The x-position of the entity body */
	private float xPosition;
	/** The y-position of the entity body */
	private float yPosition;
	
	/** The spritebatch where it should draw */ 
	private SpriteBatch sb;
	
	/**
	 * Constructor for the abstract class. Initiate the body 
	 * and creates an Animation object. 
	 * 
	 * @param body, the body of the object 
	 * 
	 */
	public EntityView(){
		animation = new Animation();
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
		animation.update(dt);
	}
	
	/**
	 * This method is looped in the level class. Moves the 
	 * texture on the screen depending on where you have moved 
	 * the body.
	 * @param sb, where it should draw
	 */
	public void render(){
		sb.begin();
		sb.draw(animation.getFrame(), 
				xPosition * Variables.PPM - width / 2,
				 yPosition * Variables.PPM - height / 2);
		sb.end();
		
		
	}
	
	
}
