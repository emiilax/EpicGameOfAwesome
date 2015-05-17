package view;

import model.EGATimer;
import model.GameData;
import model.MyInput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import controller.EGA;
import controller.GameStateManager;
import controller.SaveHandler;

public class LevelSelect extends GameState {
	
	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	private GlyphLayout layout = new GlyphLayout();

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;

	private String title;

	private int titleFontSize = 50;
	private int menuFontSize = 50;

	private int currentRow = 0;
	private int currentCol = 0;
	
	private String menuItems [][];

	private GameStateManager gsm;
	
	public LevelSelect(GameStateManager gsm, Texture backgroundTexture){
		super(gsm);
		this.gsm = gsm;
		this.backgroundTexture = backgroundTexture;
		init();
	}
	
	private void init(){
		sb = new SpriteBatch();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(titleFontSize);
		titleFont.setColor(Color.WHITE);

		font = gen.generateFont(menuFontSize);

		menuItems = new String[][]{
				{"Level 1", "Level 2", "Level 3"}, //row 0 
				{"Level 4", "Level 5", "Level 6"}, //row 1

		};
		
		setTitle();
		
		SaveHandler.save();
	}
	
	private void setTitle(){
		title = "Choose level to play";
	}

	@Override
	public void handleInput(int i) {
		switch(i){
//		case MyInput.BUTTON_JUMP:
//			if(currentRow > 0){
//				currentRow --;
//			}
//			break;
//		case MyInput.BUTTON_DOWN:
//			if(currentRow < 1){ //needs to be changed if amount of rows is changed
//				currentRow++;
//			}
//			break;
		case MyInput.BUTTON_BACKWARD:
			if(currentCol > 0){
				if(currentCol == 0){
					currentRow ++;
				}else{
					currentCol --;
				}
				
			}break;
		case MyInput.BUTTON_FORWARD:
			if(currentCol < 3){
				if(currentCol == 2){
					currentRow ++;
				}
				currentCol ++;
			}break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		}
	}
	
	private void select(){
		String lvl = menuItems[currentRow][currentCol];
		if(lvl == "Level 1"){
			System.out.print("this is Level 1");
			gsm.getGame().setLevel(new Level(gsm, gsm.getLevel(1)));
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		renderBackground();
		
		layout.setText(titleFont, title);
		float width = layout.width;

		titleFont.draw(sb, title, (EGA.V_WIDTH-width) / 2, 600);

		int x = menuItems[0].length;
		
		for (int row = 0; row < menuItems.length; row++){
			for (int col = 0; col < x; col++){
				layout.setText(font, menuItems[row][col]);
				
				if(currentRow == row && currentCol == col){
					font.setColor(Color.RED);
					System.out.print(menuItems[row][col]);
				}else{
					font.setColor(Color.WHITE);
				}
				
				if(col == 0){
					font.draw(
							sb,
							menuItems[row][col],
							(EGA.V_WIDTH - width - 7*70),
							350 - 180* row
							);
				}				
				if(col == 1){
					font.draw(
							sb,
							menuItems[row][col],
							(EGA.V_WIDTH - width - 2*70 ),
							350 - 180* row
							);
				}
				if(col == 2){
					font.draw(
							sb,
							menuItems[row][col],
							(EGA.V_WIDTH - width + 3*70),
							350 - 180* row
							);
				}
			}
		}
		
		sb.end();
		
	}
	
	private void renderBackground(){
		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.draw(sb);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
