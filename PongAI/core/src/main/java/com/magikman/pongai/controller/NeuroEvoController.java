package com.magikman.pongai.controller;

import com.badlogic.gdx.Gdx;
import com.magikman.pongai.gameObjects.Game;
import com.magikman.pongai.gameObjects.Paddle;

import neuroEvo.GenomeNet;

public class NeuroEvoController extends Controller {

	GenomeNet player;
	double[] networkInputs;
	Game vision;
	boolean isLeft;
	
	public NeuroEvoController(Paddle paddle, GenomeNet player, Game vision, boolean isLeft) {
		super(paddle);
		// TODO Auto-generated constructor stub
		this.player = player;
		this.vision = vision;
		this.isLeft = isLeft;
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
		networkInputs = vision.getAIInputs(isLeft);
		double[] outputs = player.calculate(networkInputs);
		
		int index = 0;
		for(int x = 0; x < outputs.length; x++) {
			if(outputs[x] > outputs[index]) index = x;
		}
		
		switch(index) {
		case 0:
			super.upInput();
			increaseFitness(Gdx.graphics.getDeltaTime() * -8);
			break;
		case 2:
			super.downInput();
			increaseFitness(Gdx.graphics.getDeltaTime() * -8);
			break;
		}
		
	}
	
	public void setFitness(double fitness) {
		player.setFitness(fitness);
	}
	
	public void increaseFitness(double amount) {
		player.increaseFitness(amount);
	}
}
