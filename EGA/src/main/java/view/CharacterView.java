package view;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.EGA;
import controller.Variables;
import model.CharacterModel;
import model.EntityModel;

public class CharacterView extends EntityView{
	
	private TextureRegion[] sprites;
	
	public void setTexture(Boolean isBig){
		if(!isBig){
			Texture tex;
			tex = EGA.res.getTexture("smallplayer");
			
			sprites = TextureRegion.split(tex, 20, 20)[0];
			setAnimation(sprites, 1/12f);
		}else {
			Texture tex;
			tex = EGA.res.getTexture("bigPlayer");
			sprites = TextureRegion.split(tex, 35, 35)[0];
			setAnimation(sprites, 1/12f);
		}
	}
	
	
	public void update(Observable o, Object arg) {
		
		if(o instanceof CharacterModel){		
			CharacterModel cm = (CharacterModel)o;
			setXPosition(cm.getXPosition());
			setYPosition(cm.getYPosition());
			render();
		}
	}
	
	
	
}
