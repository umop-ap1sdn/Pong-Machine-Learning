package com.magikman.pongai.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.magikman.pongai.Resources;

public class Button {
	
	ShapeRenderer shape;
	GlyphLayout text;
	BitmapFont font;
	Rectangle bounds;
	
	float textX;
	float textY;
	
	public Button(String text, float x, float y) {
		
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(1.8f);
		this.text = new GlyphLayout();
		this.text.setText(font, text);
		bounds = new Rectangle();
		
		float size = Resources.height / 6;
		bounds.set(x, y, size, size);
		
		textX = (size / 2) - (this.text.width / 2);
		textY = (size / 2) + (this.text.height / 2);
		
		shape = new ShapeRenderer();
		shape.setAutoShapeType(true);
		shape.setColor(Color.RED);
	}
	
	public boolean getClick() {
		
		if(Gdx.input.justTouched()) {
			float realY = Resources.height - Gdx.input.getY();
			
			if(bounds.contains(Gdx.input.getX(), realY)) return true;
		}
		
		return false;
	}
	
	public void render(SpriteBatch sb) {
		
		shape.begin();
		shape.set(ShapeType.Filled);
		shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		shape.end();
		
		sb.begin();
		font.draw(sb, text, textX + bounds.x, textY + bounds.y);
		sb.end();
	}
	
	public void dispose() {
		shape.dispose();
		font.dispose();
		
	}
}
