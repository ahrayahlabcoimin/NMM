package logic;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

public abstract class Board {
    protected int gridSize;
    private Cell[][] grid;

    private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    public Board(int gridSize) {
        this.gridSize = gridSize;
        setup();
    }



    public void setup() {
        initializeGrid();
        markValidPositions();
    }

    protected void initializeGrid() {
        if (grid != null) {
            return;
        }
        grid = new Cell[gridSize][gridSize];
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                grid[y][x] = new Cell();
            }
        }
    }

    public int getGridSize() {
        return gridSize;
    }

    abstract void markValidPositions();


    public void removeMarbleAt(Point pos) {
        if (getCell(pos).isOwnedByAPlayer()) {
            updateCell(pos, Cell.State.EMPTY);
        }
    }

    public void updateCell(Point pos, Cell.State newState) {
        String propertyName = "grid";
        Cell.State oldState = getCell(pos).getState();
        int index = getIndexFromPoint(pos);
        propChangeSupport.fireIndexedPropertyChange(propertyName, index, oldState, newState);
        getCell(pos).setState(newState);
    }


    public Cell getCell(Point pos) {
        return getCell((int)pos.getX(),(int)pos.getY());
    }

    public Cell getCell(int x, int y) {
        if (x >= gridSize || x < 0 || y >= gridSize || y < 0) {
            return null;
        }
        return grid[y][x];
    }

    public Point getPointFromIndex(int index) {
        int x = index%gridSize;
        int y = index/gridSize;
        return new Point(x,y);
    }

    public int getIndexFromPoint(Point pos) {
        return (int)(pos.getX() + pos.getY() * gridSize);
    }

    public boolean validCellSelection(Point pos, Cell.State matchState) {
        return getCell(pos).getState() == matchState;
    }

    public boolean validCellFlyMove(Point pos) {
        return getCell(pos).getState() == Cell.State.EMPTY;
    }

    abstract boolean validCellAdjacentMove(Point from, Point to);

    abstract Vector<Point> getValidAdjacentMoves(Point from);

    abstract int maxPieces();

    public abstract Vector<Point> getEmptyCellPositions();

    public abstract int countMillsFormedAt(Point position, Cell.State matchState);
}
