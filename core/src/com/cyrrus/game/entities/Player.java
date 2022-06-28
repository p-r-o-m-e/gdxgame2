package com.cyrrus.game.entities;

import static com.cyrrus.game.util.Constants.DEFAULT_ZOOM;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_DOWN;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_NONE;
import static com.cyrrus.game.util.Constants.DRIVE_DIRECTION_UP;
import static com.cyrrus.game.util.Constants.PPM;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_LEFT;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_NONE;
import static com.cyrrus.game.util.Constants.TURN_DIRECTION_RIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cyrrus.game.entities.weapons.minigun;
import com.cyrrus.game.util.MathFun;

public class Player extends Sprite {

    public final minigun minigun;

    public Body body;
    private TextureRegion playerIMG;
    private TextureRegion[][] textureArray;
    private Vector2 ForceVector;

    public static float getSpriteSize() {
        return SPRITE_SIZE;
    }

    private static final float TURN_SPEED = 1.4f;
    private static final float DRIVE_SPEED = 2f;
    private static final float DRIFT = 0.9f;
    private static final float MAX_SPEED = 1.3f;

    private final static float SPRITE_SIZE = 3f;

    public void setDriveDirection(int driveDirection) {
        DriveDirection = driveDirection;
    }

    public void setTurnDirection(int turnDirection) {
        TurnDirection = turnDirection;
    }

    private int TurnDirection;
    private int DriveDirection;

    public Player() {
        Texture texture = new Texture(Gdx.files.internal("entities/ts.gif"));
        textureArray = TextureRegion.split(texture, 16, 16);
        playerIMG = textureArray[0][0];
        this.setRegion(playerIMG);
        this.setSize(getSpriteSize()*8.0f/PPM,getSpriteSize()*8.0f/PPM);
        ForceVector = new Vector2(0f,0f);

        minigun = new minigun(this);
    }
    public void drawPlayer(Batch batch){
        this.draw(batch);
    }

    public Vector2 getFwrdVelocity(){
        Vector2 currentNormal = body.getWorldVector(new Vector2(0f,1f));
        float dotProduct = currentNormal.dot(body.getLinearVelocity());
        return MathFun.multiplyVector2(dotProduct, currentNormal);
    }

    public Vector2 getLateralVelocity() {
        Vector2 currentNormal = body.getWorldVector(new Vector2(1f,0f));
        float dotProduct = currentNormal.dot(body.getLinearVelocity());
        return MathFun.multiplyVector2(dotProduct, currentNormal);
    }

    public void update(float delta){

        //check Drive Direction
        if(DriveDirection==DRIVE_DIRECTION_UP){
            this.ForceVector.set(0f,DRIVE_SPEED * Gdx.graphics.getDeltaTime());
        }
        else if(DriveDirection==DRIVE_DIRECTION_DOWN){
            this.ForceVector.set(0f, DRIVE_SPEED * -1 * Gdx.graphics.getDeltaTime());
        }
        if(!ForceVector.isZero()  && body.getLinearVelocity().len() < MAX_SPEED)
            body.applyForceToCenter(body.getWorldVector(ForceVector), true);

        //Check Turn Direction
        if(TurnDirection==TURN_DIRECTION_RIGHT){
            this.body.setAngularVelocity(-TURN_SPEED);
        }
        else if(TurnDirection==TURN_DIRECTION_LEFT){
            this.body.setAngularVelocity(TURN_SPEED);
        }
        else if(this.body.getAngularVelocity()!=0f&&TurnDirection==TURN_DIRECTION_NONE){
            this.body.setAngularVelocity(0f);
        }

        handleDrift();
    }

    private void handleDrift() {
        body.setLinearVelocity(getFwrdVelocity().x+getLateralVelocity().x*DRIFT,
                getFwrdVelocity().y + getLateralVelocity().y*DRIFT);
    }
}
