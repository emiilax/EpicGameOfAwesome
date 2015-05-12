package controller;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import view.EntityView;
import lombok.Data;
import model.EntityModel;

@Data
public abstract class EntityController {
	
	
	private EntityModel theModel;
	private EntityView theView;
	
	private Body body;
	
	public EntityController(EntityModel em, EntityView ev){
		
		theModel = em;
		theView = ev;
		
		theModel.addObserver(theView);
		
	}
	
	
	public void render(){
		theModel.setPosition(body.getPosition().x, body.getPosition().y);
	}
	
	public void update(float dt){
		theView.update(dt);
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
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
	
	public void removeSensors(){
		
		body.getFixtureList().clear();
		
	}
}
