package view.entities;

import io.Content;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.entities.CharacterModel;
/**
 * 
 * @author Emil Axelsson
 * 
 * The view class for the Character.
 */
@Data 
@EqualsAndHashCode(callSuper=false)
public class CharacterView extends EntityView{
	
	/** Array of texture regions that will be used in animation class*/
	private TextureRegion[] sprites;
	
	/** Boolean shows whether the character is stopped or not*/
	private boolean isStopped = true;
	
	/**
	 * Sets the texture of the character. Depends on whether its big or small.
	 * @param isBig, boolean whitch tells if the character is big or not
	 */
	public void setTexture(Boolean isBig){
		if(!isBig){
			Texture tex;
			tex = Content.getInstance().getTexture("smallplayer");
			sprites = TextureRegion.split(tex, 30, 30)[0];
			setAnimation(sprites, 1/8f);
		}else {
			Texture tex;
			tex = Content.getInstance().getTexture("bigPlayer");
			sprites = TextureRegion.split(tex, 60, 60)[0];
			setAnimation(sprites, 1/12f); 
		}
	}
	
	/**
	 * This method is called when the model has changed
	 */
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
