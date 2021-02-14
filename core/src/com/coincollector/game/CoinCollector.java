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
	int heroState = 0;
	int pause = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		hero = new Texture[4];
		hero[0] = new Texture("frame-1.png");
		hero[1] = new Texture("frame-2.png");
		hero[2] = new Texture("frame-3.png");
		hero[3] = new Texture("frame-4.png");
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
		batch.draw(hero[heroState], Gdx.graphics.getWidth()/2 - hero[heroState].getWidth()/2, Gdx.graphics.getHeight()/2 - hero[heroState].getHeight()/2);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
