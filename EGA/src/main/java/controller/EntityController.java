package controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import view.EntityView;
import lombok.Data;
import model.EntityModel;
/**
 * 
 * @author Emil Axelsson
 *
 * Abstract controller super-class for all the
 * entities. 
 */

@Data
public abstract class EntityController {
	
	
	private EntityModel theModel;
	private EntityView theView;
	private Body body;
	
	/**
	 * Constructor who has an EntityModel and an EntityView as inputs. 
	 * The model- and view -subclasses extends these two superclasses
	 * 
	 * @param em, the EntityModel
	 * @param ev, the entityView
	 */
	public EntityController(EntityModel em, EntityView ev){
		
		theModel = em;
		theView = ev;
		
		theModel.addObserver(theView);
		
	}
	
	/**
	 * Is called in the level class. Updates the position in model. 
	 */
	public void render(){
		theModel.setPosition(getPosition().x, getPosition().y);
	}
	
	/**
	 * Updates the view
	 * 
	 * @param dt, the update rate
	 */
	public void update(float dt){
		theView.update(dt);
	}
	
	/**
	 * Returns the position of the position of the body
	 * 
	 * @return position of the body
	 */
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	/**
	 * Returns the X value of this bodies position
	 * 
	 * @return X value of the bodies position as a float
	 */
	public float getXPosition(){
		return body.getPosition().x;
	}
	
	/**
	 * Returns the Y value of this bodies position
	 * 
	 * @return Y value of the bodies position as a float
	 */
	public float getYPosition(){
		return body.getPosition().y;
	}
	/**
	 * Sets the spritebatch where the view should be drawing
	 * 
	 * @param sb, the SpriteBatch
	 */
	public void setSpriteBatch(SpriteBatch sb){
		theView.setSpriteBatch(sb);
	};
	
	
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
	
	/**
	 * Removes all the sensors and fixuredefs
	 */
	public void removeSensors(){
		
		body.getFixtureList().clear();
		
	}
}
