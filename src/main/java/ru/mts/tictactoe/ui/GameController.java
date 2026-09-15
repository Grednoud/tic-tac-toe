package ru.mts.tictactoe.ui;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import ru.mts.tictactoe.ai.AiPlayer;
import ru.mts.tictactoe.model.GameMode;
import ru.mts.tictactoe.model.GameState;

import java.util.function.Consumer;

/**
 * GameController связывает модель игры с UI.
 * Управляет ходами, AI и обновлением состояния.
 */
public class GameController {

    private static final int AI_DELAY_MS = 400;

    private final AiPlayer aiPlayer;
    private GameState gameState;
    private Consumer<GameState> onStateChanged;

    /**
     * Создаёт контроллер игры.
     */
    public GameController() {
        this.aiPlayer = new AiPlayer();
    }

    /**
     * Начинает новую игру с заданными настройками.
     *
     * @param mode режим игры
     * @param boardSize размер поля
     * @param winLength длина выигрышной линии
     */
    public void startNewGame(GameMode mode, int boardSize, int winLength) {
        this.gameState = new GameState(mode, boardSize, winLength);
        notifyStateChanged();
    }

    /**
     * Обрабатывает клик по клетке.
     *
     * @param x координата по горизонтали
     * @param y координата по вертикали
     */
    public void handleCellClick(int x, int y) {
        if (gameState == null || gameState.isGameOver()) {
            return;
        }

        if (gameState.isAiTurn()) {
            return;
        }

        if (!gameState.makeMove(x, y)) {
            return;
        }

        notifyStateChanged();

        if (gameState.isAiTurn()) {
            scheduleAiMove();
        }
    }

    /**
     * Возвращает текущее состояние игры.
     *
     * @return состояние игры или null если игра не начата
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Устанавливает слушатель изменения состояния.
     *
     * @param listener слушатель
     */
    public void setOnStateChanged(Consumer<GameState> listener) {
        this.onStateChanged = listener;
    }

    private void scheduleAiMove() {
        PauseTransition pause = new PauseTransition(Duration.millis(AI_DELAY_MS));
        pause.setOnFinished(e -> executeAiMove());
        pause.play();
    }

    private void executeAiMove() {
        if (gameState == null || gameState.isGameOver() || !gameState.isAiTurn()) {
            return;
        }

        int[] move = aiPlayer.calculateMove(gameState.getBoard());
        if (move != null) {
            gameState.makeMove(move[0], move[1]);
            notifyStateChanged();
        }
    }

    private void notifyStateChanged() {
        if (onStateChanged != null && gameState != null) {
            onStateChanged.accept(gameState);
        }
    }
}
