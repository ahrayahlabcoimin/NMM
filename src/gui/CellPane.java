package gui;

import javafx.scene.layout.*;
import logic.Cell;

import java.awt.*;


public class CellPane extends Pane {
    public CellPane(Point point, BoardPane boardPane) {

        setStyle("-fx-background-color: transparent");

        setPrefSize(2000, 2000);
        // interface
        setOnMouseClicked(event -> {
            boardPane.onCellClick(point);
        });
    }


    // visual part
    public void setState(Cell.State state) {
        switch(state) {
            case BLACK:
                setStyle("-fx-background-color: black; -fx-background-radius: 100");
                break;
            case WHITE:
                setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4; -fx-background-radius: 100; -fx-border-radius: 100;");
                break;
            case HIGHLIGHTED:
                setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 4; -fx-background-radius: 100; -fx-border-radius: 100;");
                break;
            case VOID:
                setStyle("-fx-background-color: transparent");
                break;
            case EMPTY:
                setStyle(null);
                break;
        }
    }
}
