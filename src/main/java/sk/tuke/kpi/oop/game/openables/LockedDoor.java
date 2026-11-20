package sk.tuke.kpi.oop.game.openables;
import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.characters.Armed;
import sk.tuke.kpi.oop.game.characters.Ripley;

public class LockedDoor extends Door {
    private boolean locked = true;

    public void lock() {
        locked = true;
        close();
    }

    public void unlock() {
        locked = false;
        open();
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void useWith(Actor actor) {
        if (locked) return;
        super.useWith(actor);
    }
}

