package com.magikman.pongai.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.magikman.pongai.Resources;
import com.magikman.pongai.controller.Controller;
import com.magikman.pongai.controller.HumanController;
import com.magikman.pongai.controller.NeatController;
import com.magikman.pongai.controller.NeuroEvoController;
import com.magikman.pongai.neat.neat.Client;

import neuroEvo.GenomeNet;

public class Game {
	
	Controller player1, player2;
	Paddle leftPaddle, rightPaddle;
	Ball gameBall;
	
	int winScore;
	
	int leftScore, rightScore;
	
	boolean aiLeft;
	boolean aiRight;
	
	public Game(int winScore) {
		aiLeft = false;
		aiRight = false;
		
		this.winScore = winScore;
		this.leftScore = 0;
		this.rightScore = 0;
		
		this.gameBall = new Ball();
		
		this.leftPaddle = new Paddle(gameBall, true);
		this.rightPaddle = new Paddle(gameBall, false);
		
		player1 = new HumanController(leftPaddle, false);
		
		player2 = new HumanController(rightPaddle, true);
		
	}
	
	public Game(GenomeNet mlPlayer1, Client mlPlayer2, int winScore) {
		
		this.winScore = winScore;
		this.leftScore = 0;
		this.rightScore = 0;
		
		this.gameBall = new Ball();
		
		this.leftPaddle = new Paddle(gameBall, true);
		this.rightPaddle = new Paddle(gameBall, false);
		
		player1 = new NeuroEvoController(leftPaddle, mlPlayer1, this, true);
		player2 = new NeatController(rightPaddle, mlPlayer2, this, false);
		
		aiRight = true;
		aiLeft = true;
	}
	
	public Game(GenomeNet mlPlayer, int winScore) {
		this.winScore = winScore;
		this.leftScore = 0;
		this.rightScore = 0;
		
		this.gameBall = new Ball();
		
		this.leftPaddle = new Paddle(gameBall, true);
		this.rightPaddle = new Paddle(gameBall, false);
		
		player1 = new NeuroEvoController(leftPaddle, mlPlayer, this, true);
		player2 = new HumanController(rightPaddle, true);
		
		aiLeft = true;
		aiRight = false;
	}
	
	public Game(Client mlPlayer, int winScore) {
		this.winScore = winScore;
		this.leftScore = 0;
		this.rightScore = 0;
		
		this.gameBall = new Ball();
		
		this.leftPaddle = new Paddle(gameBall, true);
		this.rightPaddle = new Paddle(gameBall, false);
		
		player1 = new HumanController(leftPaddle, false);
		player2 = new NeatController(rightPaddle, mlPlayer, this, false);
		
		aiLeft = false;
		aiRight = true;
	}
	
	public int[] getScores() {
		return new int[] {leftScore, rightScore};
	}
	
	public void update(float dt) {
		gameBall.update(dt);
		leftPaddle.update(dt);
		rightPaddle.update(dt);
		
		if(gameBall.getX() > Resources.width) {
			reset();
			leftScore++;
			
			if(aiLeft) {
				((NeuroEvoController)player1).increaseFitness(100);
				
			}
			
			if(aiRight) {
				((NeatController)player2).increaseFitness(-70);
			}
		}
		if((gameBall.getX() + gameBall.getHitbox().width) < 0) {
			reset();
			rightScore++;
			
			if(aiRight) {
				((NeatController)player2).increaseFitness(100);
				
			}
			
			if(aiLeft) {
				((NeuroEvoController)player1).increaseFitness(-70);
			}
		}
		
		if(aiLeft) {
			((NeuroEvoController)player1).increaseFitness(5 * dt);
			
			if(leftPaddle.justHit) ((NeuroEvoController)player1).increaseFitness(25);
			
		}
		
		if(aiRight) {
			((NeatController)player2).increaseFitness(5 * dt);
			
			if(rightPaddle.justHit) ((NeatController)player2).increaseFitness(25);
		}
	}
	
	public void reset() {
		gameBall.reset();
		leftPaddle.reset();
		rightPaddle.reset();
	}
	
	public int getWinner() {
		if(leftScore >= winScore) return 1;
		else if(rightScore >= winScore) return -1;
		else return 0;
	}
	
	public double[] getAIInputs(boolean left) {
		double ballXSpeed = gameBall.getVelo().x;
		double ballYSpeed = gameBall.getVelo().y;
		Rectangle ballPosData = gameBall.getHitbox();
		Rectangle paddlePos;
		
		double ballX;
		double ballY = gameBall.getCenterY();
		
		if(left) {
			paddlePos = leftPaddle.getHitbox();
			ballX = ballPosData.x;
		}
		else {
			paddlePos = rightPaddle.getHitbox();
			ballX = ballPosData.x + ballPosData.width;
		}
		
		double paddleBot = paddlePos.y;
		double paddleMid = paddlePos.y + paddlePos.height / 2;
		double paddleTop = paddlePos.y + paddlePos.height;
		
		double topDis = ballY - paddleTop;
		double midDis = ballY - paddleMid;
		double botDis = ballY - paddleBot;
		
		return new double[] {paddleMid, ballX, ballY, ballXSpeed, ballYSpeed, topDis, midDis, botDis};
	}
	
	public void handleInput() {
		player1.handleInput();
		player2.handleInput();
	}
	
	public void render(SpriteBatch sb) {
		gameBall.render(sb);
		leftPaddle.render(sb);
		rightPaddle.render(sb);
	}
	
	public void dispose() {
		gameBall.dispose();
		leftPaddle.dispose();
		rightPaddle.dispose();
	}
}
