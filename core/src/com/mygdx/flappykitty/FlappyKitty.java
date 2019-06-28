package com.mygdx.flappykitty;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyKitty extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background; // background
	Texture[] birds; // display image for bird
	Texture[] tubes; // array of tubes 0: bottom, 1: top
	Texture gameOver;
   // ShapeRenderer shapeRenderer ;// just like Batch but draw shape

	int flapState = 0; // flap state between 0 and 1
	float birdY = 0; // because bird always in the middle of the screen so we don't need to set birdX
	float velocity = 0; //  velocity of the bird
    Circle birdCircle;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;

	int gameState = 0;
	float gravity = 2; // add gravity so that the bird fall faster :D
	float gap = 400;
	float maxTubeOffSet;
	Random randomGenerator; // random generator to tube ofsf set
	float tubeVelocity = 4; //velocity of the tube move left
	int numberOfTubes = 4;
	float [] tubeX = new float[numberOfTubes]; // x tube coordinate
	float [] tubeOffSet = new float[numberOfTubes]; // off set
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomeTubeRectangles;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png"); // set image
		gameOver = new Texture("gameover.png");

		birds = new Texture[2]; // set image for bird
		tubes = new Texture[2];
		birds[0] = new Texture("bird.png"); // bird image with wings up
		birds[1] = new Texture("bird2.png"); // with wings down
		tubes[0] = new Texture("bottomtube.png"); // bottom tube
		tubes[1] = new Texture("toptube.png"); // top tube;

        maxTubeOffSet = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() *2/3; // distance between each tube

//        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomeTubeRectangles = new Rectangle[numberOfTubes];

        // display score in the screen
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(10);

        startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		// create loop for 4 tubes and it will move left again and again
		for (int i = 0; i < numberOfTubes; i++){
			tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f)* (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - tubes[1].getWidth() / 2 + Gdx.graphics.getWidth() + i*distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomeTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		// draw background
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) { //  if gameState != 0 -> Start the game

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++; // scoring

				if (scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0; // set it back to the first tube
				}
			}

			if (Gdx.input.justTouched()) { // check if user touch the screen
				velocity = -30; // add the negative so that the bird can fly up
			}

			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -tubes[1].getWidth()) {// if 1 tube move out of the screen
					tubeX[i] += numberOfTubes * distanceBetweenTubes; // shift the tube to come back again
					tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity; // move tube to left
				}
				batch.draw(tubes[1], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]); // top tube
				batch.draw(tubes[0], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[0].getHeight() + tubeOffSet[i]); // bottom tube

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i], tubes[1].getWidth(), tubes[1].getHeight());
				bottomeTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[0].getHeight() + tubeOffSet[i], tubes[0].getWidth(), tubes[0].getHeight());
			}

			if (birdY > 0) { // don't let the bird out the screen
				velocity = velocity + gravity; // each time the render loop is called increase the veloc
				birdY -= velocity; // the  Y coordinate based on veloc so that the bird fall faster
			} else { // if the bird is not above the bottom the screen
				gameState = 2;
			}
		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) { // check if user touch the screen
				gameState = 1; // if user touch the screen set gameState to 1
			}
		} else if (gameState == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			if (Gdx.input.justTouched()) { // check if user touch the screen
				gameState = 1; // if user touch the screen set gameState to 1
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		// draw bird in the middle of the screen
		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score), 100,200);
		batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2); // coordinate of the center of circle, and the last var is the radius

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++){
//        	shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i], tubes[1].getWidth(),tubes[1].getHeight());
//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[0].getHeight() + tubeOffSet[i], tubes[0].getWidth(), tubes[0].getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomeTubeRectangles[i])){ // check collision
				gameState = 2; // game is over

			}
        }

//        shapeRenderer.end();

	}
}
