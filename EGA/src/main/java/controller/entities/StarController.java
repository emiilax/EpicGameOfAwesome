package controller.entities;

import static model.Variables.PPM;
import model.Variables;
import model.entities.EntityModel;
import view.entities.SpikeView;
import view.entities.StarView;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;


/**
 * Contains all controller variables and methods used in a star
 * @author Erik
 *
 */
public class StarController extends EntityController {

	private CircleShape cshape;
	private FixtureDef fdef;
	private boolean isBig;

	/**
	 * @param em The entitymodel the star will use
	 * @param sv The starview the star will use
	 * @param isBig If this instance of the star will be big or small
	 */
	public StarController(EntityModel em, StarView sv, boolean isBig){
		super(em, sv);

		cshape = new CircleShape();
		fdef = new FixtureDef();
		this.isBig = isBig;
	}
	
	/**
	 * Sets the body of the star
	 */
	public void setBody(Body body){
		super.setBody(body);
		super.getTheModel().setPosition(body.getPosition().x, body.getPosition().y);
		setFixtureDef();
	}
	
	/**
	 * Creates a fixture definition around the star which the character can collide into.
	 */
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
	
	/**
	 * @return True if this instance is big, small otherwise
	 */
	public boolean isBig(){
		return isBig;
	}
	
	/**
	 * Chooses if this star will be rendered or not
	 */
	public void setRender(boolean b){
		super.setRender(b);
	}
}
