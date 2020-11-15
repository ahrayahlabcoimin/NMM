package logic;

import java.awt.*;
import java.nio.MappedByteBuffer;
import java.util.Vector;

public class NMMBoard extends Board {
    public static int GRID_SIZE = 7;

    public NMMBoard() {
        super(NMMBoard.GRID_SIZE);
    }

    @Override
    void markValidPositions() {
        int midpoint = gridSize/2;
        int max = 2 * midpoint;
        for (int i = 0; i < gridSize; i++) {
            if (i != midpoint) {
                // center X top to bottom
                getCell(midpoint,i).setState(Cell.State.EMPTY);

                // center Y left to right
                getCell(i, midpoint).setState(Cell.State.EMPTY);

                // diagonal from (0,0) to (6,6)
                getCell(i,i).setState(Cell.State.EMPTY);

                // diagonal from (0,6) to (6,0)
                int y = max - i;
                getCell(i, y).setState(Cell.State.EMPTY);
            }
        }
    }

    @Override
    boolean validCellAdjacentMove(Point from, Point to) {
        return false;
    }


    @Override
    public Vector<Point> getValidAdjacentMoves(Point fromPoint) {
        Vector<Point> allAdjacentMoves = getAdjacentSpots(fromPoint);
        Vector<Point> validEmptyCellMoves = new Vector<>(4);


        // randomly throws nullpointer exception should check
        for (Point position: allAdjacentMoves) {
            if (getCell(position).isEmpty()) {
                validEmptyCellMoves.add(position);
            }
        }

        return validEmptyCellMoves;

    }

    public Vector<Point> getAdjacentSpots(Point from) {
        int x = (int)from.getX(),y=(int)from.getY();

        int midpoint = gridSize/2;
        int max = midpoint * 2;
        Vector<Point> validMoves = new Vector<>(4);

        if (x == midpoint) {
            int xStepSize = Math.abs(midpoint - y);
            int leftAdjacentX = x - xStepSize;
            int rightAdjacentX = x + xStepSize;
            if (leftAdjacentX >= 0) {
                validMoves.add(new Point(leftAdjacentX, y));
            }
            if (rightAdjacentX <= max) {
                validMoves.add(new Point(rightAdjacentX, y));
            }

            int offsetY = y < midpoint ? 0 : midpoint;
            int refY = y < midpoint ? y : Math.abs(midpoint - y);

            for(int i = Math.max(0, refY - 1); i <= Math.min(refY + 1, midpoint); i++) {
                if(refY != i) {
                    validMoves.add(new Point(x, i + offsetY));
                }
            }

        } else {
            validMoves.add(new Point(midpoint, y));
        }

        if (y == midpoint) {
            int yStepSize = Math.abs(midpoint - x);
            int upAdjacentY = y - yStepSize;
            int downAdjacentY = y + yStepSize;
            if (upAdjacentY >= 0) {
                validMoves.add(new Point(x, upAdjacentY));
            }
            if (downAdjacentY <= max) {
                validMoves.add(new Point(x, downAdjacentY));
            }

            int offsetX = x < midpoint ? 0 : midpoint;
            int refX = x < midpoint ? x : Math.abs(midpoint - x);
            for(int i = Math.max(0, refX - 1); i <= Math.min(refX + 1,midpoint); i++) {
                if(refX != i) {
                    validMoves.add(new Point(i + offsetX, y));
                }
            }
        } else {
            validMoves.add(new Point(x, midpoint));
        }
        return validMoves;
    }

    private Point[] getHorizontalMill(Point point) {
        Point[] millPoints = new Point[3];
        int midpoint = gridSize/2;
        int x = (int)point.getX(),y= (int) point.getY();
        if (y == midpoint) {
            int min = x < midpoint ? 0 : 4;
            int max = min + 3;
            for (int i = min, index = 0; i < max; i++, index++) {
                millPoints[index] = new Point(i, y);
            }
        } else {
            int min;
            int boardColumnMax = midpoint * 2;
            if (x < midpoint) {
                min = x;
            } else if (x == midpoint) {
                min = y < x ? y : boardColumnMax - y;
            } else {
                min = boardColumnMax - x;
            }
            int steps = midpoint - min;
            for (int i = 0; i < 3; i++) {
                millPoints[i] = new Point(min + i * steps, y);
            }
        }

        return millPoints;
    }

    private Point[] getVerticalMill(Point point) {
        int x = (int)point.getX(),y = (int) point.getY();

        Point[] flippedMillPoints = getHorizontalMill(new Point(y, x));
        Point[] millPoints = new Point[3];
        for(int i = 0; i < 3; i++) {
            Point aFlippedPoint = flippedMillPoints[i];
            int newX = (int)aFlippedPoint.getY();
            int newY = (int)aFlippedPoint.getX();
            millPoints[i] = new Point(newX,newY);
        }

        return millPoints;
    }


    @Override
    public int countMillsFormedAt(Point position, Cell.State matchState) {
        int millsFormed = 0;
        Point[] potentialHorizontalMill = getHorizontalMill(position);

        int matchingState = 0;
        for (Point aMillPoint: potentialHorizontalMill) {
            if (getCell(aMillPoint).getState() == matchState) {
                matchingState++;
            }
        }
        if (matchingState == 3) {
            millsFormed++;
        }

        matchingState = 0;

        Point[] potentialVerticalMill = getVerticalMill(position);

        for (Point aMillPoint: potentialVerticalMill) {
            if (getCell(aMillPoint).getState() == matchState) {
                matchingState++;
            }
        }

        if (matchingState == 3) {
            millsFormed++;
        }


        return millsFormed;
    }

    @Override
    public int maxPieces() {
        return 9;
    }

    @Override
    public Vector<Point> getEmptyCellPositions() {
        Vector<Point> emptyPositions = new Vector<>(20);
        int midpoint = gridSize/2;
        int max = 2 * midpoint;
        for (int i = 0; i < gridSize; i++) {
            if (i != midpoint) {
                // center X top to bottom
                Point centerXPoint = new Point(midpoint,i);
                if (getCell(centerXPoint).isEmpty()) {
                    emptyPositions.add(centerXPoint);
                }

                // center Y left to right
                Point centerYPoint = new Point(i, midpoint);
                if (getCell(centerYPoint).isEmpty()) {
                    emptyPositions.add(centerYPoint);
                }

                // diagonal from (0,0) to (6,6)
                Point diagNegSlopePoint = new Point(i,i);
                if (getCell(diagNegSlopePoint).isEmpty()) {
                    emptyPositions.add(diagNegSlopePoint);
                }

                // diagonal from (0,6) to (6,0)
                int y = max - i;
                Point diagPosSlopePoint = new Point(i,y);
                if (getCell(diagPosSlopePoint).isEmpty()) {
                    emptyPositions.add(diagPosSlopePoint);
                }
            }
        }
        return emptyPositions;
    }
}
