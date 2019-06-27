package com.mygdx.flappykitty;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyKitty extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; // background
	Texture[] birds; // display image for bird

	int flapState = 0; // flap state between 0 and 1
	float birdY = 0; // because bird always in the middle of the screen so we don't need to set birdX
	float velocity = 0; //  velocity of the birdh

	int gameState = 0;
	float gravity = 2; // add gravity so that the bird fall faster :D
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png"); // set image

		birds = new Texture[2]; // set image for bird
		birds[0] = new Texture("bird.png"); // bird image with wings up
		birds[1] = new Texture("bird2.png"); // with wings down
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
	}

	@Override
	public void render () {

		if (gameState != 0) { //  if gameState != 0 -> Start the game

			if (Gdx.input.justTouched()) { // check if user touch the screen
				velocity = -30; // add the negative so that the bird can fly up
			}

			if (birdY > 0 || velocity < 0) { // don't let the bird out the screen
				velocity = velocity + gravity; // each time the render loop is called increase the veloc
				birdY -= velocity; // the  Y coordinate based on veloc so that the bird fall faster
			}

		} else {
			if (Gdx.input.justTouched()) { // check if user touch the screen
				gameState = 1; // if user touch the screen set gameState to 1
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.begin();
		// draw background
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// draw bird in the middle of the screen
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		batch.end();
	}


}
