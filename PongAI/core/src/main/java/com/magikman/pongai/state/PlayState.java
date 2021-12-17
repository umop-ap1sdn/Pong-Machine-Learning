package com.magikman.pongai.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magikman.pongai.Resources;
import com.magikman.pongai.gameObjects.*;
import com.magikman.pongai.neat.neat.Neat;

import neuroEvo.Population;

public class PlayState extends State{
	
	BitmapFont fnt;
	GlyphLayout score1;
	GlyphLayout score2;
	GlyphLayout currentScores;
	
	GlyphLayout winner;
	
	Game[] allGames;
	Population neuroEvo;
	Neat neat;
	
	int popSize = 20;
	
	Button left;
	Button right;
	
	boolean human = false;
	
	Game humanGame;
	
	int leftWins = 0, rightWins = 0, leftIndWins = 0, rightIndWins = 0, currLeftWins = 0, currRightWins = 0;
	float desX, desY;
	
	public PlayState(Manager gsm) {
		super(gsm);
		
		human = false;
		
		score1 = new GlyphLayout();
		score2 = new GlyphLayout();
		winner = new GlyphLayout();
		currentScores = new GlyphLayout();
		
		fnt = new BitmapFont();
		fnt.getData().setScale(1.5f);
		fnt.setColor(Color.WHITE);
		
		String scoreDes = String.format("Total Genome Overall Wins: %d%20sTotal NEAT Overall Wins: %d\nTotal Genome Game Wins: %d%20sTotal NEAT Game Wins%d\nCurrent Game Genome Wins: %d%20sCurrent NEAT Game Wins: %d", leftWins, "", rightWins, leftIndWins, "", rightIndWins, currLeftWins, "", currRightWins);
		currentScores.setText(fnt,  scoreDes);
		
		fnt.getData().setScale(3f);
		
		allGames = new Game[popSize];
		neuroEvo = new Population(8, 3, popSize, false);
		neat = new Neat(8, 3, popSize);
		
		float buttonSize = Resources.height / 6;
		
		left = new Button("     vs\nGenome", (Resources.width / 4) - buttonSize / 2, Resources.height / 6);
		right = new Button("   vs\nNEAT", (Resources.width / 4 * 3) - buttonSize / 2, Resources.height / 6);
		
		desX = Resources.width / 2 - currentScores.width / 2;
		desY = Resources.height / 8 + currentScores.height / 4;
		
		setup();
	}
	
	public void setup() {
		for(int x = 0; x < popSize; x++) {
			allGames[x] = new Game(neuroEvo.getGenome(x), neat.getClient(x), 7);
		}
	}
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		int leftScore = 0;
		int rightScore = 0;
		
		currLeftWins = 0;
		currRightWins = 0;
		
		if(!human) {
			
			for(int x = 0; x < allGames.length; x++) {
				if(allGames[x].getWinner() == 0) allGames[x].update(dt);
				else {
					if(allGames[x].getWinner() == 1) currLeftWins++;
					else currRightWins++;
				}
				
				leftScore += allGames[x].getScores()[0];
				rightScore += allGames[x].getScores()[1];
			}
			
			
			score1.setText(fnt, "" + leftScore);
			score2.setText(fnt, "" + rightScore);
			
			if(checkEnd()) {
				leftIndWins += currLeftWins;
				rightIndWins += currRightWins;
				
				if(leftScore > rightScore) leftWins++;
				else if(rightScore > leftScore) rightWins++;
				
				setupProcedure();
				setup();
			}
			
		} else {
			
			if(!checkEnd()) humanGame.update(dt);
			else {
				human = false;
				setup();
			}
			
			score1.setText(fnt,  "" + humanGame.getScores()[0]);
			score2.setText(fnt,  "" + humanGame.getScores()[1]);
		}
		
		fnt.getData().setScale(1.5f);
		String scoreDes = String.format("Total Genome Overall Wins: %d%20sTotal NEAT Overall Wins: %d\nTotal Genome Game Wins: %d%20sTotal NEAT Game Wins%d\nCurrent Game Genome Wins: %d%20sCurrent NEAT Game Wins: %d", leftWins, "", rightWins, leftIndWins, "", rightIndWins, currLeftWins, "", currRightWins);
		currentScores.setText(fnt,  scoreDes);
		fnt.getData().setScale(3f);
	}
	
	public double getMinFitness() {
		int index = 0;
		for(int x = 1; x < popSize; x++) {
			if(neat.getClient(x).getScore() < neat.getClient(index).getScore()) index = x;
		}
		
		return Math.abs(neat.getClient(index).getScore());
	}
	
	public boolean checkEnd() {
		if(!human) {
			for(int x = 0; x < allGames.length; x++) {
				if(allGames[x].getWinner() == 0) return false;
			}
		} else {
			if(humanGame.getWinner() == 0) return false; 
		}
		
		return true;
	}
	
	public void setupProcedure() {
		
		for(int x = 0; x < popSize; x++) {
			neat.getClient(x).increaseScore(getMinFitness());
		}
		
		neuroEvo.breed();
		neat.evolve();
		
		for(int x = 0; x < allGames.length; x++) {
			allGames[x].dispose();
			neat.getClient(x).setScore(0);
		}
		
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
			super.setState(new MenuState(gsm));
		}
		
		/*
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			super.setState(new MenuState(gsm));
		}
		*/
		
		if(!human) {
			for(int x = 0; x < allGames.length; x++) {
				if(allGames[x].getWinner() == 0) allGames[x].handleInput();
			}
		} else {
			humanGame.handleInput();
		}
		
		if(left.getClick() && !human) {
			human = true;
			setupProcedure();
			humanGame = new Game(neuroEvo.getGenome(0), 7);
		}
		
		if(right.getClick() && !human) {
			human = true;
			setupProcedure();
			humanGame = new Game(neat.getClient(0), 7);
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		//sb.setColor(1f, 1f, 1f, 0.2f);
		
		sb.begin();
		
		
		fnt.draw(sb, score1, (Resources.width / 4) - (score1.width / 2), (Resources.height) - score1.height / 2);
		fnt.draw(sb, score2, (Resources.width / 4 * 3) - (score2.width / 2), (Resources.height) - score2.height / 2);
		
		if(!human) {
			for(int x = 0; x < allGames.length; x++) {
				if(allGames[x].getWinner() == 0) allGames[x].render(sb);
			}
		} else {
			humanGame.render(sb);
		}
		
		if(!human) {
			fnt.getData().setScale(1.5f);
			fnt.draw(sb, currentScores, desX, desY);
			fnt.getData().setScale(3f);
		}
		
		sb.end();
		
		if(!human) {
			left.render(sb);
			right.render(sb);
		}
		
	}
	
	@Override
	public void dispose() {
		//game.dispose();
		fnt.dispose();
	}
}
