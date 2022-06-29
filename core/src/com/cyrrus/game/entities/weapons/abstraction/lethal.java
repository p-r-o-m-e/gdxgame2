package com.cyrrus.game.entities.weapons.abstraction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class lethal {

    protected Sprite sprite;
    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public void setPosition(float x, float y)
    {
     sprite.setPosition(x,y);
    }

    public void setRotation(float rotation){
     sprite.setRotation(rotation);
    }

    public void setSize(float v, float v1){
        sprite.setSize(v,v1);
    }
    public Vector2 getSize(){
        return new Vector2(sprite.getWidth(),sprite.getHeight());
    }

    public void setOriginCenter(){
        sprite.setOriginCenter();
    }
}
