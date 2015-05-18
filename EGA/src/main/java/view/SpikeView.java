package view;

import java.util.Arrays;
import java.util.Observable;

import controller.SpikeController.spikeOrientation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.EGA;
import model.EntityModel;

public class SpikeView extends EntityView {
	
	private int spikeStyle = 1;
	
	public SpikeView(){
		setTexture();
	}
	
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
			EntityModel cm = (EntityModel)o;
			setXPosition(cm.getXPosition());
			setYPosition(cm.getYPosition());
			render();
		}		
	}

}
