package com.cyrrus.game.entities.weapons.minigun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.cyrrus.game.entities.weapons.abstraction.lethal;

public class bullet extends lethal {

    public Body bulletBody;
    public bullet(Body body){
        bulletBody = body;
        sprite = new Sprite();
        sprite.setOriginCenter();
        sprite.setRegion(new Texture(Gdx.files.internal("entities/weapons/minigun/bullet.png")));
    }

}
