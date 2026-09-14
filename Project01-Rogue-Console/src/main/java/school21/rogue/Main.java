package school21.rogue;

import school21.rogue.domain.engine.GameEngine;
import school21.rogue.presentation.GameController;
import school21.rogue.presentation.TerminalRenderer;

public class Main {
    public static void main(String[] args) {
        try {
            GameEngine engine = new GameEngine();
            TerminalRenderer renderer = new TerminalRenderer();
            GameController controller = new GameController(engine, renderer);
            controller.run();
        } catch (Exception e) {
            System.err.println("Fatal error in Rogue application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
