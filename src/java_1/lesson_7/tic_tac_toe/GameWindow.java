package java_1.lesson_7.tic_tac_toe;

import java_1.lesson_7.tic_tac_toe.StartNewGameWindow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GameWindow extends JFrame{
	
	private final int WINDOW_WIDTH = 400;
	private final int WINDOW_HEIGHT = 500;
	private final int POS_X = 800;
	private final int POS_Y = 300;
	
	private final StartNewGameWindow start_new_game_window;
	private final Map map;
	
	GameWindow() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Крестики нолики");
		setResizable(false);
		start_new_game_window = new StartNewGameWindow(this); 
		JButton btn_new_game = new JButton("Новая игра");
		btn_new_game.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start_new_game_window.setVisible(true);
			}
		});
		
		JButton btn_exit = new JButton("Выход");
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		map = new Map();
		JPanel panel_bottom = new JPanel();
		panel_bottom.setLayout(new GridLayout(2, 1));
		panel_bottom.add(btn_new_game);
		panel_bottom.add(btn_exit);
		add(map, BorderLayout.CENTER);
		add(panel_bottom, BorderLayout.SOUTH);
		setVisible(true);
		start_new_game_window.setVisible(true);
	}
	void startNewGameWindow (int mode, int fild_size_x, int fild_size_y, int win_len) {
		map.stertNewGame(mode, fild_size_x, fild_size_y, win_len);
	}
 }
