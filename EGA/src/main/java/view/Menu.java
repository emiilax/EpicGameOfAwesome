package view;

import static controller.Variables.PPM;
import model.MyContactListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;

import controller.EGA;
import controller.GameStateManager;
import controller.TiledMapActor;
import controller.TiledMapClickListener;
import controller.TiledMapStage;
import controller.Variables;

public class Menu extends GameState {

	private boolean debug = true;
	private GameStateManager gsm;

	private World world;
	private Box2DDebugRenderer b2br;
	private MyContactListener cl;
	private TiledMap tileMap;
	private float menuHeight;
	private float menuWidth;
	private OrthogonalTiledMapRenderer tmr;
	private OrthographicCamera b2dCam;
	private TiledMapClickListener clickListener;
	private TiledMapActor actor;
	private Stage stage;
	
	public Menu(GameStateManager gsm){

		super(gsm);

		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);

		//this.gsm = gsm;

		menuWidth = 296;
		menuHeight = 600;
		
		createTiles();

	}


	public void createTiles(){

		// load tiled map
		tileMap = new TmxMapLoader().load("res/maps/menu.tmx");

		tmr = new OrthogonalTiledMapRenderer(tileMap);

		MapLayer layer;
		layer = (MapLayer) tileMap.getLayers().get("button");
		TiledMapTileLayer TiledMapLayer;
		TiledMapLayer = (TiledMapTileLayer)tileMap.getLayers().get("startbutton");
		createLayer(layer, TiledMapLayer, Variables.BIT_PLATFORM);

	}

	public void createLayer(MapLayer layer, TiledMapTileLayer tiledMapLayer, short bits){

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		for(MapObject mo: layer.getObjects()){
			bdef.type = BodyType.StaticBody;
			
			float width = mo.getProperties().get("width", Float.class) / PPM / 2;
			float height = mo.getProperties().get("height", Float.class) / PPM / 2;

			float x = (mo.getProperties().get("x", Float.class) + width*PPM) / PPM;
			float y = (mo.getProperties().get("y", Float.class) + height*PPM) / PPM;
			
			Cell cell = tiledMapLayer.getCell(1,1);
			
			actor = new TiledMapActor(tileMap, tiledMapLayer, cell);
			clickListener = new TiledMapClickListener(actor);
			
			stage = new TiledMapStage(tileMap);
			Gdx.input.setInputProcessor(stage);
			
			System.out.println(Float.floatToIntBits((Float)mo.getProperties().get("x")));
			
			bdef.position.set(x, y);
			shape.setAsBox(width, height);
			fdef.friction = 0;
			fdef.shape = shape;
			fdef.filter.categoryBits = bits;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			fdef.isSensor = false;

			world.createBody(bdef).createFixture(fdef);
			
			
		}

	}


	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		// draw tile map
		tmr.setView(cam);
		tmr.render();

		// draw player 
		sb.setProjectionMatrix(cam.combined);
		//player.render(sb);
		
		if(debug){
			b2br.render(world, b2dCam.combined);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
