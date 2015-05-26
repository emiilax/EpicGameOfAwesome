package view;

import java.util.Arrays;
import java.util.Observable;

import view.entities.EntityView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.EGA;
import lombok.Data;
import model.entities.EntityModel;
import model.entities.SpikeModel.spikeOrientation;

@Data
public class SpikeView extends EntityView {
	
	private int spikeStyle = 1;
	
	
	public void setTexture(spikeOrientation ori){
		TextureRegion[] sprites = getTextureRegion(ori);
		
		//Range is which sprite to use
		setAnimation(Arrays.copyOfRange(sprites, spikeStyle,  spikeStyle+1), 0/ 12f);
		
	}
	
	private TextureRegion[] getTextureRegion(spikeOrientation ori){
		switch(ori){
		case UP:
			return TextureRegion.split(EGA.res.getTexture("upSpike"),  16,  21)[0];
		case DOWN:
			return TextureRegion.split(EGA.res.getTexture("downSpike"),  16,  21)[0];
		case LEFT:
			return TextureRegion.split(EGA.res.getTexture("leftSpike"),  21,  16)[0];
		case RIGHT:
			return TextureRegion.split(EGA.res.getTexture("rightSpike"),  21,  16)[0];
		default:
			return TextureRegion.split(EGA.res.getTexture("upSpike"),  16,  21)[0];
		}
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
