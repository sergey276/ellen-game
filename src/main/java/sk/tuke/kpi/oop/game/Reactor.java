
package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.oop.game.actions.PerpetualReactorHeating;

import java.util.HashSet;
import java.util.Set;

public class Reactor extends AbstractActor implements Switchable, Repairable {
    private int temperature;
    private int damage;
    private boolean running;
    private Animation normalAnimation;
    private Animation hotAnimation;
    private Animation brokenAnimation;
    private Animation offAnimation;
    private Animation extinguishedAnimation;

    private final Set<EnergyConsumer> devices = new HashSet<>();

    public Reactor() {
        temperature = 0;
        damage = 0;
        running = false;

        offAnimation = new Animation("sprites/reactor.png");
        normalAnimation = new Animation("sprites/reactor_on.png", 80, 80, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        hotAnimation = new Animation("sprites/reactor_hot.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        brokenAnimation = new Animation("sprites/reactor_broken.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        try {
            extinguishedAnimation = new Animation("sprites/reactor_extinguished.png", 80, 80, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        } catch (RuntimeException e) {
            extinguishedAnimation = brokenAnimation;
        }
        setAnimation(offAnimation);
    }

    public int getTemperature() { return temperature; }
    public int getDamage() { return damage; }
    @Override public boolean isOn() { return running; }

    public void increaseTemperature(int increment) {
        if (increment <= 0 || !running || damage >= 100) return;

        temperature += computeTemperatureIncrease(increment);
        updateDamageFromTemperature();

        if (isDestroyed()) {
            destroy();
            return;
        }
        updateAnimation();
    }

    private int computeTemperatureIncrease(int increment) {
        if (damage <= 33) return increment;
        if (damage <= 66) return (int) (increment * 1.5);
        return increment * 2;
    }

    private void updateDamageFromTemperature() {
        if (temperature > 2000) {
            int computed = (temperature - 2000) / 40;
            if (computed > damage) damage = Math.min(100, computed);
        }
    }

    private boolean isDestroyed() {
        return temperature >= 6000 || damage >= 100;
    }

    private void destroy() {
        damage = 100;
        turnOff();
        setAnimation(brokenAnimation);
        updatePowerToDevices();
    }

    public void decreaseTemperature(int decrement) {
        if (decrement <= 0 || !running || damage >= 100) return;

        if (damage >= 50) temperature -= decrement / 2;
        else temperature -= decrement;

        if (temperature < 0) temperature = 0;
        updateAnimation();
    }

    private void updateAnimation() {
        if (damage >= 100) setAnimation(brokenAnimation);
        else if (!running) setAnimation(offAnimation);
        else if (temperature > 4000) setAnimation(hotAnimation);
        else setAnimation(normalAnimation);
    }

    @Override
    public void repair() {
        if (damage < 50 || damage >= 100);

        int newDamage = Math.max(0, damage - 50);
        int newTemp = newDamage == 0 ? 2000 : 2000 + newDamage * 40;
        if (newTemp < temperature) temperature = newTemp;

        damage = newDamage;
        updateAnimation();
        updatePowerToDevices();
    }

    public boolean extinguish() {
        if (damage < 100 || temperature <= 4000) return false;

        temperature = 4000;
        setAnimation(extinguishedAnimation);
        running = false;
        updatePowerToDevices();
        return true;
    }

    @Override
    public void turnOn() {
        if (damage >= 100) return;
        running = true;
        updateAnimation();
        updatePowerToDevices();
    }

    @Override
    public void turnOff() {
        running = false;
        updateAnimation();
        updatePowerToDevices();
    }

    public void addDevice(EnergyConsumer device) {
        if (device == null) return;
        if (devices.add(device)) {
            device.setPowered(running && damage < 100);
        }
    }

    public void removeDevice(EnergyConsumer device) {
        if (device == null) return;
        if (devices.remove(device)) {
            device.setPowered(false);
        }
    }

    private void updatePowerToDevices() {
        boolean produces = running && damage < 100;
        for (EnergyConsumer d : devices) {
            d.setPowered(produces);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new PerpetualReactorHeating(1).scheduleFor(this);
    }
}
