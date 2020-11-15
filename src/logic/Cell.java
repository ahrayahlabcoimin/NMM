package logic;

import java.awt.*;

public class Cell {

    public enum State {
        VOID,
        EMPTY,
        HIGHLIGHTED,
        BLACK,
        WHITE;

        public State complement() {
            switch(this) {
                case BLACK:
                    return WHITE;
                case WHITE:
                    return BLACK;
                case VOID:
                    return EMPTY;
                default:
                    break;
            }
            return EMPTY;
        }
    }

    private State state = State.VOID;

    public void setState(State newState) {
        state = newState;
    }

    public State getState() {
        return state;
    }


    public boolean isEmpty() {
        return state == State.EMPTY;
    }

    public boolean isHighlighted() {
        return state == State.HIGHLIGHTED;
    }

    public boolean isOwnedByAPlayer() {
        return state == State.BLACK || state == State.WHITE;
    }

}
