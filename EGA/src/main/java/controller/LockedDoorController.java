package controller;

import static controller.Variables.PPM;
import model.EntityModel;
import view.KeyView;
import view.LockedDoorView;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * 
 * @author Rebecka Reitmaier
 *
 */

public class LockedDoorController extends EntityController{

		
		private PolygonShape shape;
		private FixtureDef fDef;
		private LockedDoorView kv;
		private EntityModel em;
		
		public LockedDoorController(EntityModel em, KeyView kv) {
			super(em, kv);
			
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

			FixtureDef fdef = new FixtureDef();
			PolygonShape ps = new PolygonShape();
			ps.setAsBox(25/PPM, 25/PPM);

			fdef.shape = ps;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_DOOR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fdef, "lockedDoor");

		}

}
