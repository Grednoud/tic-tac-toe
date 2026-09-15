package ru.mts.tictactoe.model;

/**
 * GameResult представляет результат завершённой игры.
 */
public enum GameResult {
    X_WINS("Победили крестики!"),
    O_WINS("Победили нолики!"),
    DRAW("Ничья!");

    private final String message;

    GameResult(String message) {
        this.message = message;
    }

    /**
     * Возвращает сообщение о результате игры.
     *
     * @return сообщение на русском
     */
    public String getMessage() {
        return message;
    }

    /**
     * Создаёт результат победы для указанного игрока.
     *
     * @param player победивший игрок
     * @return результат победы
     */
    public static GameResult winFor(Player player) {
        return player == Player.X ? X_WINS : O_WINS;
    }
}
