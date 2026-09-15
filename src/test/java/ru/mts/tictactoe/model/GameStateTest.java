package ru.mts.tictactoe.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameState — состояние игры")
class GameStateTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState(GameMode.PLAYER_VS_PLAYER, 3, 3);
    }

    @Nested
    @DisplayName("Инициализация")
    class InitTests {

        @Test
        @DisplayName("X ходит первым")
        void xMovesFirst() {
            assertEquals(Player.X, gameState.getCurrentPlayer());
        }

        @Test
        @DisplayName("игра не завершена при старте")
        void gameNotOverAtStart() {
            assertFalse(gameState.isGameOver());
            assertTrue(gameState.getResult().isEmpty());
        }

        @Test
        @DisplayName("режим сохраняется")
        void modeIsPreserved() {
            assertEquals(GameMode.PLAYER_VS_PLAYER, gameState.getMode());
            
            GameState aiGame = new GameState(GameMode.PLAYER_VS_AI, 3, 3);
            assertEquals(GameMode.PLAYER_VS_AI, aiGame.getMode());
        }
    }

    @Nested
    @DisplayName("Ходы")
    class MoveTests {

        @Test
        @DisplayName("успешный ход переключает игрока")
        void successfulMoveSwitchesPlayer() {
            assertTrue(gameState.makeMove(0, 0));
            assertEquals(Player.O, gameState.getCurrentPlayer());
        }

        @Test
        @DisplayName("ход в занятую клетку не переключает игрока")
        void moveToOccupiedCellDoesNotSwitchPlayer() {
            gameState.makeMove(0, 0);
            assertFalse(gameState.makeMove(0, 0));
            assertEquals(Player.O, gameState.getCurrentPlayer());
        }

        @Test
        @DisplayName("ход после окончания игры невозможен")
        void moveAfterGameOverImpossible() {
            // X wins: 0,0 -> 0,1 -> 1,0 -> 1,1 -> 2,0
            gameState.makeMove(0, 0); // X
            gameState.makeMove(0, 1); // O
            gameState.makeMove(1, 0); // X
            gameState.makeMove(1, 1); // O
            gameState.makeMove(2, 0); // X wins

            assertTrue(gameState.isGameOver());
            assertFalse(gameState.makeMove(2, 2));
        }
    }

    @Nested
    @DisplayName("Победа")
    class WinTests {

        @Test
        @DisplayName("X побеждает горизонтально")
        void xWinsHorizontally() {
            gameState.makeMove(0, 0); // X
            gameState.makeMove(0, 1); // O
            gameState.makeMove(1, 0); // X
            gameState.makeMove(1, 1); // O
            gameState.makeMove(2, 0); // X wins

            assertTrue(gameState.isGameOver());
            assertTrue(gameState.getResult().isPresent());
            assertEquals(GameResult.X_WINS, gameState.getResult().get());
        }

        @Test
        @DisplayName("O побеждает вертикально")
        void oWinsVertically() {
            gameState.makeMove(0, 0); // X
            gameState.makeMove(1, 0); // O
            gameState.makeMove(0, 1); // X
            gameState.makeMove(1, 1); // O
            gameState.makeMove(2, 2); // X
            gameState.makeMove(1, 2); // O wins

            assertTrue(gameState.isGameOver());
            assertEquals(GameResult.O_WINS, gameState.getResult().get());
        }

        @Test
        @DisplayName("X побеждает диагонально")
        void xWinsDiagonally() {
            gameState.makeMove(0, 0); // X
            gameState.makeMove(0, 1); // O
            gameState.makeMove(1, 1); // X
            gameState.makeMove(0, 2); // O
            gameState.makeMove(2, 2); // X wins

            assertTrue(gameState.isGameOver());
            assertEquals(GameResult.X_WINS, gameState.getResult().get());
        }
    }

    @Nested
    @DisplayName("Ничья")
    class DrawTests {

        @Test
        @DisplayName("ничья при заполненном поле без победителя")
        void drawWhenBoardFullNoWinner() {
            // X O X
            // X O O
            // O X X
            gameState.makeMove(0, 0); // X
            gameState.makeMove(1, 0); // O
            gameState.makeMove(2, 0); // X
            gameState.makeMove(1, 1); // O
            gameState.makeMove(0, 1); // X
            gameState.makeMove(2, 1); // O
            gameState.makeMove(1, 2); // X
            gameState.makeMove(0, 2); // O
            gameState.makeMove(2, 2); // X

            assertTrue(gameState.isGameOver());
            assertEquals(GameResult.DRAW, gameState.getResult().get());
        }
    }

    @Nested
    @DisplayName("Режим PvAI")
    class AiModeTests {

        @Test
        @DisplayName("isAiTurn возвращает true когда ход AI")
        void isAiTurnReturnsTrueForAi() {
            GameState aiGame = new GameState(GameMode.PLAYER_VS_AI, 3, 3);
            
            assertFalse(aiGame.isAiTurn()); // X (human) moves first
            aiGame.makeMove(0, 0);
            assertTrue(aiGame.isAiTurn()); // O (AI) should move
        }

        @Test
        @DisplayName("isAiTurn всегда false в режиме PvP")
        void isAiTurnAlwaysFalseInPvP() {
            assertFalse(gameState.isAiTurn());
            gameState.makeMove(0, 0);
            assertFalse(gameState.isAiTurn());
        }

        @Test
        @DisplayName("isAiTurn false после окончания игры")
        void isAiTurnFalseAfterGameOver() {
            GameState aiGame = new GameState(GameMode.PLAYER_VS_AI, 3, 3);
            
            // X wins quickly
            aiGame.makeMove(0, 0); // X
            aiGame.makeMove(0, 1); // simulate O
            aiGame.makeMove(1, 0); // X
            aiGame.makeMove(1, 1); // simulate O
            aiGame.makeMove(2, 0); // X wins

            assertFalse(aiGame.isAiTurn());
        }
    }

    @Nested
    @DisplayName("Статус-бар")
    class StatusTextTests {

        @Test
        @DisplayName("статус при старте PvP")
        void statusAtStartPvP() {
            String status = gameState.getStatusText();
            assertTrue(status.contains("Крестики") || status.contains("X"));
        }

        @Test
        @DisplayName("статус при победе")
        void statusOnWin() {
            gameState.makeMove(0, 0);
            gameState.makeMove(0, 1);
            gameState.makeMove(1, 0);
            gameState.makeMove(1, 1);
            gameState.makeMove(2, 0);

            assertEquals("Победили крестики!", gameState.getStatusText());
        }

        @Test
        @DisplayName("статус при ничьей")
        void statusOnDraw() {
            // X O X
            // X O O
            // O X X
            gameState.makeMove(0, 0); // X
            gameState.makeMove(1, 0); // O
            gameState.makeMove(2, 0); // X
            gameState.makeMove(1, 1); // O
            gameState.makeMove(0, 1); // X
            gameState.makeMove(2, 1); // O
            gameState.makeMove(1, 2); // X
            gameState.makeMove(0, 2); // O
            gameState.makeMove(2, 2); // X

            assertEquals("Ничья!", gameState.getStatusText());
        }
    }
}
