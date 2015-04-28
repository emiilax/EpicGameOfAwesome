package view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import controller.B2DVars;
import controller.EGA;

public class Star implements B2DSprite, IStar{

	private Body body;
	private Animation animation;
	private float width;
	private float height;
	
	public Star(Body body) {
		
		this.body = body;
		animation = new Animation();
		Texture tex = EGA.res.getTexture("star");
		TextureRegion[] sprites = TextureRegion.split(tex,  16,  16)[0];
		
		setAnimation(sprites, 1/ 12f);
		
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt){
		animation.updtate(dt);
		
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(), 
				body.getPosition().x * B2DVars.PPM - width / 2,
				body.getPosition().y * B2DVars.PPM - height / 2);
		sb.end();
	}
	
	public Vector2 getPosition() { return body.getPosition(); }

}
