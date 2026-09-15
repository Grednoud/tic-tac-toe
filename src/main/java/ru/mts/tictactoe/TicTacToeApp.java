package ru.mts.tictactoe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mts.tictactoe.ui.MainView;

/**
 * TicTacToeApp является точкой входа для JavaFX приложения.
 */
public class TicTacToeApp extends Application {

    private static final double MIN_WIDTH = 450;
    private static final double MIN_HEIGHT = 550;

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        
        Scene scene = new Scene(mainView, MIN_WIDTH, MIN_HEIGHT);
        scene.getStylesheets().add(
            getClass().getResource("/ru/mts/tictactoe/css/styles.css").toExternalForm()
        );

        primaryStage.setTitle("Крестики-нолики");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }
}
