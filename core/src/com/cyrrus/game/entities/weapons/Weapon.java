package com.cyrrus.game.entities.weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Weapon extends Sprite {

   protected float COOLDOWN;
   //timer for animation
   protected float elapsedTime;
   //timer for aiming
   protected float counter = 0f;
   public Array<Vector2> trajectory = new Array<>();

   public static Array<Weapon> weaponsList = new Array <>();
   public static  void weaponUpdate(float delta){
      for (Weapon weapon:weaponsList
           ) {
         weapon.update(delta);
      }
   }

   public abstract void fire();
   public abstract void update(float delta);
   public abstract void drawWeapon(Batch batch, float delta);
}
