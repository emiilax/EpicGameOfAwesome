package model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The model-class for the spike
 * 
 * @author Emil Axelsson, Hampus Rönström
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class SpikeModel extends EntityModel{
	
	private spikeOrientation spikeOri;
	
	
	public SpikeModel(spikeOrientation so){
		spikeOri = so;
	}
	
	/**
	 * Enums with the direction
	 * 
	 * @author Hampus Rönström
	 *
	 */
	public enum spikeOrientation{
		UP, DOWN, RIGHT, LEFT
	}
	
	
}
