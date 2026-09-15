package ru.mts.tictactoe.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.mts.tictactoe.model.Board;
import ru.mts.tictactoe.model.Player;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AiPlayer — искусственный интеллект")
class AiPlayerTest {

    private AiPlayer ai;
    private Board board;

    @BeforeEach
    void setUp() {
        ai = new AiPlayer(new Random(42));
        board = new Board(3, 3);
    }

    @Nested
    @DisplayName("Выигрышный ход")
    class WinningMoveTests {

        @Test
        @DisplayName("AI делает выигрышный ход когда может")
        void aiMakesWinningMove() {
            // O O _
            // X X _
            // _ _ _
            board.makeMove(0, 0, Player.O);
            board.makeMove(1, 0, Player.O);
            board.makeMove(0, 1, Player.X);
            board.makeMove(1, 1, Player.X);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(2, move[0]);
            assertEquals(0, move[1]);
        }

        @Test
        @DisplayName("AI завершает диагональную победу")
        void aiCompletesDiagonalWin() {
            // O _ X
            // _ O _
            // X _ _
            board.makeMove(0, 0, Player.O);
            board.makeMove(1, 1, Player.O);
            board.makeMove(2, 0, Player.X);
            board.makeMove(0, 2, Player.X);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(2, move[0]);
            assertEquals(2, move[1]);
        }
    }

    @Nested
    @DisplayName("Блокировка победы противника")
    class BlockingMoveTests {

        @Test
        @DisplayName("AI блокирует горизонтальную победу X")
        void aiBlocksHorizontalWin() {
            // X X _
            // O _ _
            // _ _ _
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.X);
            board.makeMove(0, 1, Player.O);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(2, move[0]);
            assertEquals(0, move[1]);
        }

        @Test
        @DisplayName("AI блокирует диагональную победу X")
        void aiBlocksDiagonalWin() {
            // X _ _
            // O X _
            // _ _ _
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.X);
            board.makeMove(0, 1, Player.O);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(2, move[0]);
            assertEquals(2, move[1]);
        }

        @Test
        @DisplayName("AI предпочитает победу блокировке")
        void aiPrefersWinOverBlock() {
            // O O _
            // X X _
            // _ _ _
            board.makeMove(0, 0, Player.O);
            board.makeMove(1, 0, Player.O);
            board.makeMove(0, 1, Player.X);
            board.makeMove(1, 1, Player.X);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(2, move[0]);
            assertEquals(0, move[1]);
        }
    }

    @Nested
    @DisplayName("Стратегические ходы")
    class StrategicMoveTests {

        @Test
        @DisplayName("AI занимает центр если свободен")
        void aiTakesCenterIfAvailable() {
            // X _ _
            // _ _ _
            // _ _ _
            board.makeMove(0, 0, Player.X);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertEquals(1, move[0]);
            assertEquals(1, move[1]);
        }

        @Test
        @DisplayName("AI делает случайный ход если нет угроз")
        void aiMakesRandomMoveWhenNoThreats() {
            // X _ _
            // _ O _
            // _ _ _
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 1, Player.O);

            int[] move = ai.calculateMove(board);
            
            assertNotNull(move);
            assertTrue(board.isEmpty(move[0], move[1]));
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("возвращает null на полностью заполненном поле")
        void returnsNullOnFullBoard() {
            board.makeMove(0, 0, Player.X);
            board.makeMove(1, 0, Player.O);
            board.makeMove(2, 0, Player.X);
            board.makeMove(0, 1, Player.O);
            board.makeMove(1, 1, Player.X);
            board.makeMove(2, 1, Player.O);
            board.makeMove(0, 2, Player.X);
            board.makeMove(1, 2, Player.O);
            board.makeMove(2, 2, Player.X);

            assertNull(ai.calculateMove(board));
        }

        @Test
        @DisplayName("работает на большом поле 5x5")
        void worksOnLargerBoard() {
            Board largeBoard = new Board(5, 5);
            largeBoard.makeMove(0, 0, Player.X);

            int[] move = ai.calculateMove(largeBoard);
            
            assertNotNull(move);
            assertTrue(largeBoard.isEmpty(move[0], move[1]));
        }
    }
}
