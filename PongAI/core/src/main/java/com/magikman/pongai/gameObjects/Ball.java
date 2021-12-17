package com.magikman.pongai.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.magikman.pongai.Resources;

public class Ball {
	
	float speed;
	Vector2 velo;
	
	Sprite ball;
	
	boolean invulnerable = false;
	final int invulnTime = 10;
	int invulnCounter = 0;
	
	public Ball() {
		Texture img = new Texture("C:\\Users\\Owner\\LibGDX Projects\\PongAI\\assets\\pongBall.png");
		ball = new Sprite(img);
		ball.setSize(Resources.spRatio / (img.getWidth() * 2), Resources.spRatio / (img.getHeight() * 2));
		ball.setOrigin(ball.getWidth() / 2, ball.getHeight() / 2);
		
		reset();
	}
	
	public void reset() {
		
		invulnerable = false;
		invulnCounter = 0;
		
		ball.setCenter(Resources.width / 2, Resources.height / 2);
		
		this.speed = 500;
		float yVelo = (float)((Math.random() * 800) - 400f);
		
		float xVelo = (float)Math.sqrt((Math.pow(speed, 2) - Math.pow(yVelo, 2)));
		if(Math.random() > 0.5) xVelo *= -1;
		
		velo = new Vector2(xVelo, yVelo);
	}
	
	public void update(float dt) {
		ball.setPosition(ball.getX() + velo.x * dt, ball.getY() + velo.y * dt);
		
		checkBounce();
		
		if(invulnerable) invulnCounter++;
		if(invulnCounter >= invulnTime) {
			invulnerable = false;
			invulnCounter = 0;
		}
	}
	
	public void checkBounce() {
		if(ball.getY() < 0) {
			ball.setY(0);
			velo.y *= -1;
		} else if((ball.getY() + ball.getHeight()) > Resources.height) {
			ball.setY((Resources.height) - ball.getHeight());
			velo.y *= -1;
		}
	}
	
	public void bounce(float yVector) {
				
		float yVelo = velo.y + yVector;
		
		do {
			speed += 50;
		} while(speed < (Math.abs(yVelo) + 30));
		
		float xVelo = (float)Math.sqrt(Math.pow(speed, 2) - Math.pow(yVelo, 2));
		
		if(velo.x > 0) xVelo *= -1;
		
		velo.set(xVelo, yVelo);
		
		invulnerable = true;
	}
	
	public Rectangle getHitbox() {
		return ball.getBoundingRectangle();
	}
	
	public float getCenterY() {
		return ball.getY() + (ball.getHeight() / 2);
	}
	
	public float getHeight() {
		return ball.getHeight();
	}
	
	public void setX(float x) {
		ball.setX(x);
	}
	
	public Vector2 getVelo() {
		return velo;
	}
	
	public float getX() {
		return ball.getX();
	}
	
	public boolean getInvulnerable() {
		return this.invulnerable;
	}
	
	public void render(SpriteBatch sb) {
		ball.draw(sb);
	}
	
	public void dispose() {
		ball.getTexture().dispose();
	}
}
