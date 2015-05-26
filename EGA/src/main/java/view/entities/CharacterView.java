package view.entities;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.EGA;
import controller.Variables;
import lombok.Data;
import model.entities.CharacterModel;
import model.entities.EntityModel;

@Data
public class CharacterView extends EntityView{
	
	private TextureRegion[] sprites;
	private boolean isStopped = true;
	public void setTexture(Boolean isBig){
		if(!isBig){
			Texture tex;
			tex = EGA.res.getTexture("smallplayer");
			
			sprites = TextureRegion.split(tex, 40, 80)[0];
			setAnimation(sprites, 1/8f);
		}else {
			Texture tex;
			tex = EGA.res.getTexture("bigPlayer");
			sprites = TextureRegion.split(tex, 60, 60)[0];
			setAnimation(sprites, 1/12f);
		}
	}
	
	
	public void update(Observable o, Object arg) {
		
		if(isStopped){
			super.getAnimation().setCurrentFrame(0);
		}
		
		if(o instanceof CharacterModel){		
			CharacterModel cm = (CharacterModel)o;
			setXPosition(cm.getXPosition());
			setYPosition(cm.getYPosition());
			render();
		}
	}
	
	
	
}
