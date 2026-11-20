package sk.tuke.kpi.oop.game;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Loop;


public class SmartCooler extends Cooler {
    public SmartCooler() {
        super();
    }

    public SmartCooler(Reactor reactor) {
        super(reactor);
    }

    private void control() {
        Reactor r = getReactor();
        if (r == null) return;
        int t = r.getTemperature();
        if (t > 2500 && !isOn()) turnOn();
        if (t < 1500 && isOn()) turnOff();
    }

    @Override
    public void addedToScene(sk.tuke.kpi.gamelib.Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::control)).scheduleFor(this);
    }
}
