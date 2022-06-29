package com.cyrrus.game.util;

import static com.cyrrus.game.util.Constants.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public abstract class ShapeFactory {

    private static Array<Body> destroyBodyQueue = new Array<>();

    public static void addToDestroyBodyQueue(Body body){
        if(!destroyBodyQueue.contains(body, true))
        destroyBodyQueue.add(body);
    }

    public static Body createCircle(final Vector2 pos, final float radius,
                                    final float density, final BodyDef.BodyType bodyType,
                                    final World world)
    {
        final BodyDef bDef = new BodyDef();
        bDef.position.set(pos.x/PPM,pos.y/PPM);
        bDef.type=bodyType;
        final Body body = world.createBody(bDef);

        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        //create Fixture
        body.createFixture(circleShape,density);

        circleShape.dispose();
        return body;
    }

    public static Body createRectangle(final Vector2 pos , final Vector2 size,final float density, final BodyDef.BodyType bodyType, final World world)
    {
     //define body
     final BodyDef bDef = new BodyDef();
     bDef.position.set(pos.x/PPM,pos.y/PPM);
     bDef.type = bodyType;//Dynamic, Static, or Kinematic
     final Body body = world.createBody(bDef);

     final PolygonShape pShape = new PolygonShape();
     pShape.setAsBox(size.x/PPM, size.y/PPM);
     final FixtureDef fDef =  new FixtureDef();
     fDef.shape = pShape;
     fDef.density = density;

     body.createFixture(fDef);
     pShape.dispose();

     return body;
    }

    public static void destroyBodies() {
        if(!destroyBodyQueue.isEmpty()) {
            for (Body body : destroyBodyQueue
            ) {
                body.getWorld().destroyBody(body);
            }
            destroyBodyQueue.clear();
        }
    }
}
