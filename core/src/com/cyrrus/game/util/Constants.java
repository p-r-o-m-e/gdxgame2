package com.cyrrus.game.util;

import com.badlogic.gdx.math.Vector2;

public abstract class Constants {
    //Declare and intitialise constants
    public static final Vector2 GRAVITY = new Vector2(0f,0f);
    public static final float DEFAULT_ZOOM = 1.7f;
    public static final float PPM = 30.0f;
    public static final float[] ViewportSize = {300/PPM, 240/PPM};
    public static final int VeloIterations = 6;
    public static final int PosIterations = 2;
    public static final String DEFAULT_MAP = "map/tmap2.tmx";
    public static final int DRIVE_DIRECTION_NONE = 0;
    public static final int DRIVE_DIRECTION_UP = 1;
    public static final int DRIVE_DIRECTION_DOWN = 2;
    public static final int TURN_DIRECTION_NONE = 0;
    public static final int TURN_DIRECTION_LEFT = 1;
    public static final int TURN_DIRECTION_RIGHT = 2;

}
