package enteties;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import controller.EGA;

public class Star extends B2DSprite{

	public Star(Body body) {
		super(body);
		
		Texture tex = EGA.res.getTexture("star");
		TextureRegion[] sprites = TextureRegion.split(tex,  16,  16)[0];
		
		setAnimation(sprites, 1/ 12f);
	}

}
