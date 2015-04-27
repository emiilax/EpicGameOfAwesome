package enteties;

import lombok.Data;
import static handlers.B2DVars.PPM;
import main.EGA;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

@Data
public class Character extends B2DSprite {
	
	private int numCrystals;
	private int totalCrystals;
	private TextureRegion[] stickman;
	private TextureRegion[] sprites;
	
	public Character(Body body) {
		super(body);
		
		setTexture("small");
		
		setAnimation(sprites, 1 / 12f);
		//setAnimation(stickman, 1 / 12f);
		
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
	
	
}
