package ru.mts.tictactoe.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Board — игровое поле")
class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(3, 3);
    }

    @Nested
    @DisplayName("Создание поля")
    class ConstructorTests {

        @Test
        @DisplayName("создаёт квадратное поле 3x3")
        void createsSquareBoard() {
            Board b = new Board(3, 3);
            assertEquals(3, b.getWidth());
            assertEquals(3, b.getHeight());
            assertEquals(3, b.getWinLength());
        }

        @Test
        @DisplayName("создаёт поле 5x5 с длиной победы 4")
        void createsLargerBoardWithCustomWinLength() {
            Board b = new Board(5, 4);
            assertEquals(5, b.getWidth());
            assertEquals(5, b.getHeight());
            assertEquals(4, b.getWinLength());
        }

        @Test
        @DisplayName("выбрасывает исключение при размере меньше 3")
        void throwsOnTooSmallSize() {
            assertThrows(IllegalArgumentException.class, () -> new Board(2, 3));
            assertThrows(IllegalArgumentException.class, () -> new Board(3, 2, 3));
        }

        @Test
        @DisplayName("выбрасывает исключение при длине победы меньше 3")
        void throwsOnTooSmallWinLength() {
            assertThrows(IllegalArgumentException.class, () -> new Board(3, 2));
        }

        @Test
        @DisplayName("выбрасывает исключение при длине победы больше размера поля")
        void throwsOnWinLengthExceedsBoardSize() {
            assertThrows(IllegalArgumentException.class, () -> new Board(3, 4));
        }
    }

    @Nested
    @DisplayName("Ходы")
    class MoveTests {

        @Test
        @DisplayName("успешный ход в пустую клетку")
        void makeMoveToEmptyCell() {
            assertTrue(board.makeMove(0, 0, Player.X));
            assertEquals(Player.X, board.getCell(0, 0));
        }

        @Test
        @DisplayName("ход в занятую клетку возвращает false")
        void makeMoveToOccupiedCellReturnsFalse() {
            board.makeMove(1, 1, Player.X);
            assertFalse(board.makeMove(1, 1, Player.O));
            assertEquals(Player.X, board.getCell(1, 1));
        }

        @Test
        @DisplayName("выбрасывает исключение при координатах вне поля")
        void throwsOnOutOfBoundsCoordinates() {
            assertThrows(IndexOutOfBoundsException.class, () -> board.makeMove(-1, 0, Player.X));
            assertThrows(IndexOutOfBoundsException.class, () -> board.makeMove(3, 0, Player.X));
            assertThrows(IndexOutOfBoundsException.class, () -> board.makeMove(0, 3, Player.X));
        }

        @Test
        @DisplayName("выбрасывает исключение при null игроке")
        void throwsOnNullPlayer() {
            assertThrows(IllegalArgumentException.class, () -> board.makeMove(0, 0, null));
        }

        @Test
        @DisplayName("отмена хода очищает клетку")
        void undoMoveClearsCell() {
            board.makeMove(0, 0, Player.X);
            board.undoMove(0, 0);
            assertTrue(board.isEmpty(0, 0));
        }
    }

    @Nested
    @DisplayName("Проверка победы")
    class WinDetectionTests {

        @Test
        @DisplayName("горизонтальная победа X")
        void horizontalWinForX() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.X);
            board.makeMove(2, 0, Player.X);

            assertTrue(board.checkWin(Player.X));
            assertFalse(board.checkWin(Player.O));
        }

        @Test
        @DisplayName("вертикальная победа O")
        void verticalWinForO() {
            board.makeMove(1, 0, Player.O);
            board.makeMove(1, 1, Player.O);
            board.makeMove(1, 2, Player.O);

            assertTrue(board.checkWin(Player.O));
            assertFalse(board.checkWin(Player.X));
        }

        @Test
        @DisplayName("диагональная победа (главная диагональ)")
        void mainDiagonalWin() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.X);
            board.makeMove(2, 2, Player.X);

            assertTrue(board.checkWin(Player.X));
        }

        @Test
        @DisplayName("диагональная победа (побочная диагональ)")
        void antiDiagonalWin() {
            board.makeMove(2, 0, Player.O);
            board.makeMove(1, 1, Player.O);
            board.makeMove(0, 2, Player.O);

            assertTrue(board.checkWin(Player.O));
        }

        @Test
        @DisplayName("нет победы при неполной линии")
        void noWinWithIncompleteLine() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.X);
            // третья клетка пуста

            assertFalse(board.checkWin(Player.X));
        }

        @Test
        @DisplayName("нет победы при прерванной линии")
        void noWinWithBrokenLine() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.O);
            board.makeMove(2, 0, Player.X);

            assertFalse(board.checkWin(Player.X));
        }

        @ParameterizedTest
        @CsvSource({
            "0,0, 1,0, 2,0, 3,0, 4,0",
            "0,2, 1,2, 2,2, 3,2, 4,2",
            "2,0, 2,1, 2,2, 2,3, 2,4"
        })
        @DisplayName("победа 5 в ряд на поле 5x5")
        void fiveInARowWin(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int x5, int y5) {
            Board largeBoard = new Board(5, 5);
            largeBoard.makeMove(x1, y1, Player.X);
            largeBoard.makeMove(x2, y2, Player.X);
            largeBoard.makeMove(x3, y3, Player.X);
            largeBoard.makeMove(x4, y4, Player.X);
            largeBoard.makeMove(x5, y5, Player.X);

            assertTrue(largeBoard.checkWin(Player.X));
        }
    }

    @Nested
    @DisplayName("Ничья и заполненность")
    class DrawTests {

        @Test
        @DisplayName("пустое поле не заполнено")
        void emptyBoardIsNotFull() {
            assertFalse(board.isFull());
        }

        @Test
        @DisplayName("частично заполненное поле не полное")
        void partialBoardIsNotFull() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.O);
            assertFalse(board.isFull());
        }

        @Test
        @DisplayName("полностью заполненное поле")
        void fullBoardIsFull() {
            // X O X
            // X X O
            // O X O
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.O);
            board.makeMove(2, 0, Player.X);
            board.makeMove(0, 1, Player.X);
            board.makeMove(1, 1, Player.X);
            board.makeMove(2, 1, Player.O);
            board.makeMove(0, 2, Player.O);
            board.makeMove(1, 2, Player.X);
            board.makeMove(2, 2, Player.O);

            assertTrue(board.isFull());
        }
    }

    @Nested
    @DisplayName("Вспомогательные методы")
    class UtilityTests {

        @Test
        @DisplayName("getEmptyCells возвращает все пустые клетки")
        void getEmptyCellsReturnsAll() {
            List<int[]> empty = board.getEmptyCells();
            assertEquals(9, empty.size());
        }

        @Test
        @DisplayName("getEmptyCells уменьшается после хода")
        void getEmptyCellsDecreasesAfterMove() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.O);
            
            List<int[]> empty = board.getEmptyCells();
            assertEquals(7, empty.size());
        }

        @Test
        @DisplayName("clear очищает всё поле")
        void clearResetsBoard() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.O);
            
            board.clear();
            
            assertEquals(9, board.getEmptyCells().size());
            assertTrue(board.isEmpty(0, 0));
            assertTrue(board.isEmpty(1, 1));
        }

        @Test
        @DisplayName("getWinner возвращает победителя")
        void getWinnerReturnsWinner() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.X);
            board.makeMove(2, 0, Player.X);

            assertTrue(board.getWinner().isPresent());
            assertEquals(Player.X, board.getWinner().get());
        }

        @Test
        @DisplayName("getWinner возвращает empty если нет победителя")
        void getWinnerReturnsEmptyWhenNoWinner() {
            board.makeMove(0, 0, Player.X);
            assertTrue(board.getWinner().isEmpty());
        }
    }
}
