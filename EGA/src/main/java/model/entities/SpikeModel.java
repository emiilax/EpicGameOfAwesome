package model.entities;

import lombok.Data;

@Data
public class SpikeModel extends EntityModel{
	
	private spikeOrientation spikeOri;
	
	
	public SpikeModel(spikeOrientation so){
		spikeOri = so;
	}
	
	public enum spikeOrientation{
		UP, DOWN, RIGHT, LEFT
	}
	
	
}
