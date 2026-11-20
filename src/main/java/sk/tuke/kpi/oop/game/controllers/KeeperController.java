package sk.tuke.kpi.oop.game.controllers;

import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.BreakableTool;
import sk.tuke.kpi.oop.game.items.Collectible;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.actions.Take;
import sk.tuke.kpi.oop.game.actions.Drop;
import sk.tuke.kpi.oop.game.actions.Shift;
import sk.tuke.kpi.oop.game.actions.Use;
import sk.tuke.kpi.oop.game.openables.Door;
import sk.tuke.kpi.oop.game.openables.LockedDoor;
import sk.tuke.kpi.oop.game.Locker;
import sk.tuke.kpi.oop.game.Repairable;


public class KeeperController implements KeyboardListener {

    private final Keeper keeper;

    public KeeperController(Keeper keeper) {
        this.keeper = keeper;
    }

    @Override
    public void keyPressed(Input.Key key) {
        if (keeper == null || keeper.getScene() == null) return;


        switch (key) {

            case ENTER:
                new Take<Keeper>().scheduleFor(keeper);
                break;

            case BACKSPACE:
                new Drop<Keeper>().scheduleFor(keeper);
                break;

            case S:
                new Shift<Keeper>().scheduleFor(keeper);
                break;

            case B:
                handleBKey();
                break;

            case U:
                handleUKey();
                break;

            default:
                break;
        }
    }

    private Actor findFrontActor() {

        float cx = keeper.getPosX() + keeper.getWidth() / 2f;
        float cy = keeper.getPosY() + keeper.getHeight() / 2f;

        for (Actor actor : keeper.getScene().getActors()) {
            if (actor == keeper) continue;

            float ax = actor.getPosX();
            float ay = actor.getPosY();

            if (Math.abs(ax - cx) < 40 && Math.abs(ay - cy) < 40) {
                return actor;
            }
        }

        return null;
    }

    private void handleBKey() {
        Actor front = findFrontActor();
        Collectible inHand = keeper.getBackpack().peek();

        if (inHand == null) return;
        if (!(inHand instanceof Usable<?>)) return;
        if (front == null) return;
        if (!keeper.intersects(front)) return;

        if (inHand instanceof AccessCard && front instanceof LockedDoor) {
            new Use<>((AccessCard) inHand).scheduleFor((LockedDoor) front);
            return;
        }

        Usable<?> usable = (Usable<?>) inHand;

        if (usable.getUsingActorClass().isInstance(front)) {
            @SuppressWarnings("unchecked")
            Usable<Actor> usableForActor = (Usable<Actor>) usable;

            new Use<>(usableForActor).scheduleFor(usable.getUsingActorClass().cast(front));
        }
    }


    private void handleUKey() {
        Actor obj = findFrontActor();
        if (obj == null) return;

        if (handleLocker(obj)) return;
        if (handleLockedDoor(obj)) return;
        if (handleDoor(obj)) return;
        if (handleUsableObject(obj)) return;
        handleRepairable(obj);
    }

    private boolean handleLocker(Actor obj) {
        if (obj instanceof Locker) {
            new Use<>((Locker) obj).scheduleFor(keeper);
            return true;
        }
        return false;
    }

    private boolean handleLockedDoor(Actor obj) {
        if (obj instanceof LockedDoor) {
            LockedDoor door = (LockedDoor) obj;
            if (!door.isLocked()) {
                if (door.isOpen()) door.close();
                else door.open();
            }
            return true;
        }
        return false;
    }

    private boolean handleDoor(Actor obj) {
        if (obj instanceof Door) {
            Door door = (Door) obj;
            if (door.isOpen()) door.close();
            else door.open();
            return true;
        }
        return false;
    }

    private boolean handleUsableObject(Actor obj) {
        if (obj instanceof Usable<?>) {
            @SuppressWarnings("unchecked")
            Usable<Actor> usableObj = (Usable<Actor>) obj;
            new Use<>(usableObj).scheduleForIntersectingWith(keeper);
            return true;
        }
        return false;
    }

    private void handleRepairable(Actor obj) {
        Collectible item = keeper.getBackpack().peek();
        if (obj instanceof Repairable && item instanceof Usable<?>) {
            Usable<?> usable = (Usable<?>) item;

            if (usable instanceof BreakableTool<?>) {
                BreakableTool<?> tool = (BreakableTool<?>) usable;
                tool.setOwner(keeper);
                tool.setTarget(obj);
            }

            new Use<>(usable).scheduleForIntersectingWith(keeper);
        }
    }
}
