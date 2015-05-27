package controller.entities;

import static controller.Variables.PPM;
import model.entities.EntityModel;
import view.entities.OpenDoorView;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;

/**
 * 
 * @author Rebecka Reitmaier
 *
 */

public class OpenDoorController extends EntityController{

		
		private PolygonShape shape;
		private FixtureDef fDef;
		private EntityModel em;
		
		/**
		 * This contructor calls the superclasses and initilizes it.
		 * It also sets the shape and fDef.
		 * 
		 * @param em
		 * @param odv
		 */
		public OpenDoorController(EntityModel em, OpenDoorView odv) {
			super(em, odv);
			
			shape = new PolygonShape();
			fDef = new FixtureDef();
			
			this.em = em;
			
		}	
		/**
		 * Sets the body of the door.
		 */
		@Override
		public void setBody(Body body){
			super.setBody(body);
			setFixtureDef();
			em.setPosition(body.getPosition().x, body.getPosition().y);

			
		}
		
		/**
		 * Sets the hitbox for the door.
		 */
		public void setFixtureDef(){

			fDef = new FixtureDef();
			shape = new PolygonShape();
			shape.setAsBox(25/PPM, 25/PPM);

			fDef.shape = shape;
			fDef.isSensor = true;

			fDef.filter.categoryBits = Variables.BIT_DOOR;
			fDef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fDef, "openDoor");

		}

}
