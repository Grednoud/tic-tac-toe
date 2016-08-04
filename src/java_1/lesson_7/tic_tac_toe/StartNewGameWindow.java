package java_1.lesson_7.tic_tac_toe;

import java_1.lesson_7.tic_tac_toe.GameWindow;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StartNewGameWindow extends JFrame{
	
	private final int WINDOW_WIDTH = 300;
	private final int WINDOW_HEIGHT = 250;
	private final int MIN_WIN_LEN = 2;
	private final int MIN_FIELD_SIZE = 3;
	private final int MAX_FIELD_SIZE = 10;
	
	private final GameWindow game_window;
	private JRadioButton rb_human_vs_ai;
	private JRadioButton rb_human_vs_human;
	private JSlider slider_field_size;
	private JSlider slider_win_len;
	
	StartNewGameWindow(GameWindow game_window) {
		this.game_window = game_window;
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		Rectangle game_window_bounds = game_window.getBounds();
		int pos_x = (int)game_window_bounds.getCenterX() - WINDOW_WIDTH/2;
		int pos_y = (int)game_window_bounds.getCenterY() - WINDOW_HEIGHT/2;
		setLocation(pos_x, pos_y);
		setResizable(false);
		setTitle("Новая игра");
		setLayout(new GridLayout(10, 1));
		addGameModControls();
		addFieldAndWinLenControls();
	}

	private void addFieldAndWinLenControls() {
		final String WIN_LEN_PREFIX = "Win len: ";
		final JLabel label_win_len = new JLabel(WIN_LEN_PREFIX + MIN_WIN_LEN);
		slider_win_len = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
		slider_win_len.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				label_win_len.setText(WIN_LEN_PREFIX + slider_win_len.getValue());
			}
		});
		
		final String FIELD_SIZE_PREFIX = "Field size: ";
		
	}

	private void addGameModControls() {
		add(new JLabel("Выберите режим игры:"));
		rb_human_vs_ai = new JRadioButton("игрок против компьютера", true);
		rb_human_vs_human = new JRadioButton("игрок против игрока");
		ButtonGroup bg_game_mode = new ButtonGroup();
		bg_game_mode.add(rb_human_vs_ai);
		bg_game_mode.add(rb_human_vs_human);
		add(rb_human_vs_ai);
		add(rb_human_vs_human);
	}

}
