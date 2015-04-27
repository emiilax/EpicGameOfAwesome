package enteties;

import lombok.Data;
import main.EGA;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

@Data
public class Character extends B2DSprite {
	
	private int numCrystals;
	private int totalCrystals;
	private TextureRegion[] stickman;
	
	public Character(Body body) {
		super(body);
		
		//Texture tex = Game.res.getTexture("bunny");
		Texture tex = EGA.res.getTexture("player");
		
		TextureRegion[] sprites = TextureRegion.split(tex, 20, 20)[0];
		stickman = new TextureRegion[8];
		
		for(int i = 0; i < stickman.length; i ++){
			stickman[i] = new TextureRegion(tex, 20 * i, 40, 20, 40); 
		}
		
		setAnimation(sprites, 1 / 12f);
		//setAnimation(stickman, 1 / 12f);
		
	}
	
	public void collectCrystal() { numCrystals++; }
	
	
}
