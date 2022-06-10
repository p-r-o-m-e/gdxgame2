package com.cyrrus.game.util;

import com.badlogic.gdx.math.Vector2;

public class MathFun {

    private MathFun(){}

    public static Vector2 multiplyVector2(float multiplicand, Vector2 vector2){
        return new Vector2(multiplicand * vector2.x, multiplicand * vector2.y);
    }

}
