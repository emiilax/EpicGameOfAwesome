package view;
import static controller.Variables.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;
import controller.EGA;

public class Key extends Entity{
	
/*
 * @author Rebecka Reitmaier
 */
	
		private boolean keyIsTaken = false;
		
		public Key(Body body) {
			super(body);
			
			FixtureDef fdef = new FixtureDef();
			PolygonShape ps = new PolygonShape();
			ps.setAsBox(25/PPM, 25/PPM);
			//ps.setRadius(8/PPM);

			fdef.shape = ps;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_KEY;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fdef, "key");
			
			Texture tex = EGA.res.getTexture("key"); //view 
			TextureRegion[] sprites = TextureRegion.split(tex,  50,  50)[0];//view 
			
			setAnimation(sprites, 1/ 12f);
			
		}
		public boolean getKeyIsTaken(){
			return keyIsTaken;
		}
		
		public void setKeyIsTaken(boolean b){
			keyIsTaken = b;
		}

	}