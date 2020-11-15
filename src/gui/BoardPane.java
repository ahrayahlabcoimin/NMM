package gui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import logic.Board;
import logic.Cell;
import logic.GameController;

import java.awt.*;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class BoardPane extends GridPane implements PropertyChangeListener {
    private GameController controller;
    private CellPane[][] grid;

    public BoardPane(GameController controller) {
        this.controller = controller;
        this.setMaxSize(500,500);
        this.setMinSize(500,500);
        setup();
    }


    private void setup() {
        setupBackgroundImage();
        setupGrid();
        setupListener();
    }

    private void setupListener() {
        controller.getBoard().addPropertyChangeListener(this);
    }

    private void setupBackgroundImage() {
        //create the background image from a url. Need changing the basis to the file
        Image image = new Image("https://www.iconfinder.com/data/icons/toys-2/512/game-6-512.png",550,550,false,true);

        BackgroundImage emptyBoard = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        setBackground(new Background(emptyBoard));
    }

    private void setupGrid() {
        int gridSize = controller.getBoard().getGridSize();
        grid = new CellPane[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++)
            for (int column = 0; column < gridSize; column++) {
                CellPane cellPane =  new CellPane(new Point(column, row),this);
                grid[row][column] = cellPane;
                add(cellPane, column, row);
            }
    }

    public void onCellClick(Point position) {
        controller.makeMove(position);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if ("grid".equals(name)) {
            IndexedPropertyChangeEvent indexEvt = (IndexedPropertyChangeEvent) evt;
            int index = indexEvt.getIndex();
            Board source = (Board)evt.getSource();
            Point position = source.getPointFromIndex(index);
            int x = (int)position.getX(), y = (int)position.getY();
            grid[y][x].setState((Cell.State)evt.getNewValue());
        }
    }
}
