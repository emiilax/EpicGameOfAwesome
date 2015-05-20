package controller;

import static controller.Variables.PPM;
import model.EntityModel;
import view.SpikeView;
import view.StarView;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.SpikeController.spikeOrientation;

public class StarController extends EntityController {

	private CircleShape cshape;
	private FixtureDef fdef;
	
	public StarController(EntityModel em, StarView sv){
		super(em, sv);

		cshape = new CircleShape();
		fdef = new FixtureDef();
		
		

	}

	public void setBody(Body body){
		super.setBody(body);
		super.getTheModel().setPosition(body.getPosition().x, body.getPosition().y);
		setFixtureDef();
		
	}

	public void setFixtureDef(){
		
		cshape.setRadius(8 / PPM);

		fdef.shape = cshape;
		fdef.isSensor = true;

		fdef.filter.categoryBits = Variables.BIT_STAR;
		fdef.filter.maskBits = Variables.BIT_PLAYER;

		setSensor(fdef, "smallStar");
	}
}
