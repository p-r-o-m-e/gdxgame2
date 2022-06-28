package com.cyrrus.game.util;

import static com.cyrrus.game.entities.Player.getSpriteSize;
import static com.cyrrus.game.util.Constants.DEFAULT_MAP;
import static com.cyrrus.game.util.Constants.PPM;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class MapLoader implements Disposable {

    private final World world;
    private final TiledMap tMap;

    private static final String WALL_LAYER = "walls";
    private static final String PLAYER_LAYER = "player";

    public final OrthogonalTiledMapRenderer renderer;

    public MapLoader(World wrld){

        this.world = wrld;
        tMap = new TmxMapLoader().load(DEFAULT_MAP);

        //---------------- RESIZE MAP ---------------
        renderer = new OrthogonalTiledMapRenderer(tMap, tMap.getProperties().get("width", Integer.class)/(PPM*90.0f));

        final Array<RectangleMapObject> walls = tMap.getLayers().get(WALL_LAYER).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rMAPobj: walls
             ) {
            Rectangle RectObj = rMAPobj.getRectangle();
            ShapeFactory.createRectangle(
                    new Vector2(RectObj.x + RectObj.getWidth()/2,RectObj.y + RectObj.getHeight()/2),//position
                    new Vector2(RectObj.getWidth()/2,RectObj.getHeight()/2),//size
                    1f, BodyDef.BodyType.StaticBody, world );
        }
    }

    public Body getPlayerFromMap(){
        final Rectangle RectObj = tMap.getLayers().get(PLAYER_LAYER).getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return ShapeFactory.createRectangle(
                new Vector2(RectObj.x + RectObj.getWidth()/2,RectObj.y + RectObj.getHeight()/2),//position
                new Vector2(getSpriteSize()*3f/2,getSpriteSize()*5f/2),//size
                0.4f, BodyDef.BodyType.DynamicBody, world );

    }

    @Override
    public void dispose() {
        tMap.dispose();
        renderer.dispose();
    }
}
