package ru.mts.tictactoe.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.mts.tictactoe.model.GameMode;

import java.util.Optional;

/**
 * NewGameDialog предоставляет диалог настройки новой игры.
 * Позволяет выбрать режим, размер поля и длину выигрышной линии.
 */
public class NewGameDialog extends Dialog<NewGameDialog.GameConfig> {

    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 10;
    private static final int DEFAULT_SIZE = 3;
    private static final int DEFAULT_WIN_LENGTH = 3;

    private final ToggleGroup modeGroup;
    private final Slider sizeSlider;
    private final Slider winLengthSlider;
    private final Label sizeValueLabel;
    private final Label winLengthValueLabel;

    /**
     * Создаёт диалог настройки новой игры.
     */
    public NewGameDialog() {
        setTitle("Новая игра");
        setHeaderText("Настройки игры");

        DialogPane dialogPane = getDialogPane();
        dialogPane.getStyleClass().add("new-game-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        Label modeLabel = new Label("Режим игры:");
        modeLabel.getStyleClass().add("dialog-section-label");

        modeGroup = new ToggleGroup();
        
        RadioButton pvAiRadio = new RadioButton(GameMode.PLAYER_VS_AI.getDisplayName());
        pvAiRadio.setToggleGroup(modeGroup);
        pvAiRadio.setUserData(GameMode.PLAYER_VS_AI);
        pvAiRadio.setSelected(true);

        RadioButton pvpRadio = new RadioButton(GameMode.PLAYER_VS_PLAYER.getDisplayName());
        pvpRadio.setToggleGroup(modeGroup);
        pvpRadio.setUserData(GameMode.PLAYER_VS_PLAYER);

        VBox modeBox = new VBox(8, modeLabel, pvAiRadio, pvpRadio);

        Label sizeLabel = new Label("Размер поля:");
        sizeLabel.getStyleClass().add("dialog-section-label");
        
        sizeSlider = new Slider(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE);
        sizeSlider.setMajorTickUnit(1);
        sizeSlider.setMinorTickCount(0);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);
        
        sizeValueLabel = new Label(DEFAULT_SIZE + " × " + DEFAULT_SIZE);
        sizeValueLabel.getStyleClass().add("slider-value-label");
        
        HBox sizeValueBox = new HBox(10, sizeSlider, sizeValueLabel);
        sizeValueBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(sizeSlider, javafx.scene.layout.Priority.ALWAYS);
        
        VBox sizeBox = new VBox(8, sizeLabel, sizeValueBox);

        Label winLengthLabel = new Label("Для победы в ряд:");
        winLengthLabel.getStyleClass().add("dialog-section-label");
        
        winLengthSlider = new Slider(MIN_SIZE, DEFAULT_SIZE, DEFAULT_WIN_LENGTH);
        winLengthSlider.setMajorTickUnit(1);
        winLengthSlider.setMinorTickCount(0);
        winLengthSlider.setSnapToTicks(true);
        winLengthSlider.setShowTickLabels(true);
        winLengthSlider.setShowTickMarks(true);
        
        winLengthValueLabel = new Label(String.valueOf(DEFAULT_WIN_LENGTH));
        winLengthValueLabel.getStyleClass().add("slider-value-label");
        
        HBox winLengthValueBox = new HBox(10, winLengthSlider, winLengthValueLabel);
        winLengthValueBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(winLengthSlider, javafx.scene.layout.Priority.ALWAYS);
        
        VBox winLengthBox = new VBox(8, winLengthLabel, winLengthValueBox);

        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int size = newVal.intValue();
            sizeValueLabel.setText(size + " × " + size);
            
            winLengthSlider.setMax(size);
            if (winLengthSlider.getValue() > size) {
                winLengthSlider.setValue(size);
            }
        });

        winLengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            winLengthValueLabel.setText(String.valueOf(newVal.intValue()));
        });

        content.getChildren().addAll(modeBox, sizeBox, winLengthBox);
        dialogPane.setContent(content);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                GameMode mode = (GameMode) modeGroup.getSelectedToggle().getUserData();
                int size = (int) sizeSlider.getValue();
                int winLength = (int) winLengthSlider.getValue();
                return new GameConfig(mode, size, winLength);
            }
            return null;
        });
    }

    /**
     * Показывает диалог и возвращает конфигурацию игры.
     *
     * @return конфигурация или empty если отменено
     */
    public Optional<GameConfig> showAndGetConfig() {
        return showAndWait();
    }

    /**
     * GameConfig хранит настройки для новой игры.
     *
     * @param mode режим игры
     * @param boardSize размер поля
     * @param winLength длина выигрышной линии
     */
    public record GameConfig(GameMode mode, int boardSize, int winLength) {}
}
