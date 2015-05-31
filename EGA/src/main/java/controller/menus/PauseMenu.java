package controller.menus;

import io.SaveHandler;

import java.awt.Point;

import view.renders.MenuRender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import model.MenuModel;
import model.MyInput;
import model.Variables;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import controller.EGA;
import event.EventSupport;

/**
 * 
 * @author Emil Axelsson
 * 
 * This is the pausemenu that will be shown when 
 * you pause the game
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PauseMenu extends Menu{
	/** The spritebatch where it should draw */
	private SpriteBatch sb;
	
	/** The font for the title*/
	private BitmapFont titleFont;
	
	/** The font for the undertitles*/
	private BitmapFont font;
	
	/** 
	 * Constructor, sets up the necassary parts for the 
	 * pause menu
	 */
	public PauseMenu() {
		super();
		init();
	}
	
	/**
	 * Initiate the text that should be shown.
	 * Also sets up the model- and view- class
	 */
	private void init(){
		sb = new SpriteBatch();
		title = "Pause";		
		titleFontSize = Variables.subMenuTitleSize;
		menuFontSize = Variables.subMenuItemSize;
		titleHeight = 650;
		gap = 70;
		xPos = (int)(EGA.V_WIDTH - Variables.menuItemX) / 2;
		yPos = 450;

		menuItems = new String[]{
				"Resume",
				"Restart",
				"Settings",
				"Quit to main menu"
		};
		
		menuItemPositions = new Point[menuItems.length];
		menuItemEndPositions = new Point[menuItems.length];
		
		model = new MenuModel();
		updateModel();
		
		view = new MenuRender(model);
		
		rendered = false;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void handleInput(int i) {
		switch(i){
		case MyInput.BUTTON_JUMP:
			if(currentItem > 0){
				currentItem --;
			}
			break;
		case MyInput.BUTTON_DOWN:
			if(currentItem < menuItems.length-1){
				currentItem++;
			}
			
			break;
		case MyInput.BUTTON_ENTER:
			select();
			break;
		
		case MyInput.BUTTON_PAUSE:
			unpauseTheGame();
			break;
		}	
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void select(){
		if (currentItem == 0){
			// resume
			unpauseTheGame();
			
		}
		if (currentItem == 1){
			// restart level
			EventSupport.getInstance().fireNewEvent("level", 0);


		}
		if (currentItem == 2){
			EventSupport.getInstance().fireNewEvent("settings");

		}
		if(currentItem == 3){
			SaveHandler.save();
			EventSupport.getInstance().fireNewEvent("main");

		}
	}
	
	/**
	 * Remove the menu from the stack and the game will now
	 * be the peek of the stack. 
	 */
	public void unpauseTheGame(){
		EventSupport.getInstance().fireNewEvent("pop");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(float dt) {}
}
