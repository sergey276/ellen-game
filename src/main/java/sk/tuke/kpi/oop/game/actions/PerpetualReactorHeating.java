package sk.tuke.kpi.oop.game.actions;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Reactor;

public class PerpetualReactorHeating extends AbstractAction<Reactor> {
    private final int step;

    public PerpetualReactorHeating(int step) {
        this.step = step;
    }

    @Override
    public void execute(float deltaTime) {
        if (isDone()) return;
        Reactor r = getActor();
        if (r == null) { setDone(true); return; }
        r.increaseTemperature(step);
    }

    @Override
    public void reset() {
        setDone(false);
    }
}
