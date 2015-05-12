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

public class CharacterView extends EntityView implements Observer{
	
	private TextureRegion[] sprites;
	/*
	private Animation animation;
	private float width;
	private float height;
	
	private float xPosition;
	private float yPosition;
	
	private SpriteBatch sb;
	*/
	private Texture texSmall = new Texture(Gdx.files.internal("res/characters/redball_small.png"));
	private Texture texBig = new Texture(Gdx.files.internal("res/characters/redball_big.png"));
	
	/*
	public CharacterView(){
		animation = new Animation();
	}
	
	
	public CharacterView(SpriteBatch sb){
		this.sb = sb;
		animation = new Animation();
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		this.sb = sb;
	}
	*/
	
	public void setTexture(Boolean isBig){
		if(!isBig){
			Texture tex;
			try{
				tex = texSmall;
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
	
	/*
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt){
		animation.updtate(dt);
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(), 
				xPosition * Variables.PPM - width / 2,
				yPosition * Variables.PPM - height / 2);
		sb.end();
	}
	*/
	
	public void update(Observable o, Object arg) {
		
		if(o instanceof CharacterModel){
			CharacterModel cm = (CharacterModel)o;
			
			super.setXPosition(cm.getXPosition());
			super.setYPosition(cm.getYPosition());
			render();
		}
	}
	
	
	
}
