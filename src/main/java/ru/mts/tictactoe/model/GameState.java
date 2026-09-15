package ru.mts.tictactoe.model;

import java.util.Optional;

/**
 * GameState управляет состоянием игры: режим, текущий игрок, результат.
 * Центральный класс бизнес-логики игры.
 */
public class GameState {

    private final Board board;
    private final GameMode mode;
    private Player currentPlayer;
    private GameResult result;
    private boolean gameOver;

    /**
     * Создаёт новое состояние игры.
     *
     * @param mode режим игры (PvP или PvAI)
     * @param boardSize размер поля
     * @param winLength длина выигрышной линии
     */
    public GameState(GameMode mode, int boardSize, int winLength) {
        this.board = new Board(boardSize, winLength);
        this.mode = mode;
        this.currentPlayer = Player.X;
        this.gameOver = false;
        this.result = null;
    }

    /**
     * Возвращает игровое поле.
     *
     * @return доска с текущим состоянием
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Возвращает режим игры.
     *
     * @return режим игры
     */
    public GameMode getMode() {
        return mode;
    }

    /**
     * Возвращает текущего игрока (чей ход).
     *
     * @return текущий игрок
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Проверяет, закончена ли игра.
     *
     * @return true если игра завершена
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Возвращает результат игры.
     *
     * @return результат или Optional.empty() если игра не завершена
     */
    public Optional<GameResult> getResult() {
        return Optional.ofNullable(result);
    }

    /**
     * Выполняет ход в указанную клетку.
     * Автоматически проверяет победу/ничью и переключает игрока.
     *
     * @param x координата по горизонтали
     * @param y координата по вертикали
     * @return true если ход выполнен успешно
     */
    public boolean makeMove(int x, int y) {
        if (gameOver) {
            return false;
        }

        if (!board.makeMove(x, y, currentPlayer)) {
            return false;
        }

        if (board.checkWin(currentPlayer)) {
            gameOver = true;
            result = GameResult.winFor(currentPlayer);
            return true;
        }

        if (board.isFull()) {
            gameOver = true;
            result = GameResult.DRAW;
            return true;
        }

        currentPlayer = currentPlayer.opponent();
        return true;
    }

    /**
     * Проверяет, ход ли сейчас AI (режим PvAI и ход ноликов).
     *
     * @return true если сейчас должен ходить AI
     */
    public boolean isAiTurn() {
        return mode == GameMode.PLAYER_VS_AI && currentPlayer == Player.O && !gameOver;
    }

    /**
     * Возвращает текстовое описание текущего состояния для статус-бара.
     *
     * @return описание состояния
     */
    public String getStatusText() {
        if (gameOver && result != null) {
            return result.getMessage();
        }
        
        if (mode == GameMode.PLAYER_VS_AI) {
            if (currentPlayer == Player.X) {
                return "Ваш ход (X)";
            } else {
                return "Ход компьютера (O)...";
            }
        } else {
            return String.format("Ход: %s (%s)", 
                currentPlayer.getDisplayName(), 
                currentPlayer.getSymbol());
        }
    }
}
