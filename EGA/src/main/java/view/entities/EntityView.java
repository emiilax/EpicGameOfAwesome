package view.entities;

import java.util.Observer;

import view.Animation;
import lombok.Data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import controller.Variables;

@Data
public abstract class EntityView implements Observer {
	
	private Animation animation;
	private float width;
	private float height;
	
	private boolean render;

	private float xPosition;
	private float yPosition;
	
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
		render = true;
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
	 * This method is looped in the level class. Moves the 
	 * texture on the screen depending on where you have moved 
	 * the body.
	 * @param sb, where it should draw
	 */
	public void render(){
		if(render){
			sb.begin();
			sb.draw(animation.getFrame(), 
					xPosition * Variables.PPM - width / 2,
					 yPosition * Variables.PPM - height / 2);
			sb.end();
		}
		
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		this.sb = sb;
	}
	
}
