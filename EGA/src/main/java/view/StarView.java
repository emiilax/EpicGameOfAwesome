package view;

import java.util.Observable;

import view.entities.EntityView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Data;
import model.entities.EntityModel;
import controller.EGA;
import controller.SpikeController.spikeOrientation;

@Data
public class StarView extends EntityView {

	private boolean isBig;

	public StarView(boolean isBig){
		super();
		this.isBig = isBig;
		setTexture();
	}

	private void setTexture(){
		Texture tex;
		TextureRegion[] sprites;
		if(isBig){
			tex = EGA.res.getTexture("bigStar");
			sprites = TextureRegion.split(tex,  40,  42)[0];
		} else {
			tex = EGA.res.getTexture("star");
			sprites = TextureRegion.split(tex,  18,  17)[0];
		}
		setAnimation(sprites, 1/ 12f);
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
