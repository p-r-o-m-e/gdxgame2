package com.cyrrus.game.util;

import com.badlogic.gdx.math.Vector2;

public abstract class Constants {
    //Declare and intitialise constants
    public static final Vector2 GRAVITY = new Vector2(0f,0f);
    public static final float DEFAULT_ZOOM = 6f;
    public static final float PPM = 50.0f;
    public static final float[] ViewportSize = {300/PPM, 240/PPM};
}
