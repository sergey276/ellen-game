package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Light extends AbstractActor implements EnergyConsumer, Switchable {
    private boolean isOn;
    private boolean isPowered;

    private final Animation onAnim  = new Animation("sprites/light_on.png");
    private final Animation offAnim = new Animation("sprites/light_off.png");

    public Light() {
        setAnimation(offAnim);
        isOn = false;
        isPowered = false;
    }

    private void updateAnimation() {
        if (isOn && isPowered) setAnimation(onAnim);
        else setAnimation(offAnim);
    }

    @Override
    public void setPowered(boolean powered) {
        isPowered = powered;
        updateAnimation();
    }

    @Override
    public void turnOn() {
        isOn = true;
        updateAnimation();
    }

    @Override
    public void turnOff() {
        isOn = false;
        updateAnimation();
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public void toggle() {
        isOn = !isOn;
        updateAnimation();
    }
}
