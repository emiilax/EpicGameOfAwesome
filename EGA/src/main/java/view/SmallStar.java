package view;

import static controller.Variables.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import controller.Variables;
import controller.EGA;

public class SmallStar extends Entity implements  IStar{
	
	public SmallStar(Body body) {
		super(body);
		
		
		FixtureDef fdef = new FixtureDef();
		CircleShape cshape = new CircleShape();
		cshape.setRadius(8 / PPM);

		fdef.shape = cshape;
		fdef.isSensor = true;

		fdef.filter.categoryBits = Variables.BIT_STAR;
		fdef.filter.maskBits = Variables.BIT_PLAYER;
		
		setSensor(fdef, "smallStar");
		
		Texture tex = EGA.res.getTexture("star");
		TextureRegion[] sprites = TextureRegion.split(tex,  16,  16)[0];
		
		setAnimation(sprites, 1/ 12f);
		
	}
}
