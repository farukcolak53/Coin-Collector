package com.coincollector.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Random;

public class CoinCollector extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] hero;
    int heroState = 0;  //id of the hero state (image)
    int pause = 0;  //to slow refreshing
    float gravity = 0.2f;  //falling
    float velocity = 0;
    float heroY = 0;  //y coordinate of hero
    Random random;
    int score = 0;
    BitmapFont font;
    int gameState = 0; //0 -> not started, 1 -> running, 3 -> over

    //Coordinates of coins
    ArrayList<Integer> coinX = new ArrayList<>();
    ArrayList<Integer> coinY = new ArrayList<>();
    Texture coin;
    int coinCount;

    //Coordinates of bombs
    ArrayList<Integer> bombX = new ArrayList<>();
    ArrayList<Integer> bombY = new ArrayList<>();
    Texture bomb;
    int bombCount;

    //Coin boundaries to detect collisions
    ArrayList<Rectangle> coinRectangles = new ArrayList<>();

    //Bomb boundaries to detect collisions
    ArrayList<Rectangle> bombRectangles = new ArrayList<>();

    Rectangle heroRectangle;

    Texture dizzy;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        hero = new Texture[4];
        hero[0] = new Texture("frame-1.png");
        hero[1] = new Texture("frame-2.png");
        hero[2] = new Texture("frame-3.png");
        hero[3] = new Texture("frame-4.png");
        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        dizzy = new Texture("dizzy-1.png");
        random = new Random();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        heroY = Gdx.graphics.getHeight() / 2;
    }

    public void makeCoin() {
        float height = random.nextFloat() * (Gdx.graphics.getHeight() - coin.getHeight());
        coinY.add((int) height);

        coinX.add(Gdx.graphics.getWidth());
    }

    public void makeBomb() {
        float height = random.nextFloat() * (Gdx.graphics.getHeight() - bomb.getHeight());
        bombY.add((int) height);

        bombX.add(Gdx.graphics.getWidth());
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            // Make bomb interval
            if (bombCount < 150)
                bombCount++;
            else {
                bombCount = 0;
                makeBomb();
            }

            for (int i = 0; i < bombX.size(); i++) {
                batch.draw(bomb, bombX.get(i), bombY.get(i));
                bombX.set(i, bombX.get(i) - 8); //Moves bomb to left
                bombRectangles.add(new Rectangle(bombX.get(i), bombY.get(i), bomb.getWidth(), bomb.getHeight()));
            }

            if (Gdx.input.justTouched()) {  //For every tap, jump +10
                velocity = -10;
            }

            // Make coin interval
            if (coinCount < 150)
                coinCount++;
            else {
                coinCount = 0;
                makeCoin();
            }
            coinRectangles.clear();
            for (int i = 0; i < coinX.size(); i++) {
                batch.draw(coin, coinX.get(i), coinY.get(i));
                coinX.set(i, coinX.get(i) - 4); //Moves coin to left
                coinRectangles.add(new Rectangle(coinX.get(i), coinY.get(i), coin.getWidth(), coin.getHeight()));
            }

            if (pause < 8) {
                pause++;
            } else {
                pause = 0;
                if (heroState < 3)
                    heroState++;
                else
                    heroState = 0;
            }

            velocity = velocity + gravity;
            heroY = heroY - velocity;

            if (heroY <= 0)  //Prevent falling below screen
                heroY = 0;
            else if (heroY >= Gdx.graphics.getHeight() - hero[heroState].getHeight())  //Prevent go above screen
                heroY = Gdx.graphics.getHeight() - hero[heroState].getHeight();

        } else if (gameState == 0) {
            if (Gdx.input.justTouched())
                gameState = 1;
        } else if (gameState == 2) {
            if (Gdx.input.justTouched()) { //restart the game
                gameState = 1;
                heroY = Gdx.graphics.getHeight() / 2;
                score = 0;
                velocity = 0;
                coinX.clear();
                coinY.clear();
                coinRectangles.clear();
                coinCount = 0;

                bombX.clear();
                bombY.clear();
                bombRectangles.clear();
                bombCount = 0;
            }
        }

        if (gameState == 2) {
            batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - hero[heroState].getWidth() / 2, heroY);
        } else
            batch.draw(hero[heroState], Gdx.graphics.getWidth() / 2 - hero[heroState].getWidth() / 2, heroY);

        heroRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - hero[heroState].getWidth() / 2, (int) heroY, hero[heroState].getWidth(), hero[heroState].getHeight());

        //Detect collisions between coin and hero
        for (int i = 0; i < coinRectangles.size(); i++) {
            if (Intersector.overlaps(heroRectangle, coinRectangles.get(i))) {
                score++;
                //If a collision occurs, remove that coin
                coinRectangles.remove(i);
                coinX.remove(i);
                coinY.remove(i);
                break;
            }
        }

        //Detect collisions between bomb and hero
        for (int i = 0; i < bombRectangles.size(); i++) {
            if (Intersector.overlaps(heroRectangle, bombRectangles.get(i))) {
                gameState = 2;
            }
        }

        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
