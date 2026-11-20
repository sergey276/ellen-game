package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Cooler extends AbstractActor implements Switchable {
    private Reactor reactor;
    private boolean on;
    private final Animation fan = new Animation("sprites/fan.png", 32, 32, 0.2f);

    public Cooler() {
        setAnimation(fan);
        fan.stop();
        on = false;
    }

    public Cooler(Reactor reactor) {
        this();
        setReactor(reactor);
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public void turnOn() {
        on = true;
        fan.play();
    }

    @Override
    public void turnOff() {
        on = false;
        fan.stop();
    }

    @Override
    public boolean isOn() {
        return on;
    }

    protected Reactor getReactor() {
        return reactor;
    }

    private void coolReactor() {
        if (reactor == null) return;
        if (on) reactor.decreaseTemperature(2);
    }

    @Override
    public void addedToScene(sk.tuke.kpi.gamelib.Scene scene) {
        super.addedToScene(scene);
        new sk.tuke.kpi.gamelib.framework.actions.Loop<>(
            new sk.tuke.kpi.gamelib.actions.Invoke<>(this::coolReactor)
        ).scheduleFor(this);
    }
}
