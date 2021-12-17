package com.magikman.pongai.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.magikman.pongai.gameObjects.Paddle;

public class HumanController extends Controller{

	boolean arrowKeys;
	
	public HumanController(Paddle paddle, boolean arrowKeys) {
		super(paddle);
		
		this.arrowKeys = arrowKeys;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if(arrowKeys) {
			if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) super.upInput();
			if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) super.downInput();
		} else {
			if(Gdx.input.isKeyPressed(Input.Keys.W)) super.upInput();
			if(Gdx.input.isKeyPressed(Input.Keys.S)) super.downInput();
		}
	}

	
}
