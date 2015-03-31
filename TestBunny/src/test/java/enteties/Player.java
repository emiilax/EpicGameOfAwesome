package enteties;

import lombok.Data;
import main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

@Data
public class Player extends B2DSprite {
	
	private int numCrystals;
	private int totalCrystals;
	
	public Player(Body body) {
		super(body);
		
		Texture tex = Game.res.getTexture("bunny");
		
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		
		setAnimation(sprites, 1 / 12f);
		
	}
	
	public void collectCrystal() { numCrystals++; }
	
	
}
