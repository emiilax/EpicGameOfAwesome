package controller.entities;

import static model.Variables.PPM;
import model.Variables;
import model.entities.EntityModel;
import view.entities.DoorView;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * 
 * @author Rebecka Reitmaier
 *
 */

public class DoorController extends EntityController{

		private PolygonShape shape;
		private FixtureDef fDef;
		private EntityModel em;
		private DoorView dv;
		
		/**
		 * This contructor calls the superclasses and initilizes it.
		 * It also sets the shape and fDef.
		 * 
		 * @param em
		 * @param ldv
		 */
		public DoorController(EntityModel em, DoorView dv) {
			super(em, dv);
			
			this.dv = dv;
			shape = new PolygonShape();
			fDef = new FixtureDef();
			
			this.em = em;
			
		}	
		
		@Override
		public void update(float dt){
			dv.update(dt);
			super.update(dt);
		}
		
		
		/**
		 * Sets the body of the door.
		 */
		@Override
		public void setBody(Body body){
			super.setBody(body);
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
			
			setDoorSensor();
		}
	/**
	 * Sets the door sensor.
	 */
	public void setDoorSensor(){
		
		if(dv.getDoorIsLocked()){
			setSensor(fDef, "lockedDoor"); 
		}
		else{
			setSensor(fDef, "openDoor");
		}
	}
	
	/**
	 * Sets door, if its locked or not.
	 * 
	 * @param b, Boolean
	 */
	public void setDoorIsLocked(Boolean b){
		dv.setDoorIsLocked(b);
		setFixtureDef();
	}
}
