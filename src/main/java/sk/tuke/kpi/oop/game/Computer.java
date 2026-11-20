package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;

public class Computer extends AbstractActor implements EnergyConsumer {
    private boolean powered;

    public Computer() {
        powered = false;
        setAnimation(new Animation("sprites/computer.png"));
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public int add(int a, int b) {
        return powered ? a + b : 0;
    }

    public float add(float a, float b) {
        return powered ? a + b : 0f;
    }

    public int sub(int a, int b) {
        return powered ? a - b : 0;
    }

    public float sub(float a, float b) {
        return powered ? a - b : 0f;
    }
}
