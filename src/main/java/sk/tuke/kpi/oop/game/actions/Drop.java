package sk.tuke.kpi.oop.game.actions;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import sk.tuke.kpi.oop.game.Keeper;
import sk.tuke.kpi.oop.game.items.BreakableTool;
import sk.tuke.kpi.oop.game.items.Collectible;

public class Drop<A extends Keeper> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {

        if (getActor() == null) {
            setDone(true);
            return;
        }

        Keeper keeper = getActor();

        if (keeper.getScene() == null) {
            setDone(true);
            return;
        }

        Scene scene = keeper.getScene();

        Collectible item = keeper.getBackpack().peek();

        if (item == null) {
            setDone(true);
            return;
        }

        keeper.getBackpack().remove(item);

        int x = keeper.getPosX() + keeper.getWidth() / 2 - item.getWidth() / 2;
        int y = keeper.getPosY() + keeper.getHeight() / 2 - item.getHeight() / 2;

       
        scene.addActor(item, x, y);

        if (item instanceof BreakableTool<?>) {
            BreakableTool<?> tool = (BreakableTool<?>) item;
            if (tool.getRemainingUses() == 0) {
                scene.removeActor(tool);
            }
        }

        setDone(true);
    }
}
