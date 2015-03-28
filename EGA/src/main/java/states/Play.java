package states;

import static handlers.B2DVars.PPM; 
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;

public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2br;
	
	private OrthographicCamera b2dCam;
	
	private Body playerBody;
	
	private MyContactListener cl;
	
	
	public Play(GameStateManager gsm){
		super(gsm);
		// creating world
		world = new World(new Vector2(0,-1.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		
		b2br = new Box2DDebugRenderer();
		
		// creating platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 120 / PPM);
		
		// Static body, don't move, unaffected by forces
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50 / PPM, 5 / PPM);
		
		
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_GROUND;
		fDef.filter.maskBits = B2DVars.BIT_PLAYER;
		body.createFixture(fDef).setUserData("ground");
		
		//Creating player
		//dynamic body, always get affected by forces
		bdef.position.set(160  / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		shape.setAsBox(5 / PPM, 5 / PPM);
		
		
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_GROUND;
		//fDef.restitution = 0.5f;
		playerBody.createFixture(fDef).setUserData("player");
		
		// create foot sensor
		shape.setAsBox(2/PPM,  2 / PPM, new Vector2(0, -5/ PPM), 0);
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_GROUND;
		fDef.isSensor = true;
		playerBody.createFixture(fDef).setUserData("foot");
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGTH / PPM);
		

		// kinematic body, ex. a moving platform
		
	}

	


	public void handleInput() {
		if(MyInput.isPressed(MyInput.BUTTON1)){
			if(cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(0, 100, true);
			}
		}
		if(MyInput.isDown(MyInput.BUTTON2)){
			System.out.println("hold x");
		}
	}


	public void update(float dt) {
		handleInput();
		
		world.step(dt, 6, 2);
	}


	public void render() {
		
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		b2br.render(world, b2dCam.combined);
	}


	public void dispose() {}

}
