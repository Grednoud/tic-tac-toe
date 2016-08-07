package java_1.lesson_7.tic_tac_toe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {
	
	static final int GAME_MODE_HUMAN_VS_AI = 0;
	static final int GAME_MODE_HUMAN_VS_HUMAN = 1;
	
	private static final int EMPTY_DOT = 0;
	private static final int HUMAN_DOT = 1;
	private static final int PLAYER_2_DOT = 2;
	
	private static final int DRAW = 0;
	private static final int AI_WIN = 1;
	private static final int HUMAN_WIN = 2;
	private static final int HUMAN_2_WIN = 3;
	
	private static final String DRAW_MSG = "Ничья!";
	private static final String HUMAN_WIN_MSG = "Победил крестик!";
	private static final String HUMAN_2_WIN_MSG = "Победил нолик!";
	private static final String AI_WIN_MSG = "Победил компьютер!";
	
	private static final Random rnd = new Random();
	private static final Font font = new Font("Times new roman", Font.BOLD, 32);
	
	private BufferedImage img_human_dot;
	private BufferedImage img_ai_dot;
	
	private boolean initialized;
	private boolean game_over;
	private boolean player_1_turn;
	private int game_over_state;
	private int game_mode;
	private int field_size_x;
	private int field_size_y;
	private int win_len;
	private int cell_width;
	private int cell_height;
	private int[][] field;
	
	Map() {
		setBackground(Color.WHITE);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				update(e);
			}
		});
	}

	private void update(MouseEvent e) {
		if (game_over || !initialized) return;
		int cell_x = e.getX() / cell_width;
		int cell_y = e.getY() / cell_height;
		int dot;
		if (!isValidCell(cell_x, cell_y) || !isEmptyCell(cell_x, cell_y)) return;
		if (player_1_turn) dot = HUMAN_DOT;
		else dot = PLAYER_2_DOT;
		field[cell_y][cell_x] = dot;
		repaint();
		if (checkWin(dot)) {
			if (player_1_turn) game_over_state = HUMAN_WIN;
			else game_over_state = HUMAN_2_WIN;
			game_over = true;
			return;
		}
		if (isMApFull()) {
			game_over_state = DRAW;
			game_over = true;
			return;
		}
		if (game_mode == GAME_MODE_HUMAN_VS_HUMAN) {
			player_1_turn = (!player_1_turn);
			return;
		}
		aiTurn();
		repaint();
		if (checkWin(PLAYER_2_DOT)) {
			game_over_state = AI_WIN;
			game_over = true;
			return;
		}
		if (isMApFull()) {
			game_over_state = DRAW;
			game_over = true;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}
	
	private void render(Graphics g) {
		try {
		img_human_dot = ImageIO.read(new File("src\\images\\x.bmp"));
		img_ai_dot = ImageIO.read(new File("src\\images\\o.bmp"));
		} catch (IOException ex) {
			throw new RuntimeException ("Не удалось открыть файл");
		}
		if (!initialized) return;
		int panel_width = getWidth();
		int panel_height = getHeight();
		cell_width = panel_width / field_size_x;
		cell_height = panel_height / field_size_y;
		for (int i = 0; i <= field_size_y; i++) {
			int y = i * cell_height;
			g.drawLine( 0, y, field_size_x * cell_width, y);
		}
		for (int i = 0; i <= field_size_x; i++) {
			int x = i * cell_width;
			g.drawLine( x, 0, x, field_size_y * cell_height);
		}
		for (int i = 0; i < field_size_y; i++) {
			for (int j = 0; j < field_size_x; j++) {
				if (isEmptyCell(j, i)) continue;
				if (field[i][j] == HUMAN_DOT) {
					g.drawImage(img_human_dot, j * cell_width, i * cell_height, 
							cell_width, cell_height, this);
				}
				else if (field[i][j] == PLAYER_2_DOT) {
					g.drawImage(img_ai_dot, j * cell_width, i * cell_height, 
							cell_width, cell_height, this);
				}
				else throw new RuntimeException("Недопустимое значение поля = " + field[i][j]);
			}
		}
		if (game_over) showGameOver(g);
	}

	private void showGameOver(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect( 0, getHeight()/2 - font.getSize(), getWidth(), 48);
		g.setColor(Color.YELLOW);
		g.setFont(font);
		switch (game_over_state) {
		case DRAW:
			g.drawString(DRAW_MSG, 135, getHeight()/2);
			break;
		case HUMAN_WIN:
			g.drawString(HUMAN_WIN_MSG, 80, getHeight()/2);
			break;
		case AI_WIN:
			g.drawString(AI_WIN_MSG, 40, getHeight()/2);
			break;
		case HUMAN_2_WIN:
			g.drawString(HUMAN_2_WIN_MSG, 90, getHeight()/2);
			break;
		default:
			throw new RuntimeException("Неизвестный game_over_state = " + game_over_state);
		}
	}

    private void aiTurn() {
        if(detectAIWinCell()) return;
        if(detectHumanWinCell()) return;
        int x, y;
        do {
            x = rnd.nextInt(field_size_x);
            y = rnd.nextInt(field_size_y);
        } while (!isEmptyCell(x, y));
        field[y][x] = PLAYER_2_DOT;
        player_1_turn = true;
    }

    private boolean detectAIWinCell(){
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if(isEmptyCell(j, i)) {
                    field[i][j] = PLAYER_2_DOT;
                    if (checkWin(PLAYER_2_DOT)) return true;
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean detectHumanWinCell(){
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if(isEmptyCell(j, i)) {
                    field[i][j] = HUMAN_DOT;
                    if (checkWin(HUMAN_DOT)) {
                        field[i][j] = PLAYER_2_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

	private boolean isMApFull() {
		for (int i = 0; i < field_size_y; i++) {
			for (int j = 0; j < field_size_x; j++) {
				if (field[i][j] == EMPTY_DOT) return false;
			}
		}
		return true;
	}

	private boolean checkWin(int dot) {
        for(int i=0; i < field_size_x; i++){
            for (int j = 0; j < field_size_y; j++) {
                if(checkLine(i, j, 1, 0, win_len, dot)) return true;
                if(checkLine(i, j, 1, 1, win_len, dot)) return true;
                if(checkLine(i, j, 0, 1, win_len, dot)) return true;
                if(checkLine(i, j, 1, -1, win_len, dot)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        final int far_x = x + (len - 1) * vx;
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != dot) return false;
        }
        return true;
    }

	private boolean isEmptyCell(int x, int y) {return field[y][x] == EMPTY_DOT;}

	private boolean isValidCell(int x, int y) {return  x >= 0 && x < field_size_x && y >= 0 && y < field_size_y;}

	public void startNewGame(int mode, int field_size_x, int field_size_y, int win_len) {
		this.game_mode = mode;
		this.field_size_x = field_size_x;
		this.field_size_y = field_size_y;
		this.win_len = win_len;
		field = new int[field_size_y][field_size_x];
		game_over = false;
		initialized = true;
		player_1_turn = true;
		repaint();
	}
}
