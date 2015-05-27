package controller.entities;

import static controller.Variables.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;
import model.entities.SpikeModel;
import model.entities.SpikeModel.spikeOrientation;
import view.entities.SpikeView;

public class SpikeController extends EntityController {
	
	private PolygonShape shape;
	private FixtureDef fDef;
	
	
	
	public SpikeController(SpikeModel em, SpikeView sv){
		super(em, sv);
		
		//currentOri = ori;
		
		shape = new PolygonShape();
		fDef = new FixtureDef();
		
		((SpikeView)super.getTheView()).setTexture(getSpikeOrientation());
		
	}
	
	public void setBody(Body body){
		super.setBody(body);
		super.getTheModel().setPosition(body.getPosition().x, body.getPosition().y);
		setFixtureDef();
	}

	public void setFixtureDef(){
		fDef = new FixtureDef();
		shape = new PolygonShape();
		
		Vector2[] vertices = setSpikeShape(getSpikeOrientation());
		shape.set(vertices);
		
		fDef.shape = shape;

		fDef.filter.categoryBits = Variables.BIT_SPIKE;
		fDef.filter.maskBits = Variables.BIT_PLAYER;
		
		setSensor(fDef, "spike");
	}
	
	public spikeOrientation getSpikeOrientation(){
		return ((SpikeModel)super.getTheModel()).getSpikeOri();
	}
	
	
	
	private Vector2[] setSpikeShape(spikeOrientation ori){
		Vector2 v1 = new Vector2(-8f/PPM, -10f/PPM); 
		Vector2 v2 = new Vector2(0.0f/PPM, 10f/PPM);
		Vector2 v3 = new Vector2(8f/PPM, -10f/PPM);
		switch (ori){
		case UP: 
			return spikeVectors(v1, v2, v3, 0);
		case DOWN:
			return spikeVectors(v1, v2, v3, 180);
		case RIGHT:
			return spikeVectors(v1, v2, v3, 270);
		case LEFT:
			return spikeVectors(v1, v2, v3, 90);
		default:
			return spikeVectors(v1, v2, v3, 0);
		}
	}
	
	private Vector2[] spikeVectors(Vector2 v1, Vector2 v2, Vector2 v3, float deg){
		v1.rotate(deg);
		v2.rotate(deg);
		v3.rotate(deg);
		Vector2[] vertices = {v1, v2, v3};
		return vertices;
	}
}
