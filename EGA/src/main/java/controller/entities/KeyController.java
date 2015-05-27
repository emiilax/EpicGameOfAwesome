package controller.entities;

import static model.Variables.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import model.Variables;
import model.entities.EntityModel;
import view.entities.EntityView;
import view.entities.KeyView;

/**
 * 
 * @author Rebecka Reitmaier
 *	
 * KeyController, a controller for the key.
 */

public class KeyController extends EntityController{
	
	private PolygonShape shape;
	private FixtureDef fDef;
	private boolean keyIsTaken;
	private KeyView kv;
	private EntityModel em;
	
	/**
	 * This contructor calls the superclasses and initilizes it.
	 * It also sets the shape and fDef.
	 * 
	 * @param em
	 * @param kv
	 */
	public KeyController(EntityModel em, KeyView kv) {
		super(em, kv);
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
		
		this.em = em;
		
	}	
	/**
	 * Sets the body of the Key. 
	 */
	@Override
	public void setBody(Body body){
		super.setBody(body);
		setFixtureDef();
		em.setPosition(body.getPosition().x, body.getPosition().y);

		
	}
	
	/**
	 * Sets the hitbox for the key.
	 */
	public void setFixtureDef(){

		shape.setAsBox(25/PPM, 25/PPM);

		fDef.shape = shape;
		fDef.isSensor = true;

		fDef.filter.categoryBits = Variables.BIT_KEY;
		fDef.filter.maskBits = Variables.BIT_PLAYER;
		
		setSensor(fDef, "key");

	}
	public boolean getKeyIsTaken(){
		return keyIsTaken;
	}
	
	public void setKeyIsTaken(boolean b){
		keyIsTaken = b;
	}

}
