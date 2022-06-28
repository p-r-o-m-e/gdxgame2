package com.cyrrus.game.screens;

import static com.cyrrus.game.entities.weapons.Weapon.weaponUpdate;
import static com.cyrrus.game.entities.weapons.Weapon.weaponsList;
import static com.cyrrus.game.util.Constants.DEFAULT_ZOOM;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_DOWN;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_NONE;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_UP;
import static com.cyrrus.game.util.Constants.GRAVITY;
import static com.cyrrus.game.util.Constants.PPM;
import static com.cyrrus.game.util.Constants.PosIterations;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_LEFT;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_NONE;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_RIGHT;
import static com.cyrrus.game.util.Constants.VeloIterations;
import static com.cyrrus.game.util.Constants.ViewportSize;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cyrrus.game.entities.Player;
import com.cyrrus.game.entities.weapons.Weapon;
import com.cyrrus.game.util.InputHandler;
import com.cyrrus.game.util.MapLoader;

public class PlayScreen implements Screen {
    private final Batch gameBatch;
    private final World world ;
    private final Box2DDebugRenderer b2dDebugRenderer;
    private final OrthographicCamera cam;
    private final Viewport vPort;
    public final Player player;
    private final MapLoader mapLoader;
    private final InputHandler INPctrl;

    private final ShapeRenderer shapeRenderer;

    public PlayScreen(){
        //initialize final variables
        //gravity (x : 0,y : 0)
        //camera zoom = 6x
        world = new World(GRAVITY, true);
        cam = new OrthographicCamera();
        cam.zoom = DEFAULT_ZOOM;
        b2dDebugRenderer = new Box2DDebugRenderer();
        vPort = new FillViewport(ViewportSize[0], ViewportSize[1], cam);
        player=new Player();
        mapLoader = new MapLoader(world);
        gameBatch = mapLoader.renderer.getBatch();
        shapeRenderer = new ShapeRenderer();

        //initialise camera viewport size and position
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set (new Vector2(cam.viewportWidth * .5f, cam.viewportHeight * .5f), 0f);

        //extra operations that require being performed upon start.
        player.body= mapLoader.getPlayerFromMap();
        player.setTurnDirection(TURN_DIRECTION_NONE);
        player.setDriveDirection(DRIVE_DIRECTION_NONE);
        player.setOriginCenter();
        shapeRenderer.setColor(Color.LIME);
        INPctrl= new InputHandler(this);
        Gdx.input.setInputProcessor(INPctrl);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(.1f,0.1f,0.1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        ScreenUtils.clear(0.2f,0.41f,0.494f,1); //darker sand color
        ScreenUtils.clear(0.28235f,0.545f,0.83f,1);
//        Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());

        gameBatch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
        mapLoader.renderer.setView(cam);

        mapLoader.renderer.render();
        b2dDebugRenderer.render(world, cam.combined);

        //draw
        gameBatch.begin();
        draw(delta);
        gameBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawShapes();
        shapeRenderer.end();
        //some update methods , that are called repeatedly.
        player.update(delta);
        weaponUpdate(delta);
        camReposition();
        world.step(delta,VeloIterations,PosIterations);
    }

    private void drawShapes() {

        //draw trajectory
        if (!weaponsList.isEmpty()) {
            System.out.println("drawing shapes");
            for (Weapon weapon:weaponsList)
            {
                System.out.print(" ( "+ weapon.trajectory.size +" )");
                shapeRenderer.line(weapon.trajectory.first(),weapon.trajectory.get(weapon.trajectory.size-1));
//                for (Vector2 v:weapon.trajectory
//                ) {
//                    shapeRenderer.circle(v.x,v.y, Player.getSpriteSize()*3.0f/PPM);
//                }
            }
            System.out.println("");
        }

    }


    private void draw(float delta) {
//        draw player
        player.setPosition(player.body.getPosition().x - player.getWidth()/2, player.body.getPosition().y-player.getHeight()/2);
        player.setRotation(player.body.getAngle() * MathUtils.radiansToDegrees);
        player.drawPlayer(gameBatch);

        //draw weapons
        if(!weaponsList.isEmpty()) {
            for (Weapon weapon:weaponsList)
            {
                weapon.drawWeapon(gameBatch, delta);
            }
        }
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

    public void KeyUpNotif( int keycode) {

        if( keycode == Input.Keys.A){
        if (INPctrl.keyPressed(Input.Keys.D)){
            player.setTurnDirection(TURN_DIRECTION_RIGHT);
        }
        else
            player.setTurnDirection(TURN_DIRECTION_NONE);
        }
        else if( keycode == Input.Keys.D){
            if (INPctrl.keyPressed(Input.Keys.A)){
                player.setTurnDirection(TURN_DIRECTION_LEFT);
            }
            else
                player.setTurnDirection(TURN_DIRECTION_NONE);
        }
        }

    public void KeyDownNotif(int keycode) {
        if(keycode == Input.Keys.W){
            player.setDriveDirection(DRIVE_DIRECTION_UP);
        }
        else if(keycode == Input.Keys.S){
            player.setDriveDirection(DRIVE_DIRECTION_DOWN);
        }
        else
        {
            if(keycode == Input.Keys.A){
                player.setTurnDirection(TURN_DIRECTION_LEFT);
            }
            else if(keycode == Input.Keys.D){
                player.setTurnDirection(TURN_DIRECTION_RIGHT);
            }
            else if (keycode == Input.Keys.DOWN){
                player.minigun.fire();
            }
            else {
                if(keycode == Input.Keys.ESCAPE)Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        b2dDebugRenderer.dispose();
        world.dispose();
        mapLoader.dispose();
    }
}
