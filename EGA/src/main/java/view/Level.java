package view;

import static controller.Variables.PPM;

import java.util.ArrayList;
import java.util.List;

import model.MyContactListener;
import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;


import controller.Variables;
import controller.EGA;
import controller.GameStateManager;

public class Level extends GameState{

	private boolean debug = true;

	private World world;
	private Box2DDebugRenderer b2br;

	private OrthographicCamera b2dCam;

	private MyContactListener cl;

	private TiledMap tileMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;

	private Character player;

	private Array<IStar> stars;

	//private Array<BigStar> bigStars;

	private HUD hud;

	public Level(GameStateManager gsm){

		super(gsm);

		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();

		stars = new Array<IStar>();

		// create player
		createPlayer();

		// create tiles
		createTiles();

		// create crystals 
		createStars();

		//create big Stars
		//createBigStars();

		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);

		// set up HUD

		hud = new HUD(player);

		//go through all the cells in the layer;

		// kinematic body, ex. a moving platform

	}




	public void handleInput() {
		
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			if(cl.isPlayerOnGround()){

				player.getBody().applyForceToCenter(0, 250, true);

			}
		}

		if(MyInput.isDown(MyInput.BUTTON_FORWARD)){

			player.getBody().setLinearVelocity(1.5f, player.getBody().getLinearVelocity().y);

		}else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			player.getBody().setLinearVelocity(-1.5f, player.getBody().getLinearVelocity().y);

		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){

			player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);

		}

	}
	
	float x = 0;
	float y = 0;
	public void update(float dt) {
		handleInput();
		
		world.step(dt, 6, 2);
		
		Body plb = player.getBody();
		if(!(plb.getPosition().x == x &&  plb.getPosition().y == y)){
			System.out.println("x: " + plb.getPosition().x + ", y: " + plb.getPosition().y);
		}
		
		
		x = plb.getPosition().x;
		y = plb.getPosition().y;
		
		// remove crystals
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size > 0){
			String uData = bodies.get(0).getFixtureList().get(0).getUserData().toString();
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				
				stars.removeValue((IStar) b.getUserData(), true);
				world.destroyBody(b);
				if(uData.equals("smallStar")){
					Body pb = player.getBody();
					
					world.destroyBody(pb);
					BodyDef bdef = new BodyDef();
					bdef.position.set(pb.getPosition().x , pb.getPosition().y );
					bdef.type = BodyType.DynamicBody;
					Body body = world.createBody(bdef);
				
					player = new Character(body);

					body.setUserData(player);
					
					player.collectShrinkStar();
				} else {
					player.collectGrowStar();
				}
			}
		}

		bodies.clear();

		player.update(dt);

		for(int i  = 0; i < stars.size; i++){
			stars.get(i).update(dt);
		}
	}


	public void render() {

		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set camera to follow player
		//cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH / 4,
		//	Game.V_HEIGTH / 2, 
		//	0);

		cam.update();

		// draw tile map
		tmr.setView(cam);
		tmr.render();

		// draw player 
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);

		// draw crystals

		for(int i  = 0; i < stars.size; i++){
			stars.get(i).render(sb);
		}

		//sb.setProjectionMatrix(hudCam.combined);
		//hud.render(sb);

		if(debug){
			b2br.render(world, b2dCam.combined);
		}

	}

	public void dispose() {}

	public void createPlayer(){

		BodyDef bdef = new BodyDef();
		bdef.position.set(100  / PPM, 45 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
	
		player = new Character(body);

		body.setUserData(player);
	}
	
	

	public void createTiles(){

		// load tiled map
		tileMap = new TmxMapLoader().load("res/maps/testmap.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);


		tilesize = (Integer) tileMap.getProperties().get("tilewidth");

		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");	
		createLayer(layer, Variables.BIT_GROUND);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("platform");	
		createLayer(layer, Variables.BIT_PLATFORM);

	}

	public void createLayer(TiledMapTileLayer layer, short bits){

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(int row = 0; row < layer.getHeight(); row++){
			for(int col = 0; col < layer.getWidth(); col++){

				// get cell
				Cell cell = layer.getCell(col, row);

				// check if it exist
				if(cell == null) continue;
				if(cell.getTile() == null) continue;


				// create a body + fixure from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f)* tilesize / PPM, (row + 0.5f) * tilesize / PPM);

				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tilesize / 2 / PPM, - tilesize / 2 / PPM);
				v[1] = new Vector2(-tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[2] = new Vector2(tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[3] = new Vector2(tilesize / 2 / PPM, -tilesize / 2 / PPM);
				v[4] = new Vector2(-tilesize / 2 / PPM, -tilesize / 2 / PPM);

				ChainShape cs = new ChainShape();
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = Variables.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}

		bdef.position.set(EGA.V_WIDTH /2/ PPM, EGA.V_HEIGTH /2/ PPM);

		Vector2[] v = new Vector2[5];
		v[0] = new Vector2(-EGA.V_WIDTH /2/ PPM, -EGA.V_HEIGTH/2/PPM);
		v[1] = new Vector2(-EGA.V_WIDTH/2/PPM, EGA.V_HEIGTH/2/PPM);
		v[2] = new Vector2(EGA.V_WIDTH/2/PPM, EGA.V_HEIGTH/2/PPM);
		v[3] = new Vector2(EGA.V_WIDTH/2/PPM, -EGA.V_HEIGTH/2/PPM);
		v[4] = new Vector2(-EGA.V_WIDTH/2/PPM, -EGA.V_HEIGTH/2/PPM);

		ChainShape cs = new ChainShape();
		cs.createChain(v);
		fdef.friction = 0;
		fdef.shape = cs;
		fdef.filter.categoryBits = bits;
		fdef.filter.maskBits = Variables.BIT_PLAYER;
		fdef.isSensor = false;
		world.createBody(bdef).createFixture(fdef);

	}

	private void createStars(){
		BodyDef bdef = new BodyDef();
		
		//Create small stars
		MapLayer layer = tileMap.getLayers().get("stars");
		loopInStars(layer,true);
	
		// Create the big stars
		layer = tileMap.getLayers().get("bigStar");
		loopInStars(layer,false);
	
	}

	private void loopInStars(MapLayer layer, boolean isSmallStar){
		BodyDef bdef = new BodyDef();
		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = mo.getProperties().get("y", Float.class) / PPM;

			bdef.position.set(x, y);
			
			Body body = world.createBody(bdef);
			
			IStar s;
			if(isSmallStar){
				//body.setUserData("smallstar");
				s = new SmallStar(body);
			} else {
				//body.setUserData("bigstar");
				s = new BigStar(body);
			}
			stars.add(s);
			body.setUserData(s);
			
		}	
	}
}
