package sk.tuke.kpi.oop.game.EscapeRoom;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.ActorFactory;

import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.characters.Alien;
import sk.tuke.kpi.oop.game.characters.MotherAlien;
import sk.tuke.kpi.oop.game.items.AccessCard;
import sk.tuke.kpi.oop.game.items.Ammo;
import sk.tuke.kpi.oop.game.items.Energy;
import sk.tuke.kpi.oop.game.behaviours.RandomlyMoving;
import sk.tuke.kpi.oop.game.behaviours.Observing;
import sk.tuke.kpi.oop.game.openables.Door;

public class Factory implements ActorFactory {

    @Override
    public Actor create(String type, String name) {

        if (name.equals("ellen"))
            return new Ripley();

        if (name.equals("ammo"))
            return new Ammo();
        if (name.equals("access card")) return new AccessCard();
        if (name.equals("energy"))
            return new Energy();

        if (name.equals("front door")) {
            Door.Orientation orientation = type.equals("vertical") ? Door.Orientation.VERTICAL : Door.Orientation.HORIZONTAL;
            return new Door(name, orientation);
        }

        if (name.equals("back door")) {
            Door.Orientation orientation = type.equals("vertical") ? Door.Orientation.VERTICAL : Door.Orientation.HORIZONTAL;
            return new Door(name, orientation);
        }

        if (name.equals("exit door")) {
            Door.Orientation orientation = type.equals("vertical") ? Door.Orientation.VERTICAL : Door.Orientation.HORIZONTAL;
            return new Door(name, orientation);
        }

        if (name.equals("alien")) {
            if (type.equals("running")) {
                return new Alien(new RandomlyMoving());
            } else if (type.equals("waiting1")) {
                return new Alien(
                    new Observing<>(
                        Door.DOOR_OPENED,
                        (Door d) -> d.getName().equals("front door"),
                        new RandomlyMoving()
                    )
                );
            } else if (type.equals("waiting2")) {
                return new Alien(
                    new Observing<>(
                        Door.DOOR_OPENED,
                        (Door d) -> d.getName().equals("back door"),
                        new RandomlyMoving()
                    )
                );
            } else {
                return new Alien();
            }
        }

        if (name.equals("alien mother")) {
            if (type.equals("waiting2")) {
                return new MotherAlien(
                    new Observing<>(
                        Door.DOOR_OPENED,
                        (Door d) -> d.getName().equals("back door"),
                        new RandomlyMoving()
                    )
                );
            } else {
                return new MotherAlien(null);
            }
        }

        return null;
    }
}
