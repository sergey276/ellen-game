package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Disposable;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;

import java.util.concurrent.ThreadLocalRandom;

public class DefectiveLight extends Light implements Repairable {
    private final Runnable flicker = this::maybeToggle;
    private Disposable flickerLoop;
    private boolean inCooldown;

    public DefectiveLight() {
        super();
        setAnimation(new Animation("sprites/light_off.png"));
    }

    private void maybeToggle() {
        if (inCooldown || !isPowered()) return;
        if (ThreadLocalRandom.current().nextInt(5) == 0) {
            toggle();
        }
    }

    private void startFlicker() {
        if (flickerLoop != null) return;
        flickerLoop = new Loop<>(
            new ActionSequence<>(
                new Wait<>(0.1f),
                new Invoke<>(flicker)
            )
        ).scheduleFor(this);
    }

    private void stopFlicker() {
        if (flickerLoop != null) {
            flickerLoop.dispose();
            flickerLoop = null;
        }
    }

    @Override
    public void setPowered(boolean powered) {
        super.setPowered(powered);
        if (powered && !inCooldown) startFlicker();
        else stopFlicker();
    }

    @Override
    public void repair() {
        if (inCooldown) ;
        inCooldown = true;
        stopFlicker();
        new ActionSequence<>(
            new Wait<>(10f),
            new Invoke<>(this::endCooldown)
        ).scheduleFor(this);

    }

    private void endCooldown() {
        inCooldown = false;
        if (isPowered()) startFlicker();
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        if (isPowered() && !inCooldown) startFlicker();
    }
}
