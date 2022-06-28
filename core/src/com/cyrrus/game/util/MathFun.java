package com.cyrrus.game.util;

import static com.cyrrus.game.util.Constants.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MathFun {

    private MathFun(){}

    public static Vector2 multiplyVector2(float multiplicand, Vector2 vector2){
        return new Vector2(multiplicand * vector2.x, multiplicand * vector2.y);
    }

//    returns Array of Vector 2 of points in trajectory.
//    takes the number of points in trajectory,
//    the rotation angle of the trajectory,
//    and coordinates of 1st point in trajectory.
    public static Array<Vector2> getTrajectory( int point_count,float angle, float OriginX, float OriginY){

        /*If your starting point is (0,0), and your new point is
        r units away at an angle of θ, you can find the coordinates
        of that point using the equations x = r cosθ and y = r sinθ
        */
        Array<Vector2> res = new Array<>();
        Vector2 next_point;
        Vector2 point1 = new Vector2();
        Vector2 point2 = new Vector2();
        Vector2 last_point = new Vector2();
        Vector2 points_gap_register = new Vector2();

        final float dist;
        float angleOfIncline = angle;
        angleOfIncline *= 3.14f/180.0f;
        //  coordinates of two points of radius at 0 degree angleOfIncline trajectory
        point1.y=point2.y= OriginY;
        point1.x = OriginX;
        point2.x = point1.x + ((point_count*10.0f)/PPM);
        //find the coordinates of the last point
        dist = point2.x - point1.x;
        last_point.x = point1.x + (float) (dist * Math.cos(angleOfIncline));
        last_point.y = point1.y + (float) (dist * Math.sin(angleOfIncline));

        //add first point to res.
        //next_point will be stored to res.
        //update next point.
        res.add(point1);
        next_point = point1.cpy();

        //remove first point(it appears on top of car)

        res.removeIndex(0);

        //set the x and y gaps between two nearby points
        points_gap_register.set((last_point.x - point1.x)/(point_count-1),
                (last_point.y - point1.y)/(point_count-1));

        for (int i = 0; i < point_count-2; i++) {
            next_point.x+=points_gap_register.x;
            next_point.y+=points_gap_register.y;
            res.add(next_point.cpy());
        }
        res.add(last_point);

        return res;
    }

}
