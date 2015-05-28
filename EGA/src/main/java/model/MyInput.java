package model;

/**
 * 
 * @author Emil Axelsson, 
 *	
 * Class that are used to see what buttons that are pressed.
 * The class is static so when there has been a change, it changes for
 * all classes. 
 */

public class MyInput {
	
	/** Array with booleans that tells whether a button is pressed or not */
	public static boolean[] keys;
	
	/** 
	 * Array with booleans that holds the old buttons, used to se if the button
	 * is just pressed or down
	 */
	public static boolean[] pkeys;
	
	/** The size of the arrays */
	public static final int NUM_KEYS = 8;
	
	/** The positions for the buttons in the array */
	public static final int BUTTON_FORWARD = 0;
	public static final int BUTTON_BACKWARD = 1;
	public static final int BUTTON_JUMP = 2;
	public static final int BUTTON_DOWN = 3;
	public static final int BUTTON_ESCAPE = 4;
	public static final int BUTTON_ENTER = 5;
	public static final int BUTTON_PAUSE = 6;
	public static final int BUTTON_RESTART = 7;
	
	/** Initiate the arrays */
	static{
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	/** Updates the keys, put the prev.keys into the pkey array */
	public static void update(){
		for(int i = 0; i < NUM_KEYS; i++){
			pkeys[i] = keys[i];
		}
	}
	
	/**
	 * Sets if a key is pressed or not 
	 * @param i, what key that should be set
	 * @param b, whether its pressed or not
	 */
	public static void setKey(int i, boolean b){
		keys[i] = b;
	}
	
	/**
	 * Return whether a specific key is down or not
	 * @param i, the key
	 * @return true if key i is down, false if up
	 */
	public static boolean isDown(int i){ 
		return keys[i];
	}
	
	/**
	 * Return whether a specific key is pressed or not.
	 * Pressed is when the user not hold the key down.
	 * @param i, the key
	 * @return true if key i is pressed, false if not pressed
	 */
	public static boolean isPressed(int i){
		return keys[i] && !pkeys[i];
	}		
	
	/**
	 * Resets the keys
	 */
	public static void setAllKeysFalse(){
		for(int i = 0; i < keys.length; i++){
			keys[i] = false;
			pkeys[i] = false;
		}
	}
}
