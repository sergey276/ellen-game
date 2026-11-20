package sk.tuke.kpi.oop.game.characters;

import java.util.ArrayList;
import java.util.List;

public class Health {

    private int value;
    private int max;
    private boolean fatigued = false;
    private List<FatigueEffect> effects = new ArrayList<>();

    public Health(int initial, int max) {
        this.value = initial;
        this.max = max;
    }

    public Health(int value) {
        this(value, value);
    }

    public int getValue() {
        return value;
    }

    public void refill(int amount) {
        value = Math.min(max, value + amount);
    }

    public void restore() {
        value = max;
    }

    public void drain(int amount) {
        value = Math.max(0, value - amount);
        if (value == 0 && !fatigued) {
            fatigued = true;
            for (FatigueEffect eff : effects) eff.apply();
        }
    }

    public void exhaust() {
        drain(value);
    }

    public void onFatigued(FatigueEffect effect) {
        effects.add(effect);
    }

    @FunctionalInterface
    public interface FatigueEffect {
        void apply();
    }
}
