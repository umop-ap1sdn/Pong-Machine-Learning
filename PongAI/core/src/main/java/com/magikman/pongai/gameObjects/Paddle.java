package com.magikman.pongai.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.magikman.pongai.Resources;

public class Paddle {
	
	Ball gameBall;
	Sprite paddle;
	
	final float speed = 500;
	float currSpeed = 0;
	
	boolean left;
	
	public boolean justHit = false;
	
	public Paddle(Ball gameBall, boolean left) {
		Texture img = new Texture("C:\\Users\\Owner\\LibGDX Projects\\PongAI\\assets\\pongPaddle.png");
		paddle = new Sprite(img);
		this.left = left;
		
		float paddleR = Resources.spRatio / img.getHeight();
		
		float paddleW = paddleR * img.getWidth();
		float paddleH = paddleR * img.getHeight();
		
		paddle.setSize(paddleW, paddleH);
		paddle.setOriginCenter();
		
		reset();
		
		this.gameBall = gameBall;
	}
	
	public void reset() {
		paddle.setCenterY(Resources.height / 2);
		
		if(left) {
			paddle.setCenterX(Resources.width / 8);
		} else {
			paddle.setCenterX(Resources.width / 8 * 7);
		}
	}
	
	public void update(float dt) {
		currSpeed = speed * dt;
		
		if(paddle.getY() < 0) paddle.setY(0);
		if(paddle.getY() + paddle.getHeight() > Resources.height) paddle.setY(Resources.height - paddle.getHeight());
		
		justHit = checkBounce();
	}
	
	public boolean checkBounce() {
		if(paddle.getBoundingRectangle().overlaps(gameBall.getHitbox()) && !gameBall.getInvulnerable()  ) {
			float maxDis = (paddle.getHeight() / 2) + (gameBall.getHeight() / 2);
			float add = 1;
			if(gameBall.getCenterY() < getCenterY()) {
				maxDis *= -1;
				add = -1;
			}
			
			float inverseDis = maxDis - (gameBall.getCenterY() - getCenterY());
			inverseDis += add;
			/*
			if(Math.abs(inverseDis) < 1) {
				if(inverseDis > 0) inverseDis = 1;
				else inverseDis = -1;
			}
			*/
			
			float yVector = 2000 / inverseDis;
			
			//System.out.println(yVector);
			//System.out.println(inverseDis);
			gameBall.bounce(yVector);
			
			return true;
		}
		
		return false;
	}
	
	public Rectangle getHitbox() {
		return paddle.getBoundingRectangle();
	}
	
	public float getCenterY() {
		return paddle.getY() + (paddle.getHeight() / 2);
	}
	
	public void moveUp() {
		paddle.setY(paddle.getY() + currSpeed);
	}
	
	public void moveDown() {
		paddle.setY(paddle.getY() - currSpeed);
	}
	
	public void render(SpriteBatch sb) {
		paddle.draw(sb);
	}
	
	public void dispose() {
		paddle.getTexture().dispose();
	}
}
