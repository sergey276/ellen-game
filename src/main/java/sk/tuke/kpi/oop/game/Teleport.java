package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.Player;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class Teleport extends AbstractActor {
    private Teleport destination;
    private final Set<Actor> actorsInside = new HashSet<>();
    private static final Map<Actor, Teleport> arrivalCooldown =
        Collections.synchronizedMap(new WeakHashMap<>());

    public Teleport(Teleport destination) {
        this.destination = destination;
        setAnimation(new Animation("sprites/lift.png"));
    }

    public Teleport() {
        setAnimation(new Animation("sprites/lift.png"));
    }

    public Teleport getDestination() {
        return destination;
    }

    public void setDestination(Teleport destinationTeleport) {
        if (destinationTeleport == this) return;
        this.destination = destinationTeleport;
    }

    private void centerActorHere(Actor actor) {
        float centerX = getPosX() + getWidth() / 2f;
        float centerY = getPosY() + getHeight() / 2f;
        float newX = centerX - actor.getWidth() / 2f;
        float newY = centerY - actor.getHeight() / 2f;
        actor.setPosition((int) newX, (int) newY);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::checkAndTeleportPlayers)).scheduleFor(this);
    }

    private void checkAndTeleportPlayers() {
        Scene scene = getScene();
        if (scene == null) return;
        for (Actor a : scene.getActors()) {
            if (a instanceof Player) processActor(a);
        }
    }

    private void processActor(Actor a) {
        boolean inside = isActorCenterInside(a);
        boolean wasInside = actorsInside.contains(a);
        if (inside && !wasInside) handleEnter(a);
        if (inside) actorsInside.add(a);
        else handleExit(a);
    }

    private void handleEnter(Actor a) {
        Teleport dest = getDestination();
        if (dest == null || dest == this) return;
        Teleport cool = arrivalCooldown.get(a);
        if (cool == this) return;

        float destCenterX = dest.getPosX() + dest.getWidth() / 2f;
        float destCenterY = dest.getPosY() + dest.getHeight() / 2f;
        float newX = destCenterX - a.getWidth() / 2f;
        float newY = destCenterY - a.getHeight() / 2f;

        a.setPosition((int) newX, (int) newY);
        arrivalCooldown.put(a, dest);
        dest.centerActorHere(a);
    }

    private void handleExit(Actor a) {
        actorsInside.remove(a);
        Teleport cool = arrivalCooldown.get(a);
        if (cool == this) arrivalCooldown.remove(a);
    }

    private boolean isActorCenterInside(Actor actor) {
        float px = actor.getPosX() + actor.getWidth() / 2f;
        float py = actor.getPosY() + actor.getHeight() / 2f;
        return px >= getPosX() && px <= getPosX() + getWidth()
            && py >= getPosY() && py <= getPosY() + getHeight();
    }
}
