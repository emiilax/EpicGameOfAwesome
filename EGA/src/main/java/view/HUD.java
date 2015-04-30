package view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.Variables;
import controller.EGA;

public class HUD {
	
	private Character player;
	
	private TextureRegion[] blocks;
	
	public HUD(Character player){
		this.player = player;
		
		Texture tex = EGA.res.getTexture("hud");
		
		blocks = new TextureRegion[3];
		
		for (int i = 0; i < blocks.length; i ++){
			blocks[i] = new TextureRegion(tex, 32+ i* 16, 0, 16, 16);
			
		}
		
	}
	
	public void render(SpriteBatch sb){
		
		short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;
		
		sb.begin();
		/*
		if((bits & Variables.BIT_RED) != 0){
			sb.draw(blocks[0], 40, 200);
		}
		
		if((bits & Variables.BIT_GREEN) != 0){
			sb.draw(blocks[1], 40, 200);
		}
		
		if((bits & Variables.BIT_BLUE) != 0){
			sb.draw(blocks[2], 40, 200);
		}
		*/
		sb.end();
		
	}
	

}
