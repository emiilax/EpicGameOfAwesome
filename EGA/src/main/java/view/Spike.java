package view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.EGA;
import controller.Variables;

public class Spike extends Entity{

	public Spike(Body body) {
		super(body);

		FixtureDef fdef = new FixtureDef();
		
		Vector2 v1, v2, v3;
		v1 = new Vector2(0.0f, 0.0f);
		v2 = new Vector2(0.1f, 0.1f);
		v3 = new Vector2(0.2f, 0.0f);

		Vector2[] vertices = {v1, v2, v3};
		
		PolygonShape pshape;
		pshape = new PolygonShape();
		pshape.set(vertices);
		
		fdef.shape = pshape;
		//fdef.isSensor = true;

		fdef.filter.categoryBits = Variables.BIT_SPIKE;
		fdef.filter.maskBits = Variables.BIT_PLAYER;
		
		setSensor(fdef, "spike");
		
		Texture tex = EGA.res.getTexture("spike");
		// is this part necessary?
		TextureRegion[] sprites = TextureRegion.split(tex,  32,  32)[0];
		setAnimation(sprites, 0/ 12f);
	}

}
