package logic;

import java.awt.*;
import java.util.Vector;

public class Player {
    private Vector<Marble> onHand = new Vector<>(20);
    private Vector<Marble> onBoard = new Vector<>(20);
    private Marble selectedMarble;
    private Cell.State cellState;
    private int millsFormed = 0;

    public Player(Cell.State cellState, int defaultMarbles) {
        this.cellState = cellState;
        addMarbles(defaultMarbles);
    }

    public boolean hasSelectedMarble() {
        return selectedMarble != null;
    }

    public Point getSelectedMarblePosition() {
        return selectedMarble.getPosition();
    }

    private void addMarbles(int numMarblesToAdd) {
        for (int i = 0; i < numMarblesToAdd; i++) {
            onHand.add(new Marble());
        }
    }

    public void placeMarbleOnBoard(Board board, Point position) {
        Marble marble = onHand.remove(onHand.size() - 1);
        marble.setPosition(position);
        onBoard.add(marble);
        board.updateCell(position, getColorCellState());
    }

    public void moveMarble(Board board, Point to) {
        board.updateCell(selectedMarble.getPosition(), Cell.State.EMPTY);
        selectedMarble.setPosition(to);
        board.updateCell(to, getColorCellState());
        unselectMarble();
    }

    public void removeBoardMarbleAt(Board board, Point point) {
        Marble marbleToRemove = null;
        for (int i = 0; i < onBoard.size(); i++) {
            Marble targetMarble = onBoard.get(i);
            if (targetMarble.getPosition().equals(point)) {
                onBoard.remove(i);
                marbleToRemove = targetMarble;
                break;
            }
        }
        if (marbleToRemove != null) {
           board.updateCell(point, Cell.State.EMPTY);
        }
    }

    public Cell.State getColorCellState() {
        return cellState;
    }

    public Cell.State getOpponentColorCellState() {
        return cellState.complement();
    }

    public boolean atFirstStage() {
        return onHand.size() > 0;
    }

    public void selectMarbleAt(Point position) {
        for (int i = 0; i < onBoard.size(); i++) {
            Marble targetMarble = onBoard.get(i);
            if (targetMarble.getPosition().equals(position)) {
                selectedMarble = targetMarble;
                return;
            }
        }
    }


    public void unselectMarble() {
        selectedMarble = null;
    }

    public boolean canMarblesFly() {
        return getTotalMarbles() == 3;
    }


    public void setNumberMarblesToRemove(int millsFormed) {
        this.millsFormed = millsFormed;
    }

    public boolean canRemoveOpponentMarbles() {
        return millsFormed > 0;
    }


    public void decrementNumberMarblesToRemove() {
        millsFormed--;
    }

    public int getTotalMarbles() {
        return onBoard.size() + onHand.size();
    }

    public Vector<Marble> getMarblesOnBoard() {
        return onBoard;
    }
}
