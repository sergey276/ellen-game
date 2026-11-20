package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class PowerSwitch extends AbstractActor {
    private final Switchable device;

    public PowerSwitch(Switchable device) {
        this.device = device;
        setAnimation(new Animation("sprites/switch.png"));
    }

    public Switchable getDevice() {
        return device;
    }

    public void switchOn() {
        if (device != null) device.turnOn();
    }

    public void switchOff() {
        if (device != null) device.turnOff();
    }

    public void toggle() {
        if (device == null) return;
        if (device.isOn()) device.turnOff();
        else device.turnOn();
    }
}
