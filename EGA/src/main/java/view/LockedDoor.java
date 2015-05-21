package view;
import static controller.Variables.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;
import controller.EGA;

/*
 * @author Rebecka Reitmaier
 * Door is a class which extends the class Entity.
 * The class can create two different "doors" - open and closed, NOT NOW
 * the door will be closed if the texture argument is "lockedDoor"
 * the door will be open if the texture argument is "openDoor"
 */

public class LockedDoor extends Entity implements IDoor {
	
		//private boolean doorIsLocked;
		private Texture texture;
		
		public LockedDoor(Body body, String texString) {
			super(body);

			FixtureDef fdef = new FixtureDef();
			PolygonShape ps = new PolygonShape();
			ps.setAsBox(25/PPM, 25/PPM);

			fdef.shape = ps;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_DOOR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fdef, texString);
			
			
			
			
			this.texture = EGA.res.getTexture(texString);
			
			TextureRegion[] sprites = TextureRegion.split(this.texture,  50,  50)[0];
			
			setAnimation(sprites, 1/ 12f);
			
			//setDoorStatus(texString);
			
		}
		/*
		 * This method determine if the door will be open or closed
		 * Will only be nessesary if open and closed door is the same class
		 */
//		private void setDoorStatus(String texture){
//			if (texture.equals("openDoor")){
//				doorIsLocked = false;
//			}
//			if(texture.equals("lockedDoor")){
//				doorIsLocked = true;
//			}
//		}
//		
//		public boolean getDoorisLocked(){
//			return doorIsLocked;
//		}

	}

