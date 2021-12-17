package com.magikman.pongai.controller;

import com.magikman.pongai.gameObjects.Paddle;

public abstract class Controller {
	
	Paddle paddle;
	public Controller(Paddle paddle) {
		this.paddle = paddle;
	}
	
	public void upInput() {
		paddle.moveUp();
	}
	
	public void downInput() {
		paddle.moveDown();
	}
	
	public void dynamicInput(float y) {
		
	}
	
	public abstract void handleInput();
}
