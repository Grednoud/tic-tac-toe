package ru.mts.tictactoe.model;

/**
 * GameMode представляет режим игры.
 */
public enum GameMode {
    PLAYER_VS_AI("Игрок против компьютера"),
    PLAYER_VS_PLAYER("Игрок против игрока");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает отображаемое название режима.
     *
     * @return название режима на русском
     */
    public String getDisplayName() {
        return displayName;
    }
}
