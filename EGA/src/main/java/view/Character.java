package view;

import lombok.Data;
import static controller.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.B2DVars;
import controller.EGA;

@Data
public class Character implements B2DSprite {
	
	private Body body;
	private Animation animation;
	private float width;
	private float height;
	private int numCrystals;
	private int totalCrystals;
	private TextureRegion[] stickman;
	private TextureRegion[] sprites;
	
	private PolygonShape shape;
	private FixtureDef fDef;
	
	public Character(Body body) {
		this.body = body;
		animation = new Animation();
		shape = new PolygonShape();
		fDef = new FixtureDef();
		
		shape.setAsBox(10 / PPM, 9 / PPM);
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_GREEN | B2DVars.BIT_BLUE | B2DVars.BIT_CRYSTAL;
		//fDef.restitution = 0.5f;
		body.createFixture(fDef).setUserData("player");
		
		shape.setAsBox( 10/PPM,  1 / PPM, new Vector2(0, -10/ PPM), 0);
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_GREEN | B2DVars.BIT_BLUE;
		fDef.isSensor = true;
		body.createFixture(fDef).setUserData("foot");
		
		
		
		setTexture("small");
		setAnimation(sprites, 1 / 12f);
		//setAnimation(stickman, 1 / 12f);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt){
		animation.updtate(dt);
		
	}
	
	private void setTexture(String size){
		
		if(size.equals("small")){
			//Texture tex = Game.res.getTexture("bunny");
			Texture tex = EGA.res.getTexture("smallplayer");
			sprites = TextureRegion.split(tex, 20, 20)[0];
			
		}else {

			Texture tex = EGA.res.getTexture("bigPlayer");
			
			sprites = TextureRegion.split(tex, 35, 35)[0];
			
		}
		
	}
	
	public void collectGrowStar() { 
		//Ta bort?
		numCrystals++; 
		
		setTexture("big");
		setAnimation(sprites, 1 / 12f);
		System.out.println("test");
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
