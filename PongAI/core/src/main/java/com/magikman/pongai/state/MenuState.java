package com.magikman.pongai.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuState extends State{
	
	Texture play;
	Texture paddle;
	
	float imgX, imgY, imgW, imgH;
	
	float paddle1X, paddle2X, paddleY, paddleW, paddleH;
	
	public MenuState(Manager gsm) {
		super(gsm);
		play = new Texture("C:\\Users\\Owner\\LibGDX Projects\\PongAI\\assets\\startButton.png");
		paddle = new Texture("C:\\Users\\Owner\\LibGDX Projects\\PongAI\\assets\\pongPaddle.png");
		
		imgH = Gdx.graphics.getHeight() / 4;
		imgW = imgH;
		imgX = (Gdx.graphics.getWidth() / 2) - (imgW / 2);
		imgY = (Gdx.graphics.getHeight() / 2) - (imgH / 2);
		
		float paddleRatio = (Gdx.graphics.getHeight() / 12) / paddle.getHeight();
		paddleH = paddle.getHeight() * paddleRatio;
		paddleW = paddle.getWidth() * paddleRatio;
		
		paddleY = (Gdx.graphics.getHeight() / 2) - (paddleH / 2);
		paddle1X = (Gdx.graphics.getWidth() / 8) - (paddleW / 2);
		paddle2X = (Gdx.graphics.getWidth() * 7 / 8) - (paddleW / 2);
	}
	
	@Override
	public void handleInput() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			super.setState(new PlayState(gsm));
		}
	}
	
	@Override
	public void update(float dt) {
		
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(play, imgX, imgY, imgW, imgH);
		sb.draw(paddle, paddle1X, paddleY, paddleW, paddleH);
		sb.draw(paddle, paddle2X, paddleY, paddleW, paddleH);
		sb.end();
	}
	
	public void dispose() {
		play.dispose();
		paddle.dispose();
	
	}
}
