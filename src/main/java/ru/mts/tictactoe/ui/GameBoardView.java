package ru.mts.tictactoe.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ru.mts.tictactoe.model.Board;
import ru.mts.tictactoe.model.Player;

import java.util.function.BiConsumer;

/**
 * GameBoardView отвечает за отображение игрового поля.
 * Создаёт сетку кнопок-клеток и обрабатывает клики.
 */
public class GameBoardView extends GridPane {

    private static final double CELL_SIZE = 80;
    private static final double GAP = 4;

    private StackPane[][] cellPanes;
    private BiConsumer<Integer, Integer> onCellClick;

    /**
     * Создаёт пустое представление поля.
     */
    public GameBoardView() {
        setAlignment(Pos.CENTER);
        setHgap(GAP);
        setVgap(GAP);
        setPadding(new Insets(20));
        getStyleClass().add("game-board");
    }

    /**
     * Инициализирует поле заданного размера.
     *
     * @param width ширина поля
     * @param height высота поля
     */
    public void initialize(int width, int height) {
        getChildren().clear();
        cellPanes = new StackPane[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                StackPane cell = createCell(x, y);
                cellPanes[y][x] = cell;
                add(cell, x, y);
            }
        }
    }

    /**
     * Обновляет отображение по состоянию доски.
     *
     * @param board текущее состояние доски
     */
    public void update(Board board) {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                updateCell(x, y, board.getCell(x, y));
            }
        }
    }

    /**
     * Устанавливает обработчик клика по клетке.
     *
     * @param handler обработчик (x, y)
     */
    public void setOnCellClick(BiConsumer<Integer, Integer> handler) {
        this.onCellClick = handler;
    }

    /**
     * Блокирует/разблокирует взаимодействие с полем.
     *
     * @param locked true для блокировки
     */
    public void setLocked(boolean locked) {
        setMouseTransparent(locked);
        if (locked) {
            setOpacity(0.8);
        } else {
            setOpacity(1.0);
        }
    }

    private StackPane createCell(int x, int y) {
        StackPane cell = new StackPane();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setMinSize(CELL_SIZE, CELL_SIZE);
        cell.getStyleClass().add("cell");

        Label label = new Label();
        label.getStyleClass().add("cell-label");
        cell.getChildren().add(label);

        cell.setOnMouseClicked(e -> {
            if (onCellClick != null) {
                onCellClick.accept(x, y);
            }
        });

        cell.setOnMouseEntered(e -> {
            if (!cell.getStyleClass().contains("cell-filled")) {
                cell.getStyleClass().add("cell-hover");
            }
        });

        cell.setOnMouseExited(e -> {
            cell.getStyleClass().remove("cell-hover");
        });

        return cell;
    }

    private void updateCell(int x, int y, Player player) {
        StackPane cell = cellPanes[y][x];
        Label label = (Label) cell.getChildren().get(0);

        cell.getStyleClass().removeAll("cell-x", "cell-o", "cell-filled");

        if (player == null) {
            label.setText("");
        } else {
            label.setText(player.getSymbol());
            cell.getStyleClass().add("cell-filled");
            if (player == Player.X) {
                cell.getStyleClass().add("cell-x");
            } else {
                cell.getStyleClass().add("cell-o");
            }
        }
    }
}
