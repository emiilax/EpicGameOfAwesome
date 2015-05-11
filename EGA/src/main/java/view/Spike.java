package view;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.EGA;
import controller.Variables;
import static controller.Variables.PPM;

public class Spike extends Entity{

	public Spike(Body body) {
		this(body, spikeOrientation.UP);
	}
	
	public Spike(Body body, spikeOrientation ori){
		super(body);

		FixtureDef fdef = new FixtureDef();

		Vector2[] vertices = setSpikeShape(ori);
		
		PolygonShape pshape;
		pshape = new PolygonShape();
		pshape.set(vertices);
		
		fdef.shape = pshape;

		fdef.filter.categoryBits = Variables.BIT_SPIKE;
		fdef.filter.maskBits = Variables.BIT_PLAYER;
		
		setSensor(fdef, "spike");
		
		TextureRegion[] sprites = getTextureRegion(ori);
		//Range is which sprite to use
		setAnimation(Arrays.copyOfRange(sprites,  1,  2), 0/ 12f);
		
	}

	protected enum spikeOrientation{
		UP, DOWN, RIGHT, LEFT
	}
	
	private Vector2[] setSpikeShape(spikeOrientation ori){
		switch (ori){
		case UP: 
			return spikeVectors(new Vector2(-8f/PPM, -10f/PPM), 
					new Vector2(0.0f/PPM, 10f/PPM), new Vector2(8f/PPM, -10f/PPM));
		case DOWN:
			return spikeVectors(new Vector2(-8f/PPM, -10f/PPM), 
					new Vector2(0.0f/PPM, -20f/PPM), new Vector2(8f/PPM, -10f/PPM));
		case RIGHT:
			return spikeVectors(new Vector2(-8f/PPM, -10f/PPM), 
					new Vector2(0.0f/PPM, 10f/PPM), new Vector2(8f/PPM, -10f/PPM));
		case LEFT:
			return spikeVectors(new Vector2(-8f/PPM, -10f/PPM), 
					new Vector2(0.0f/PPM, 10f/PPM), new Vector2(8f/PPM, -10f/PPM));
		default:
			return spikeVectors(new Vector2(-8f/PPM, -10f/PPM), 
					new Vector2(0.0f/PPM, 10f/PPM), new Vector2(8f/PPM, -10f/PPM));
		}
	}
	
	private Vector2[] spikeVectors(Vector2 v1, Vector2 v2, Vector2 v3){
		Vector2[] vertices = {v1, v2, v3};
		return vertices;
	}
	
	private TextureRegion[] getTextureRegion(spikeOrientation ori){
		switch(ori){
		case UP:
			return TextureRegion.split(EGA.res.getTexture("spike"),  16,  21)[0];
		case DOWN:
			return TextureRegion.split(EGA.res.getTexture("spike"),  16,  21)[0];
		case LEFT:
			return TextureRegion.split(EGA.res.getTexture("spike"),  16,  21)[0];
		case RIGHT:
			return TextureRegion.split(EGA.res.getTexture("spike"),  16,  21)[0];
		default:
			return TextureRegion.split(EGA.res.getTexture("spike"),  16,  21)[0];
		}
	}
	
}
