package model_test;

import static org.junit.Assert.*;
import model.entities.SpikeModel;

import org.junit.Test;

public class TestSpikeModel {

	@Test
	public void testSpikeOri() {
		testAOri(SpikeModel.spikeOrientation.UP);
		testAOri(SpikeModel.spikeOrientation.DOWN);
		testAOri(SpikeModel.spikeOrientation.LEFT);
		testAOri(SpikeModel.spikeOrientation.RIGHT);
	}

	public void testAOri(SpikeModel.spikeOrientation ori){
		SpikeModel tester = new SpikeModel(ori);
		assertTrue(tester.getSpikeOri().equals(ori));
	}
}
