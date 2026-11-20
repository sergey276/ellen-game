package sk.tuke.kpi.oop.game.items;

import sk.tuke.kpi.gamelib.ActorContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private final String name;
    private final int capacity;
    private final List<Collectible> items;

    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    @Override
    public void add(Collectible actor) {
        if (items.size() >= capacity)
            throw new IllegalStateException(name + " is full");
        items.add(actor);
    }

    @Override
    public void remove(Collectible actor) {
        items.remove(actor);
    }

    @Override
    public void shift() {
        if (!items.isEmpty())
            Collections.rotate(items, 1);   // ✅ зсув вправо
    }

    @Override
    public Collectible peek() {
        if (items.isEmpty()) return null;
        return items.get(items.size() - 1);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public List<Collectible> getContent() {
        return List.copyOf(items);
    }

    @Override
    public java.util.Iterator<Collectible> iterator() {
        return items.iterator();
    }
}
