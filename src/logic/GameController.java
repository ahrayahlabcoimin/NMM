package logic;

import java.awt.*;
import java.util.Vector;

public class GameController {
    private Board board;

    private Vector<Player> players = new Vector<>(2);

    private Vector<Point> validMoves;

    private int turn = 0;
    private int millsFormed = 0;
    private boolean gameOver = false;
    public GameController(Board board) {
        this.board = board;
        setupPlayers();
    }

    private void setupPlayers() {
        players.add(new Player(Cell.State.BLACK, board.maxPieces()));
        players.add(new Player(Cell.State.WHITE, board.maxPieces()));
    }


    public Board getBoard() {
        return board;
    }

    public void makeMove(Point position) {
        if (gameOver) return;
        Player currentPlayer = players.get(turn%players.size());
        Player opponentPlayer = players.get((turn + 1)%players.size());
        boolean subPhaseFinished = false;
        boolean canSwitchTurn = false;

        if (currentPlayer.canRemoveOpponentMarbles()) {
            canSwitchTurn = thirdStageMove(position, currentPlayer, opponentPlayer);
        } else if (currentPlayer.atFirstStage()) {
            subPhaseFinished = firstStageMove(position, currentPlayer);
        } else {
            subPhaseFinished = secondStageMove(position, currentPlayer);

        }

        if (subPhaseFinished) {
            // check how many mills form from this position
            // if zero then switch turn
            int millFormed = board.countMillsFormedAt(position, currentPlayer.getColorCellState());
            if (millFormed > 0) {
                currentPlayer.setNumberMarblesToRemove(millFormed);
            } else {
                canSwitchTurn = true;
            }
        }

        if (canSwitchTurn) {
            boolean notEnoughMarbles = opponentPlayer.getTotalMarbles() == 2;
            if (notEnoughMarbles) {
                // game over
                gameOver = true;
            } else if (!opponentPlayer.atFirstStage()) {
                if (!opponentPlayer.canMarblesFly()) {
                    int unmovableMarblesCount = 0;
                    for (Marble marble: opponentPlayer.getMarblesOnBoard()) {
                        int moveCount = board.getValidAdjacentMoves(marble.getPosition()).size();
                        if (moveCount == 0) {
                            unmovableMarblesCount++;
                        }
                    }
                    if (opponentPlayer.getTotalMarbles() == unmovableMarblesCount) {
                        gameOver = true;
                    }
                }
            }

            if (gameOver) {
                System.out.println(opponentPlayer.getColorCellState() + " lost");
                return;
            }

            // check for gameOverState
            // if not game over then switch turn
            turn++;
        }
    }

    private boolean thirdStageMove(Point position, Player currentPlayer, Player opponent) {
        Cell.State opponentColor = opponent.getColorCellState();

        // check 1 it's actually an opponent color
        boolean validOpponentCell = board.validCellSelection(position, opponentColor);

        // check 2 check if it's part of a mill
        if (validOpponentCell) {
            int millsFormed = board.countMillsFormedAt(position, opponentColor);
            int piecesInMill = 1 + (2 * millsFormed);

            if (millsFormed > 0 && opponent.getTotalMarbles() != piecesInMill) {
                validOpponentCell = false;
            }
        }

        if (validOpponentCell) {
            currentPlayer.decrementNumberMarblesToRemove();
            opponent.removeBoardMarbleAt(board, position);
        }


        return validOpponentCell && !currentPlayer.canRemoveOpponentMarbles();
    }

    private boolean firstStageMove(Point position, Player currentPlayer) {
        boolean validMove = board.validCellFlyMove(position);
        if (validMove) {
            currentPlayer.placeMarbleOnBoard(board, position);
        }
        return validMove;
    }

    private boolean secondStageMove(Point position, Player currentPlayer) {

        // sub stage 1: select piece, return false

        if (!currentPlayer.hasSelectedMarble()) {
            secondStageCellSelection(position, currentPlayer);
            return false;
        }
        // sub stage 2: place move, fail return false

        // after both success, return true
        return secondStageCellPlacement(position, currentPlayer);
    }

    private boolean secondStageCellPlacement(Point position, Player currentPlayer) {
        Cell targetCell = board.getCell(position);

        boolean finishCellPlacement = targetCell.isHighlighted();
        boolean shouldUnselect = position == currentPlayer.getSelectedMarblePosition();
        boolean clearSelected = finishCellPlacement || shouldUnselect;

        if (shouldUnselect) {
            currentPlayer.unselectMarble();
        }

        if (clearSelected) {
            for(Point pos : validMoves) {
                board.updateCell(pos, Cell.State.EMPTY);
            }
        }

        if (finishCellPlacement) {
            currentPlayer.moveMarble(board, position);
        }

        return finishCellPlacement;
    }

    private void secondStageCellSelection(Point position, Player currentPlayer) {
        boolean validSelection = board.validCellSelection(position, currentPlayer.getColorCellState());
        if (!validSelection) {
            return;
        }

        currentPlayer.selectMarbleAt(position);

        if (currentPlayer.canMarblesFly()) {
            validMoves = board.getEmptyCellPositions();
        } else {
            validMoves = board.getValidAdjacentMoves(position);
        }

        if (validMoves.size() == 0) {
            // no valid adjacent moves
            currentPlayer.unselectMarble();
            // emit an error
        } else {
            for(Point pos : validMoves) {
                board.updateCell(pos, Cell.State.HIGHLIGHTED);
            }
        }
    }


}
