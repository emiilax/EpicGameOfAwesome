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

	
	private Texture texSmall = new Texture(Gdx.files.internal("res/characters/redball_small.png"));
	private Texture texBig = new Texture(Gdx.files.internal("res/characters/redball_big.png"));
	
	
	public void setTexture(Boolean isBig){
		if(!isBig){
			Texture tex;
			try{
				tex = EGA.res.getTexture("smallplayer");
			}catch(Exception e){
				tex = texSmall;
			} 
			
			sprites = TextureRegion.split(tex, 20, 20)[0];
			setAnimation(sprites, 1/12f);
		}else {
			//removeSensors();
			//setFixtureDef(17.5f, 17.5f);
			
			Texture tex;
			try{
				tex = EGA.res.getTexture("bigPlayer");
			}catch(Exception e){
				tex = texBig;
			} 
			//Texture tex = EGA.res.getTexture("bigPlayer");
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
