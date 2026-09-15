package ru.mts.tictactoe.model;

/**
 * Player представляет игрока в крестики-нолики.
 * Каждый игрок имеет символ (X или O) и отображаемое имя.
 */
public enum Player {
    X("X", "Крестики"),
    O("O", "Нолики");

    private final String symbol;
    private final String displayName;

    Player(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }

    /**
     * Возвращает символ игрока.
     *
     * @return символ игрока (X или O)
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Возвращает отображаемое имя игрока на русском.
     *
     * @return отображаемое имя
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Возвращает противоположного игрока.
     *
     * @return противник текущего игрока
     */
    public Player opponent() {
        return this == X ? O : X;
    }
}
