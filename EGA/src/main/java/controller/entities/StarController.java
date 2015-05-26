package controller.entities;

import static controller.Variables.PPM;
import model.entities.EntityModel;
import view.SpikeView;
import view.entities.StarView;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import controller.Variables;


public class StarController extends EntityController {

	private CircleShape cshape;
	private FixtureDef fdef;
	private boolean isBig;

	public StarController(EntityModel em, StarView sv, boolean isBig){
		super(em, sv);

		cshape = new CircleShape();
		fdef = new FixtureDef();
		this.isBig = isBig;



	}

	public void setBody(Body body){
		super.setBody(body);
		super.getTheModel().setPosition(body.getPosition().x, body.getPosition().y);
		setFixtureDef();

	}

	public void setFixtureDef(){

		if(isBig){
			cshape.setRadius(22 / PPM);

			fdef.shape = cshape;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_STAR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;

			setSensor(fdef, "bigStar");

		} else {

			cshape.setRadius(8 / PPM);

			fdef.shape = cshape;
			fdef.isSensor = true;

			fdef.filter.categoryBits = Variables.BIT_STAR;
			fdef.filter.maskBits = Variables.BIT_PLAYER;

			setSensor(fdef, "smallStar");
		}
	}

	public boolean isBig(){
		return isBig;
	}

	public void setRender(boolean b){
		super.setRender(b);
	}
}
