import logic.Board;
import logic.Cell;
import logic.NMMBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    Board board;

    @BeforeEach
    public void setup() {
        board = new NMMBoard();
    }

    @Test
    public void testGridSize() {
        assertEquals(board.getGridSize(), NMMBoard.GRID_SIZE);
    }

    @Test
    public void nonEmptyCell() {
        assertNotEquals(board.getCell(0,0), null);
    }

    @Test
    public void validCellMarkedEmpty() {
        assertEquals(board.getCell(0,0).getState(), Cell.State.EMPTY);
        assertEquals(board.getCell(6,6).getState(), Cell.State.EMPTY);
        assertEquals(board.getCell(0,6).getState(), Cell.State.EMPTY);
        assertEquals(board.getCell(6,0).getState(), Cell.State.EMPTY);
    }

    @Test
    public void invalidCellMarkedVoid() {
        assertEquals(board.getCell(3,3).getState(), Cell.State.VOID);
    }

    @Test
    public void validCellSelections() {
        board.updateCell(new Point(0,0), Cell.State.BLACK);
        assertTrue(board.validCellSelection(new Point(0,0), Cell.State.BLACK));
    }

    @Test
    public void detectOneMillFormed() {
        int y = 3;
        for (int i = 0; i < 3; i++) {
            board.updateCell(new Point(i,y), Cell.State.BLACK);
        }
        assertEquals(1, board.countMillsFormedAt(new Point(2,3), Cell.State.BLACK));
    }

    @Test
    public void detectTwoMillsFormed() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < 3; i++) {
            board.updateCell(new Point(x ,y), Cell.State.BLACK);
            board.updateCell(new Point(y, x), Cell.State.BLACK);
            x += 3;
        }
        assertEquals(2, board.countMillsFormedAt(new Point(0,0), Cell.State.BLACK));
    }
}
