package sk.tuke.kpi.oop.game.openables;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.items.Usable;

import static sk.tuke.kpi.oop.game.openables.Door.Orientation.VERTICAL;

public class Door extends AbstractActor implements Openable, Usable<Actor> {

    public static final Topic<Door> DOOR_OPENED =
        Topic.create("door opened", Door.class);

    public static final Topic<Door> DOOR_CLOSED =
        Topic.create("door closed", Door.class);

    public static final Topic<Door> DOOR_OPENED_TOPIC = DOOR_OPENED;
    public static final Topic<Door> DOOR_CLOSED_TOPIC = DOOR_CLOSED;

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    private final Animation anim;
    private boolean open;
    private Orientation orientation;

    public Door() {
        this(VERTICAL);
    }

    public Door(Orientation orientation) {
        this("door", orientation);
    }

    public Door(String name, Orientation orientation) {
        super(name);
        this.orientation = orientation;

        if (orientation == Orientation.HORIZONTAL) {
            anim = new Animation("sprites/hdoor.png", 32, 16, 0.1f);
        } else {
            anim = new Animation("sprites/vdoor.png", 16, 32, 0.1f);
        }

        setAnimation(anim);

        open = true;
        close();
    }

    @Override
    public void useWith(Actor actor) {
        if (open) {
            close();
        } else {
            open();
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }

    @Override
    public void open() {
        if (open) return;

        open = true;
        anim.setPlayMode(Animation.PlayMode.ONCE);
        anim.play();
        updateTiles(MapTile.Type.CLEAR);

        Scene scene = getScene();
        if (scene != null) {
            scene.getMessageBus().publish(DOOR_OPENED, this);
        }
    }

    @Override
    public void close() {
        if (!open) return;

        open = false;
        anim.setPlayMode(Animation.PlayMode.ONCE_REVERSED);
        anim.play();
        updateTiles(MapTile.Type.WALL);

        Scene scene = getScene();
        if (scene != null) {
            scene.getMessageBus().publish(DOOR_CLOSED, this);
        }
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    private void updateTiles(MapTile.Type type) {
        Scene scene = getScene();
        if (scene == null) return;

        if (orientation == Orientation.HORIZONTAL) {
            int tileX = getPosX() / 16;
            int tileY = (getPosY() + getHeight() / 2) / 16;

            MapTile left = scene.getMap().getTile(tileX, tileY);
            MapTile right = scene.getMap().getTile(tileX + 1, tileY);

            if (left != null) left.setType(type);
            if (right != null) right.setType(type);
        } else { // VERTICAL
            int tileX = (getPosX() + getWidth() / 2) / 16;
            int tileY = getPosY() / 16;

            MapTile bottom = scene.getMap().getTile(tileX, tileY);
            MapTile top = scene.getMap().getTile(tileX, tileY + 1);

            if (bottom != null) bottom.setType(type);
            if (top != null) top.setType(type);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        open = true;
        close();
    }
}
