package com.cyrrus.game.entities.weapons;

import static com.cyrrus.game.util.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cyrrus.game.entities.Player;
import com.cyrrus.game.util.MathFun;

public class minigun extends Weapon{

    public final static float RateOfFire = 3.0f; // bullets/round
    public final static float DMG = 100.0f; //100% AD
    public final static int roundsCount = 3;
    private final static float recastWindow = 3.0f;

    private final TextureRegion[] appearAnimationArray;
    private final TextureRegion[] fireAnimationArray;
    private final TextureRegion [] disappearAnimationArray;
    public Animation animation;
    private final static float animationFrameDuration = 0.25f;

    private int status;
    private final static int STATUS_RECALIBERATE = 4;
    private static final int STATUS_FIRING = 3;
    private final static int STATUS_READY_TO_FIRE = 2;
    private final static int STATUS_SET_ME_UP = 1;
    private final static int STATUS_ASLEEP = 0;

    private boolean setIdleimg = false;
    private int rotation = 0; // clockWise
    private final float bulletCD;
    private float AttackTimer = 0.0f;
    private float nextAttackTime = 0.0f;
    private Body miniGunBody;
    private TextureRegion miniGunImg;

    public int roundsSpent = 0;

    private Player player;

    public minigun(Player player) {

        status = STATUS_ASLEEP;
        COOLDOWN = 5.0f;

        this.player = player;
        this.setSize(Player.getSpriteSize()*7.0f/PPM,Player.getSpriteSize()*7.0f/PPM);

        setOriginCenter();//0.164,0.164

        Texture texture = new Texture(Gdx.files.internal("entities/weapons/minigun/mg.png"));
        TextureRegion[][] textureArray =  TextureRegion.split(texture,64,64);
        appearAnimationArray = new TextureRegion[] // set images for appear animation
                {textureArray[2][0],textureArray[2][1], textureArray[0][0],textureArray[0][0]};
        fireAnimationArray = new TextureRegion[] // set the images for fire animation
                {textureArray[0][1],textureArray[1][0],textureArray[1][1]};
        disappearAnimationArray = new TextureRegion[] // set the images to show when disappearing
                {textureArray[0][0],textureArray[2][1],textureArray[2][0]};

        bulletCD = (fireAnimationArray.length*animationFrameDuration)/RateOfFire;
    }

    @Override
    public void fire() {
        if(status == STATUS_ASLEEP) {
            status = STATUS_SET_ME_UP;
            this.setPosition(player.body.getPosition().x - this.getWidth()/2,player.body.getPosition().y - this.getHeight()/2);
            this.trajectory = MathFun.getTrajectory(6, 10.0f,this.getX(), this.getY());
            show();
        }
        else if (status == STATUS_READY_TO_FIRE) {
            status = STATUS_FIRING;
            AttackTimer=0.0f;
            nextAttackTime = AttackTimer + bulletCD;
        }
    }

    @Override
    public void drawWeapon(Batch batch, float delta) {

        this.elapsedTime+=delta;
        if(status == STATUS_READY_TO_FIRE) {
            if(!setIdleimg) {
                this.setRegion(fireAnimationArray[0]);
                setIdleimg=true;
            }
        }
        else {
            this.setRegion((TextureRegion) animation.getKeyFrame(elapsedTime, !(animation.getPlayMode().equals(Animation.PlayMode.NORMAL))));
        }
        this.draw(batch);
    }

    public void show() {
        this.elapsedTime = 0f;
        this.animation = new Animation((appearAnimationArray.length*animationFrameDuration)/ appearAnimationArray.length, appearAnimationArray);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        weaponsList.add(this);
    }

    private void aim() {

        this.trajectory = MathFun.getTrajectory(6, this.getRotation(), player.body.getPosition().x,player.body.getPosition().y);
        setRotation((player.body.getAngle() * MathUtils.radiansToDegrees ) + 90 + rotation);
        this.setPosition(player.body.getPosition().x - this.getWidth()/2,player.body.getPosition().y - this.getHeight()/2);

        if ((status == STATUS_SET_ME_UP)&&this.animation.isAnimationFinished(elapsedTime))
            {
                System.out.println("---- set status to ready");
                status = STATUS_READY_TO_FIRE;
                animation = new Animation((fireAnimationArray.length*animationFrameDuration)/ fireAnimationArray.length, fireAnimationArray);
                animation.setPlayMode(Animation.PlayMode.LOOP);
            }
        else if (status == STATUS_READY_TO_FIRE || status == STATUS_RECALIBERATE) {
            if(counter < recastWindow) {
                counter += Gdx.graphics.getDeltaTime();
                }
                else {
                    if(status != STATUS_RECALIBERATE){
                        elapsedTime=0f;
                        animation = new Animation((disappearAnimationArray.length*animationFrameDuration)/ disappearAnimationArray.length, disappearAnimationArray);
                        animation.setPlayMode(Animation.PlayMode.NORMAL);
                        status = STATUS_RECALIBERATE;
                    }
                    else if (animation.isAnimationFinished(elapsedTime)) {

                        System.out.println("timeout, removing minigun");

                        weaponsList.removeValue(this, false);
                        counter = 0f;
                        setIdleimg = false;
                        status = STATUS_ASLEEP;
                    }
                }
            }

    }

    @Override
    public void update(float delta) {
       if(status != STATUS_ASLEEP) {
           if(status==STATUS_FIRING){
               attack();
           }
           else
            aim();
        }
    }

    private void attack() {
        //check if bullets can still be fired.
        //if timer == the time to shoot next bullet,
        //then shoot a bullet,
        //and update the time to shoot next bullet.
        if(AttackTimer<=RateOfFire*bulletCD) {
            AttackTimer += Gdx.graphics.getDeltaTime();
            if (AttackTimer >= nextAttackTime) {
                nextAttackTime += bulletCD;

                //shoot
            }
        }
        else {
            status = STATUS_READY_TO_FIRE;
        }
    }
}
