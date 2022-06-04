package com.cyrrus.game.screens;

import static com.cyrrus.game.util.Constants.DEFAULT_ZOOM;
import static com.cyrrus.game.util.Constants.GRAVITY;
import static com.cyrrus.game.util.Constants.ViewportSize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cyrrus.game.BoxGame;
import com.cyrrus.game.entities.Player;

import com.cyrrus.game.util.ShapeFactory;

public class PlayScreen implements Screen {
    private final SpriteBatch spriteBatch;
    private final World world ;
    private final Box2DDebugRenderer b2dDebugRenderer;
    private final OrthographicCamera cam;
    private final Viewport vPort;
    public final Player player;

    public PlayScreen(){
        //initialize final variables
        //gravity (x : 0,y : 0)
        //camera zoom = 6x
        world = new World(GRAVITY, true);
        cam = new OrthographicCamera();
        cam.zoom = DEFAULT_ZOOM;
        spriteBatch = new SpriteBatch();
        b2dDebugRenderer = new Box2DDebugRenderer();
        vPort = new FillViewport(ViewportSize[0], ViewportSize[1], cam);

        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set (new Vector2(cam.viewportWidth * .5f, cam.viewportHeight * .5f), 0f);

        player=new Player();
        player.body= ShapeFactory.createRectangle(new Vector2(0f,0f), new Vector2(64f,120f), 0.4f, BodyDef.BodyType.DynamicBody, world);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.1f,0.1f,0.1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(cam.combined);

        b2dDebugRenderer.render(world, cam.combined);

        draw();
        player.update(delta);
        camReposition();
        world.step(delta,6,2);
    }


    private void draw() {
//        draw player
//        player.setPosition(player.body.getPosition().x, player.body.getPosition().y);
//        player.setRotation();
//        player.draw(spriteBatch);

        System.out.println(player.body.getPosition().x+" " + player.body.getPosition().y+" , "+ cam.position.x+ " " + cam.position.y);
    }

    private void camReposition() {

        cam.position.set(player.body.getPosition(),0f);
        cam.update();

    }

    @Override
    public void resize(int width, int height) {
        vPort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        b2dDebugRenderer.dispose();
        world.dispose();
    }
}
