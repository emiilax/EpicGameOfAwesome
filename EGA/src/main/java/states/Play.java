package states;

import static handlers.B2DVars.PPM; 
import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

import main.Game;



public class Play extends GameState{
	

	private World wd;
	private Box2DDebugRenderer b2br;
	
	private OrthographicCamera b2dCam;
	
	private Body playerBody;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;
	
	
	
	public Play(GameStateManager gsm){
		super(gsm);
		// creating wd
		GdxNativesLoader.load();
		this.reset();
		cl = new MyContactListener();
		wd.setContactListener(cl);

		b2br = new Box2DDebugRenderer();
		
		// creating platform
		BodyDef bdef = new BodyDef();
		bdef.position.set(160 / PPM, 120 / PPM);
		
		// Static body, don't move, unaffected by forces
		bdef.type = BodyType.StaticBody;
		Body body = wd.createBody(bdef);
		
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
		playerBody = wd.createBody(bdef);
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
		
		///////////////////
		
		// load tiled map
		tileMap = new TmxMapLoader().load("res/maps/firstmap.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		// kinematic body, ex. a moving platform
		
	}

	public void reset(){
		
		wd = new World(new Vector2(0.0f, -1.9f), true);
		
	}


	public void handleInput() {
		if(MyInput.isDown(MyInput.BUTTON1)){
			if(cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(2, 0, true);
			}
		}
		if(MyInput.isDown(MyInput.BUTTON2)){
			playerBody.applyForceToCenter(-2, 0, true);
		}
	}


	public void update(float dt) {
		handleInput();
		
		wd.step(dt, 6, 2);
	}


	public void render() {
		
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		b2br.render(wd, b2dCam.combined);
	}


	public void dispose() {}

}
