package model;

public class MyInput {
	
	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS = 6;
	public static final int BUTTON_FORWARD = 0;
	public static final int BUTTON_BACKWARD = 1;
	public static final int BUTTON_JUMP = 2;
	public static final int BUTTON_LEVEL1 = 4;
	public static final int BUTTON_LEVEL2 = 5;
	
	
	static{
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update(){
		for(int i = 0; i < NUM_KEYS; i++){
			pkeys[i] = keys[i];
		}
	}
	
	public static void setKey(int i, boolean b){
		keys[i] = b;
	}
	
	public static boolean isDown(int i){ 
		return keys[i];
	}
	
	public static boolean isPressed(int i){
		return keys[i] && !pkeys[i];
	}		
	
}
