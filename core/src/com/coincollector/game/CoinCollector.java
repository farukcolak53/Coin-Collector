package com.coincollector.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class CoinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] hero;
	int heroState = 0;  //id of the hero state (image)
	int pause = 0;  //to slow refreshing
	float gravity = 0.2f;  //falling
	float velocity = 0;
	float heroY = 0;  //y coordinate of hero


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		hero = new Texture[4];
		hero[0] = new Texture("frame-1.png");
		hero[1] = new Texture("frame-2.png");
		hero[2] = new Texture("frame-3.png");
		hero[3] = new Texture("frame-4.png");

		heroY = Gdx.graphics.getHeight()/2;
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (Gdx.input.justTouched()){  //For every tap, jump +10
			velocity = -10;
		}

		if (pause < 8){
			pause++;
		}
		else {
			pause = 0;
			if (heroState < 3)
				heroState++;
			else
				heroState = 0;
		}

		if (heroY <= 0)  //Prevent falling below screen
			heroY = 0;
		else if (heroY >= Gdx.graphics.getHeight() - hero[heroState].getHeight())  //Prevent go above screen
			heroY = Gdx.graphics.getHeight() - hero[heroState].getHeight();

		batch.draw(hero[heroState], Gdx.graphics.getWidth()/2 - hero[heroState].getWidth()/2, heroY);

		velocity = velocity + gravity;
		heroY = heroY - velocity;

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
