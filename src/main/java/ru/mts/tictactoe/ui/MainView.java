package ru.mts.tictactoe.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.mts.tictactoe.model.GameMode;
import ru.mts.tictactoe.model.GameState;

/**
 * MainView является главным контейнером UI приложения.
 * Содержит игровое поле, статус-бар и кнопки управления.
 */
public class MainView extends BorderPane {

    private final GameBoardView boardView;
    private final Label statusLabel;
    private final Label modeLabel;
    private final GameController controller;

    /**
     * Создаёт главное представление приложения.
     */
    public MainView() {
        getStyleClass().add("main-view");

        controller = new GameController();
        controller.setOnStateChanged(this::updateView);

        Label titleLabel = new Label("Крестики-нолики");
        titleLabel.getStyleClass().add("title-label");

        modeLabel = new Label();
        modeLabel.getStyleClass().add("mode-label");

        VBox header = new VBox(5, titleLabel, modeLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 20, 10, 20));

        boardView = new GameBoardView();
        boardView.setOnCellClick(controller::handleCellClick);

        statusLabel = new Label("Нажмите «Новая игра» для начала");
        statusLabel.getStyleClass().add("status-label");

        HBox statusBar = new HBox(statusLabel);
        statusBar.setAlignment(Pos.CENTER);
        statusBar.setPadding(new Insets(15));
        statusBar.getStyleClass().add("status-bar");

        Button newGameButton = new Button("Новая игра");
        newGameButton.getStyleClass().add("action-button");
        newGameButton.setOnAction(e -> showNewGameDialog());

        Button exitButton = new Button("Выход");
        exitButton.getStyleClass().addAll("action-button", "exit-button");
        exitButton.setOnAction(e -> System.exit(0));

        HBox buttonBar = new HBox(15, newGameButton, exitButton);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 20, 20, 20));

        VBox bottom = new VBox(statusBar, buttonBar);

        setTop(header);
        setCenter(boardView);
        setBottom(bottom);

        showNewGameDialog();
    }

    private void showNewGameDialog() {
        NewGameDialog dialog = new NewGameDialog();
        dialog.initOwner(getScene() != null ? getScene().getWindow() : null);
        
        dialog.showAndGetConfig().ifPresent(config -> {
            controller.startNewGame(config.mode(), config.boardSize(), config.winLength());
        });
    }

    private void updateView(GameState state) {
        boardView.initialize(state.getBoard().getWidth(), state.getBoard().getHeight());
        boardView.update(state.getBoard());

        statusLabel.setText(state.getStatusText());

        String modeText = state.getMode().getDisplayName();
        String boardInfo = String.format("Поле: %d×%d, для победы: %d в ряд",
            state.getBoard().getWidth(),
            state.getBoard().getHeight(),
            state.getBoard().getWinLength());
        modeLabel.setText(modeText + " | " + boardInfo);

        boolean inputLocked = state.isGameOver() || state.isAiTurn();
        boardView.setLocked(inputLocked);

        statusLabel.getStyleClass().removeAll("status-win", "status-draw");
        if (state.isGameOver()) {
            state.getResult().ifPresent(result -> {
                switch (result) {
                    case DRAW -> statusLabel.getStyleClass().add("status-draw");
                    default -> statusLabel.getStyleClass().add("status-win");
                }
            });
        }
    }
}
