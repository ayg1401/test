package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	int i;
	//ShapeRenderer shapeRenderer;
    Circle birdcircle;
    Rectangle[] toptuberecangle;
	Rectangle[] bottomtuberectangle;

	Texture bird[];
	Texture topTube;
	Texture bottomTube;
	int flapstate=0;
	float birdy=0;
	float velocity=0;
	int gameState=0;
	float gavity =2;
	float gap=600;
	float maxtubeoffset;
	Random randomgenegrator;
	BitmapFont font;
	BitmapFont TapTOStart;
	BitmapFont TapToRestart;

	float tubeVelocity=4;
	int numberoftubes=4;
	float distancebetweentubes;
	float[] tubeoffset=new float[numberoftubes];
	float[] tubex=new float[numberoftubes];
	int score=0;
	int scoringtube=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
        //shapeRenderer=new ShapeRenderer();

        birdcircle=new Circle();
        toptuberecangle=new Rectangle[numberoftubes];
		bottomtuberectangle=new Rectangle[numberoftubes];
		font=new BitmapFont();
		TapTOStart=new BitmapFont();
		TapToRestart=new BitmapFont();
		font.setColor(Color.WHITE);
		TapToRestart.setColor(Color.WHITE);
		TapTOStart.setColor(Color.WHITE);
		TapTOStart.getData().setScale(7);

		TapToRestart.getData().setScale(7);
		font.getData().setScale(7);



		gameover=new Texture("gameover.png");
		background=new Texture("bg.png");
		bird=new Texture[2];
		bird[0]=new Texture("bird.png");
		bird[1]=new Texture("bird2.png");

		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");
		maxtubeoffset=Gdx.graphics.getHeight()/2- gap/2 -100;
		randomgenegrator =new Random();
		distancebetweentubes = Gdx.graphics.getWidth() *3/4;
		gamestart();
	}
	void gamestart()
	{

		birdy=Gdx.graphics.getHeight()/2 - bird[0].getHeight()/2;
		for(i=0;i<numberoftubes;i++) {
			tubeoffset[i]=((randomgenegrator.nextFloat()- 0.5f)*(Gdx.graphics.getHeight()/2- gap-50) );
			tubex[i]=(Gdx.graphics.getWidth() - topTube.getWidth()+ Gdx.graphics.getWidth()+(i*distancebetweentubes));

			toptuberecangle[i]=new Rectangle();
			bottomtuberectangle[i]=new Rectangle();

		}
	}
	@Override
	public void render () {
		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState==1) {
			if (Gdx.input.justTouched()) {
				velocity = -30;


			}
			for (i = 0; i < numberoftubes; i++) {
				if (tubex[i] < -topTube.getWidth()) {
					tubex[i] += numberoftubes * distancebetweentubes;
					tubeoffset[i] = ((randomgenegrator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() / 2 - gap - 50));


				} else {
					tubex[i] = tubex[i] - tubeVelocity;
					if (tubex[scoringtube] < Gdx.graphics.getWidth() / 2) {
						score++;
						Gdx.app.log("Score", String.valueOf(score));
						if (scoringtube < 3) {
							scoringtube++;
						} else {
							scoringtube = 0;
						}
					}
				}
				batch.draw(topTube, tubex[i],
						Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomTube, tubex[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i]);
				toptuberecangle[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],
						topTube.getWidth(), topTube.getHeight());
				bottomtuberectangle[i] = new Rectangle(tubex[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i]
						, bottomTube.getWidth(), bottomTube.getHeight());
			}
			if (birdy > 0) {
				velocity = velocity + gavity;
				birdy -= velocity;
			} else {
				gameState = 2;
			}
		}
		else if (gameState==0)
		{
			TapTOStart.draw(batch,"Tap to Start",Gdx.graphics.getWidth()/2-230,
					Gdx.graphics.getHeight()/2-50);
			if (Gdx.input.justTouched()){
				Gdx.app.log("Touched","Yep!");
				gameState=1;
				TapTOStart.dispose();
			}
		}
		else if(gameState==2){
			batch.draw(gameover,Gdx.graphics.getWidth()/2-250,Gdx.graphics.getHeight()/2-50,
					500,100);

			if (Gdx.input.justTouched()){
				Gdx.app.log("Touched","Yep!");
				TapToRestart.dispose();
				gameState=1;
				gamestart();
				score=0;
				scoringtube=0;
				velocity=0;
			}
		}
		if (flapstate == 0)
			flapstate = 1;
		else
			flapstate = 0;

		batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdy);
		font.draw(batch,String.valueOf(score),100, 200);


        birdcircle.set(Gdx.graphics.getWidth()/2,birdy+bird[flapstate].getHeight()/2,
                bird[flapstate].getWidth()/2);
        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
         */
		for(i=0;i<4;i++)
		{
			/*
			shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],
					topTube.getWidth(),topTube.getHeight());
			shapeRenderer.rect(tubex[i],
					Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeoffset[i]
					,bottomTube.getWidth(),bottomTube.getHeight());*/
			if (Intersector.overlaps(birdcircle,toptuberecangle[i])||
			Intersector.overlaps(birdcircle,bottomtuberectangle[i]))
			{
				gameState=2;

			}
		}
		batch.end();


		/*shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
		shapeRenderer.end();*/
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}