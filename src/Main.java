import controller.GameController;

public class Main {
    public static void main(String[] args) {

        String musicPath = "src/resources/music/menu.wav";
        String fontName = "Blade Runner Movie Font";

        GameController gameController = new GameController(musicPath, fontName);
        gameController.startGame();

    }
}