import gui.BoardPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.Board;
import logic.GameController;
import logic.NMMBoard;

public class Morris extends Application {

    Board board = new NMMBoard();

    GameController controller = new GameController(board);

    BoardPane boardUi = new BoardPane(controller);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(boardUi, 550, 550));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
