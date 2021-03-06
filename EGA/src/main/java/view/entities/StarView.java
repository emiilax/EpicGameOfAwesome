package view.entities;

import io.Content;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.entities.EntityModel;

/**
 * The view class for stars. 
 * @author Erik
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class StarView extends EntityView {

	private boolean isBig;

	public StarView(boolean isBig){
		super();
		this.isBig = isBig;
		setTexture();
	}
	
	/**
	 * Sets the texture depending on if the star
	 * is big or small. 
	 * @author Erik 
	 */
	private void setTexture(){
		Texture tex;
		TextureRegion[] sprites;
		if(isBig){
			tex = Content.getInstance().getTexture("bigStar");
			sprites = TextureRegion.split(tex,  42,  42)[0];
		} else {
			tex = Content.getInstance().getTexture("star");
			sprites = TextureRegion.split(tex,  18,  17)[0];
		}
		setAnimation(sprites, 1/ 6f);
	}
	
	public void update(Observable o, Object arg) {
		if(o instanceof EntityModel){		
			EntityModel em = (EntityModel)o;
			setXPosition(em.getXPosition());
			setYPosition(em.getYPosition());
			render();
		}
	}

}
