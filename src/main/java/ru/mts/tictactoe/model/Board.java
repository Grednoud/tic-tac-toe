package ru.mts.tictactoe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Board представляет игровое поле для крестиков-ноликов.
 * Поддерживает произвольный размер NxM и настраиваемую длину выигрышной линии.
 */
public class Board {
    
    private final int width;
    private final int height;
    private final int winLength;
    private final Player[][] cells;

    private static final int[][] DIRECTIONS = {
        {1, 0},   // горизонталь →
        {0, 1},   // вертикаль ↓
        {1, 1},   // диагональ ↘
        {1, -1}   // диагональ ↗
    };

    /**
     * Создаёт квадратное игровое поле.
     *
     * @param size размер поля (ширина и высота)
     * @param winLength длина выигрышной линии
     * @throws IllegalArgumentException если размер меньше 3 или winLength больше размера
     */
    public Board(int size, int winLength) {
        this(size, size, winLength);
    }

    /**
     * Создаёт игровое поле заданного размера.
     *
     * @param width ширина поля
     * @param height высота поля
     * @param winLength длина выигрышной линии
     * @throws IllegalArgumentException если размеры некорректны
     */
    public Board(int width, int height, int winLength) {
        if (width < 3 || height < 3) {
            throw new IllegalArgumentException("Минимальный размер поля: 3x3");
        }
        if (winLength < 3) {
            throw new IllegalArgumentException("Минимальная длина выигрышной линии: 3");
        }
        if (winLength > Math.min(width, height)) {
            throw new IllegalArgumentException(
                "Длина выигрышной линии не может превышать размер поля");
        }

        this.width = width;
        this.height = height;
        this.winLength = winLength;
        this.cells = new Player[height][width];
    }

    /**
     * Возвращает ширину поля.
     *
     * @return ширина поля
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает высоту поля.
     *
     * @return высота поля
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает длину выигрышной линии.
     *
     * @return длина выигрышной линии
     */
    public int getWinLength() {
        return winLength;
    }

    /**
     * Возвращает содержимое клетки.
     *
     * @param x координата по горизонтали (0-based)
     * @param y координата по вертикали (0-based)
     * @return игрок, занявший клетку, или null если клетка пуста
     * @throws IndexOutOfBoundsException если координаты вне поля
     */
    public Player getCell(int x, int y) {
        validateCoordinates(x, y);
        return cells[y][x];
    }

    /**
     * Проверяет, пуста ли клетка.
     *
     * @param x координата по горизонтали
     * @param y координата по вертикали
     * @return true если клетка пуста
     */
    public boolean isEmpty(int x, int y) {
        return getCell(x, y) == null;
    }

    /**
     * Делает ход в указанную клетку.
     *
     * @param x координата по горизонтали
     * @param y координата по вертикали
     * @param player игрок, делающий ход
     * @return true если ход успешен, false если клетка занята
     * @throws IndexOutOfBoundsException если координаты вне поля
     * @throws IllegalArgumentException если player == null
     */
    public boolean makeMove(int x, int y, Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Игрок не может быть null");
        }
        validateCoordinates(x, y);
        
        if (cells[y][x] != null) {
            return false;
        }
        
        cells[y][x] = player;
        return true;
    }

    /**
     * Отменяет ход (очищает клетку). Используется для AI-расчётов.
     *
     * @param x координата по горизонтали
     * @param y координата по вертикали
     */
    public void undoMove(int x, int y) {
        validateCoordinates(x, y);
        cells[y][x] = null;
    }

    /**
     * Проверяет, выиграл ли указанный игрок.
     *
     * @param player проверяемый игрок
     * @return true если игрок выиграл
     */
    public boolean checkWin(Player player) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] == player && checkWinFromCell(x, y, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Возвращает победившего игрока, если есть.
     *
     * @return Optional с победителем или empty если победителя нет
     */
    public Optional<Player> getWinner() {
        if (checkWin(Player.X)) {
            return Optional.of(Player.X);
        }
        if (checkWin(Player.O)) {
            return Optional.of(Player.O);
        }
        return Optional.empty();
    }

    /**
     * Проверяет, заполнено ли поле полностью.
     *
     * @return true если все клетки заняты
     */
    public boolean isFull() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Возвращает список всех пустых клеток.
     *
     * @return список координат пустых клеток [x, y]
     */
    public List<int[]> getEmptyCells() {
        List<int[]> empty = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (cells[y][x] == null) {
                    empty.add(new int[]{x, y});
                }
            }
        }
        return empty;
    }

    /**
     * Очищает поле для новой игры.
     */
    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = null;
            }
        }
    }

    private boolean checkWinFromCell(int startX, int startY, Player player) {
        for (int[] direction : DIRECTIONS) {
            if (checkLine(startX, startY, direction[0], direction[1], player)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLine(int startX, int startY, int dx, int dy, Player player) {
        int endX = startX + dx * (winLength - 1);
        int endY = startY + dy * (winLength - 1);

        if (endX < 0 || endX >= width || endY < 0 || endY >= height) {
            return false;
        }

        for (int i = 0; i < winLength; i++) {
            int x = startX + dx * i;
            int y = startY + dy * i;
            if (cells[y][x] != player) {
                return false;
            }
        }
        return true;
    }

    private void validateCoordinates(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException(
                String.format("Координаты (%d, %d) вне поля %dx%d", x, y, width, height));
        }
    }
}
