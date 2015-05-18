package view;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.EGA;
import controller.Variables;
import static controller.Variables.PPM;

public class Spike extends Entity{
	
	private int spikeStyle = 1;

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
		setAnimation(Arrays.copyOfRange(sprites, spikeStyle,  spikeStyle+1), 0/ 12f);
		
	}
	//View
	protected enum spikeOrientation{
		UP, DOWN, RIGHT, LEFT
	}
	
	
	//Controller
	private Vector2[] setSpikeShape(spikeOrientation ori){
		Vector2 v1 = new Vector2(-8f/PPM, -10f/PPM); 
		Vector2 v2 = new Vector2(0.0f/PPM, 10f/PPM);
		Vector2 v3 = new Vector2(8f/PPM, -10f/PPM);
		switch (ori){
		case UP: 
			return spikeVectors(v1, v2, v3, 0);
		case DOWN:
			return spikeVectors(v1, v2, v3, 180);
		case RIGHT:
			return spikeVectors(v1, v2, v3, 270);
		case LEFT:
			return spikeVectors(v1, v2, v3, 90);
		default:
			return spikeVectors(v1, v2, v3, 0);
		}
	}
	
	//Controller
	private Vector2[] spikeVectors(Vector2 v1, Vector2 v2, Vector2 v3, float deg){
		v1.rotate(deg);
		v2.rotate(deg);
		v3.rotate(deg);
		Vector2[] vertices = {v1, v2, v3};
		return vertices;
	}
	
	//view
	private TextureRegion[] getTextureRegion(spikeOrientation ori){
		switch(ori){
		case UP:
			return TextureRegion.split(EGA.res.getTexture("upSpike"),  16,  21)[0];
		case DOWN:
			return TextureRegion.split(EGA.res.getTexture("downSpike"),  16,  21)[0];
		case LEFT:
			return TextureRegion.split(EGA.res.getTexture("leftSpike"),  21,  16)[0];
		case RIGHT:
			return TextureRegion.split(EGA.res.getTexture("rightSpike"),  21,  16)[0];
		default:
			return TextureRegion.split(EGA.res.getTexture("upSpike"),  16,  21)[0];
		}
	}
}
