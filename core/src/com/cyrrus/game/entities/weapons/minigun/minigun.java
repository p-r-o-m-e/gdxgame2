package com.cyrrus.game.entities.weapons.minigun;

import static com.cyrrus.game.util.Constants.PPM;
import static com.cyrrus.game.util.MathFun.getAngularImpulse;
import static com.cyrrus.game.util.ShapeFactory.addToDestroyBodyQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.cyrrus.game.entities.Player;
import com.cyrrus.game.entities.weapons.abstraction.Weapon;
import com.cyrrus.game.util.MathFun;
import com.cyrrus.game.util.ShapeFactory;

public class minigun extends Weapon {

    public final static float RateOfFire = 3.0f; // bullets/round
    public final static float DMG = 100.0f; //100% AD
    public final static int roundsCount = 3;
    private final static float recastWindow = 3.0f;
    private final static float bulletsSpeed = 1.0f;
    private final float bulletCD;

    private final TextureRegion[] appearAnimationArray;
    private final TextureRegion[] fireAnimationArray;
    private final TextureRegion [] disappearAnimationArray;
    public Animation animation;
    private final static float animationFrameDuration = 0.25f;
    private final Array<bullet> bulletList;
    private final Player player;

    public int roundsSpent = 0;

    private int status;
    private final static int STATUS_RECALIBERATE = 4;
    private static final int STATUS_FIRING = 3;
    private final static int STATUS_READY_TO_FIRE = 2;
    private final static int STATUS_SET_ME_UP = 1;
    private final static int STATUS_ASLEEP = 0;

    private boolean setIdleimg = false;
    private int rotation = 0; // clockWise
    private float AttackTimer = 0.0f;
    private float nextAttackTime = 0.0f;
    private TextureRegion miniGunImg;

    public minigun(Player player) {

        status = STATUS_ASLEEP;
        COOLDOWN = 5.0f;

        bulletList = new Array<>();

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
            bulletList.clear();
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

        //draw bullets

        for (bullet b:bulletList
             ) {
            b.setPosition(b.bulletBody.getPosition().x - b.getSize().x/2,b.bulletBody.getPosition().y - b.getSize().y/2);
            b.draw(batch);
        }

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

                //attack once after setting up
                fire();

                setIdleimg=false;
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

                        for (bullet b:bulletList
                        ) {
                            addToDestroyBodyQueue(b.bulletBody);
                        }
                        bulletList.clear();
                    }
                    else if (animation.isAnimationFinished(elapsedTime)) {

                        System.out.println("timeout, removing minigun");

                        setIdleimg = false;

                        weaponsList.removeValue(this, false);
                        counter = 0f;
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
            aim();
        }
    }

    //----------- Spend 1 round of bullets -------
    private void attack() {

        final int directionFlag;
        if(this.getRotation() > 0.0f)
            directionFlag =  1;
        else
            directionFlag = -1;
        final Vector2 bulletPos;
        final Vector2 bulletSize;
        final Vector2 impulseVector;
        //check if bullets can still be fired.
        //if timer == the time to shoot next bullet,
        //then shoot a bullet,
        //and update the time to shoot next bullet.
        if(AttackTimer<=RateOfFire*bulletCD) {
            AttackTimer += Gdx.graphics.getDeltaTime();
            if (AttackTimer >= nextAttackTime) {
                nextAttackTime += bulletCD;

                //shoot
                //generate bullet
                bulletSize = new Vector2((player.getWidth()/25.0f)*PPM,(player.getHeight()/25.0f)*PPM);
                bulletPos = new Vector2(trajectory.get(0).x*PPM - bulletSize.x/2,
                        trajectory.get(0).y*PPM - bulletSize.y/2);
                bulletList.add(new bullet(ShapeFactory.createRectangle(bulletPos, bulletSize, 0.01f,
                                BodyDef.BodyType.DynamicBody,player.body.getWorld()))
                );
//                bulletList.get(bulletList.size-1).bulletBody;

                //set bullet sprite rotation, size, and origin
                bulletList.get(bulletList.size-1).setRotation( player.body.getAngle() * MathUtils.radiansToDegrees + rotation + 90);
                bulletList.get(bulletList.size-1).setSize(player.getWidth()/4.0f,player.getHeight()/8.0f);
                bulletList.get(bulletList.size-1).setOriginCenter();

//                final float AngularImpulse = getAngularImpulse(bulletList.get(bulletList.size-1).bulletBody.getAngle(),
//                        bulletList.get(bulletList.size-1).bulletBody.getAngularVelocity(), player.body.getAngle(),
//                        bulletList.get(bulletList.size-1).bulletBody.getInertia());
//                System.out.println("AngularImpulse : "+ AngularImpulse);
//                bulletList.get(bulletList.size-1).bulletBody.applyAngularImpulse(AngularImpulse, true);

                bulletList.get(bulletList.size-1).bulletBody.setTransform(bulletList.get(bulletList.size-1).bulletBody.getPosition(),
                        player.boady.getAngle());
                impulseVector = new Vector2((float) Math.sin(bulletList.get(bulletList.size-1).bulletBody.getAngle()-Math.PI),
                        (float) Math.cos( bulletList.get(bulletList.size-1).bulletBody.getAngle())
                                );
                impulseVector.set(bulletsSpeed*(impulseVector.x / 5.0f), bulletsSpeed*(impulseVector.y/5.0f));
                bulletList.get(bulletList.size-1).bulletBody.applyLinearImpulse(new Vector2((impulseVector.x / PPM)* Gdx.graphics.getDeltaTime(),(impulseVector.y/PPM) * Gdx.graphics.getDeltaTime()),
                        new Vector2(bulletList.get(bulletList.size-1).bulletBody.getWorldCenter().x,bulletList.get(bulletList.size-1).bulletBody.getWorldCenter().y) ,
                        true
                );

            }
        }
        else {
            status = STATUS_READY_TO_FIRE;
            counter=0.0f;

            for (bullet b:bulletList
                 ) {
                addToDestroyBodyQueue(b.bulletBody);
            }
            bulletList.clear();
        }
    }
}
