package java_1.lesson_7.tic_tac_toe;

import java_1.lesson_7.tic_tac_toe.GameWindow;
import java_1.lesson_7.tic_tac_toe.Map;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StartNewGameWindow extends JDialog{
	
	private final int WINDOW_WIDTH = 300;
	private final int WINDOW_HEIGHT = 250;
	private final int MIN_WIN_LEN = 3;
	private final int MIN_FIELD_SIZE = 3;
	private final int MAX_FIELD_SIZE = 20;
	
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
		setTitle("����� ����");
		setLayout(new GridLayout(10, 1));
		addGameModControls();
		addFieldAndWinLenControls();
		JButton btn_start_game = new JButton("������");
		btn_start_game.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onPressButtonStart();
			}
		});
		add(btn_start_game);
	}
	
	private void onPressButtonStart() {
		int game_mode;
		if (rb_human_vs_ai.isSelected()) game_mode = Map.GAME_MODE_HUMAN_VS_AI;
		else if (rb_human_vs_human.isSelected()) game_mode = Map.GAME_MODE_HUMAN_VS_HUMAN;
		else throw new RuntimeException("�������������� ����� ����");
		int field_size = slider_field_size.getValue();
		setVisible(false);
		game_window.startNewGameWindow(game_mode, field_size, field_size, slider_win_len.getValue());
	}

	private void addFieldAndWinLenControls() {
		final String WIN_LEN_PREFIX = "Win len: ";
		final JLabel label_win_len = new JLabel(WIN_LEN_PREFIX + MIN_WIN_LEN);
		slider_win_len = new JSlider(MIN_WIN_LEN, MIN_FIELD_SIZE, MIN_WIN_LEN);
		slider_win_len.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				label_win_len.setText(WIN_LEN_PREFIX + slider_win_len.getValue());
			}
		});
		
		final String FIELD_SIZE_PREFIX = "Field size: ";
		final JLabel label_field_size = new JLabel(FIELD_SIZE_PREFIX + MIN_FIELD_SIZE);
		slider_field_size = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
		slider_field_size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int i = slider_field_size.getValue();
				label_field_size.setText(FIELD_SIZE_PREFIX + i);
				slider_win_len.setMaximum(i);
			}
		});
		add(new JLabel("�������� ������ ����:"));
		add(label_field_size);
		add(slider_field_size);
		add(new JLabel("���������� ������ �������� ������������������: "));
		add(label_win_len);
		add(slider_win_len);		
	}

	private void addGameModControls() {
		add(new JLabel("�������� ����� ����:"));
		rb_human_vs_ai = new JRadioButton("����� ������ ����������", true);
		rb_human_vs_human = new JRadioButton("����� ������ ������");
		ButtonGroup bg_game_mode = new ButtonGroup();
		bg_game_mode.add(rb_human_vs_ai);
		bg_game_mode.add(rb_human_vs_human);
		add(rb_human_vs_ai);
		add(rb_human_vs_human);
	}

}
