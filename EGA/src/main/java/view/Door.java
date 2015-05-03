package view;
import static controller.Variables.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;
import controller.EGA;

public class Door extends Entity {
		
		public Door(Body body) {
			super(body);
			
			FixtureDef fdef = new FixtureDef();
			PolygonShape ps = new PolygonShape();
			ps.setAsBox(25/PPM, 25/PPM);
			//ps.setRadius(8/PPM);

			fdef.shape = ps;
			//fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_DOOR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			
			setSensor(fdef, "bigdoor");
			
			Texture tex = EGA.res.getTexture("bigdoor");
			TextureRegion[] sprites = TextureRegion.split(tex,  50,  50)[0];
			
			setAnimation(sprites, 1/ 12f);
		
			
		}

	}
