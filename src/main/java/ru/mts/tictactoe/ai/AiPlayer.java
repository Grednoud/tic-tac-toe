package ru.mts.tictactoe.ai;

import ru.mts.tictactoe.model.Board;
import ru.mts.tictactoe.model.Player;

import java.util.List;
import java.util.Random;

/**
 * AiPlayer реализует логику искусственного интеллекта для игры в крестики-нолики.
 * Стратегия: 1) Выиграть если можно 2) Заблокировать победу противника 3) Случайный ход
 */
public class AiPlayer {

    private static final Player AI_PLAYER = Player.O;
    private static final Player HUMAN_PLAYER = Player.X;

    private final Random random;

    /**
     * Создаёт AI-игрока с генератором случайных чисел по умолчанию.
     */
    public AiPlayer() {
        this.random = new Random();
    }

    /**
     * Создаёт AI-игрока с заданным генератором (для тестирования).
     *
     * @param random генератор случайных чисел
     */
    public AiPlayer(Random random) {
        this.random = random;
    }

    /**
     * Вычисляет лучший ход для AI.
     *
     * @param board текущее состояние доски
     * @return координаты хода [x, y] или null если ходов нет
     */
    public int[] calculateMove(Board board) {
        List<int[]> emptyCells = board.getEmptyCells();
        
        if (emptyCells.isEmpty()) {
            return null;
        }

        int[] winningMove = findWinningMove(board, AI_PLAYER);
        if (winningMove != null) {
            return winningMove;
        }

        int[] blockingMove = findWinningMove(board, HUMAN_PLAYER);
        if (blockingMove != null) {
            return blockingMove;
        }

        int[] centerMove = tryCenterMove(board);
        if (centerMove != null) {
            return centerMove;
        }

        return emptyCells.get(random.nextInt(emptyCells.size()));
    }

    /**
     * Ищет выигрышный ход для указанного игрока.
     * Если найден — это либо победа для AI, либо угроза от человека (блокировка).
     *
     * @param board доска
     * @param player игрок, для которого ищем выигрыш
     * @return координаты выигрышного хода или null
     */
    private int[] findWinningMove(Board board, Player player) {
        List<int[]> emptyCells = board.getEmptyCells();
        
        for (int[] cell : emptyCells) {
            int x = cell[0];
            int y = cell[1];
            
            board.makeMove(x, y, player);
            boolean wins = board.checkWin(player);
            board.undoMove(x, y);
            
            if (wins) {
                return cell;
            }
        }
        
        return null;
    }

    /**
     * Пытается занять центр поля (хорошая стратегическая позиция).
     *
     * @param board доска
     * @return координаты центра если он свободен, иначе null
     */
    private int[] tryCenterMove(Board board) {
        int centerX = board.getWidth() / 2;
        int centerY = board.getHeight() / 2;
        
        if (board.isEmpty(centerX, centerY)) {
            return new int[]{centerX, centerY};
        }
        
        return null;
    }
}
