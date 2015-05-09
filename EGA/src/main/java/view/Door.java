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
 * 
 */

public class Door extends Entity {
	
		private boolean doorIsLocked;
		private Texture tex;
		
		public Door(Body body, String texture) {
			super(body);
			
			FixtureDef fdef = new FixtureDef();
			PolygonShape ps = new PolygonShape();
			ps.setAsBox(25/PPM, 25/PPM);
			//ps.setRadius(8/PPM);

			fdef.shape = ps;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_DOOR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fdef, "bigdoor");
			setTexture(texture);
			//Texture tex = EGA.res.getTexture("bigdoor");
			TextureRegion[] sprites = TextureRegion.split(this.tex,  50,  50)[0];
			
			setAnimation(sprites, 1/ 12f);
			
		}
		public void setTexture(String texture){
			this.tex = EGA.res.getTexture(texture);
			if (texture.equals("bigdoor")){
				doorIsLocked = false;
			}
			if(texture.equals("lockedDoor")){
				doorIsLocked = true;
			}
		}
		
		public void setDoorIsLocked(boolean b){
			doorIsLocked = b;
		}
		public boolean getDoorisLocked(){
			return doorIsLocked;
		}

	}
