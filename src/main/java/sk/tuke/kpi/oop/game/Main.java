package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Game;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.WindowSetup;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import sk.tuke.kpi.gamelib.World;
import sk.tuke.kpi.oop.game.scenarios.EscapeRoom;
import sk.tuke.kpi.oop.game.EscapeRoom.Factory;

public class Main {
    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Project Ellen", 800, 600);
        Game game = new GameApplication(windowSetup, new LwjglBackend());

        Scene mission = new World(
            "escape room",
            "maps/escape-room.tmx",
            new Factory()
        );

        game.addScene(mission);

        mission.addListener(new EscapeRoom());

        game.start();
    }
}
