import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Arcadia;
import arcadia.Button;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;

public class EnergyFalcon extends Game {
	Image cover, background;
	private GameState state = GameState.START;
	private Player p;
	private Wall[] walls;
	private Image win, lose;
	SpawnTracker spawn;

	java.applet.AudioClip backgroundMusic = java.applet.Applet
			.newAudioClip(this.getClass().getResource("backgroundmusic.wav"));
	java.applet.AudioClip coverMusic = java.applet.Applet.newAudioClip(this.getClass().getResource("covermusic.wav"));
	java.applet.AudioClip defeatMusic = java.applet.Applet.newAudioClip(this.getClass().getResource("dead.wav"));
	java.applet.AudioClip victoryMusic = java.applet.Applet.newAudioClip(this.getClass().getResource("popdance.wav"));
	private boolean started = false;
	private boolean lost = false;
	private boolean won = false;

	public EnergyFalcon() {

		p = new Player();
		walls = new Wall[4];
		spawn = new SpawnTracker(p);

		walls[Wall.LEFT_WALL] = new Wall(0, 0, 80, Game.HEIGHT, Wall.LEFT_WALL);
		walls[Wall.RIGHT_WALL] = new Wall(Wall.RIGHT_WALL_EDGE, 0, 80, Game.HEIGHT, Wall.RIGHT_WALL);
		walls[Wall.TOP_WALL] = new Wall(0, 0, Game.WIDTH, 40, Wall.TOP_WALL);
		walls[Wall.BOTTOM_WALL] = new Wall(0, Wall.BOTTOM_WALL_EDGE, Game.WIDTH, 80, Wall.BOTTOM_WALL);

		try {
			cover = ImageIO.read(this.getClass().getResource("cover.png"));
			background = ImageIO.read(this.getClass().getResource("ArenaFix.png")).getScaledInstance(Game.WIDTH,
					Game.HEIGHT, 0);
			win = ImageIO.read(this.getClass().getResource("WinScreen.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT,
					0);
			lose = ImageIO.read(this.getClass().getResource("GameOverScreen.png")).getScaledInstance(Game.WIDTH,
					Game.HEIGHT, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		coverMusic.loop();
	}

	public static void main(String[] args) {
		Arcadia.display(new Arcadia(new EnergyFalcon()));
	}

	public void tick(Graphics2D graphics, Input input, Sound sound) {
		switch (state) {
		case START:
			graphics.drawImage(cover, 0, 0, null);
			if (input.pressed(Button.A)) {
				state = GameState.PLAY; // Starts game
			}
			if (input.pressed(Button.S)) {
				state = GameState.CONTROLS; // Views controls
			}
			break;
		case CONTROLS:
			//TODO make controls screen graphics.drawImage(controls, 0, 0, null);
			if (input.pressed(Button.B)) {
				state = GameState.START; // Goes back to start screen
			}
			break;
		case PLAY:
			if (!started) {
				init();
			}
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			graphics.drawImage(background, 0, 0, null);
			p.onTick(input);
			spawn.onTick(input);

			CollisionTracker.handleCollisions();

			p.draw(graphics);
			spawn.draw(graphics);

			if (p.getPlayerHealth() <= 0) {
				state = GameState.DEFEAT; // Ends game because player lost
			}
			
			if (spawn.winner()) {
				state = GameState.VICTORY; // Ends game because player won
			}
			if (input.pressed(Button.S)) {
				state = GameState.PAUSE; // Pauses game
			}
			break;
		case PAUSE:
			//TODO make pause screen graphics.drawImage(pause, 0, 0, null);
			if (input.pressed(Button.B)) {
				state = GameState.START; // Returns to start menu
			}
			if (input.pressed(Button.S)) {
				state = GameState.PLAY; // Resumes game
			}
			if (input.pressed(Button.A)) {
				state = GameState.CONTROLS; // View controls
			}
			break;
		case DEFEAT:
			if (!lost) {
				deadinit();
			}
			graphics.drawImage(lose, 0, 0, null);
			if (input.pressed(Button.A)) {
				state = GameState.PLAY; // Starts game again
			}
			if (input.pressed(Button.S)) {
				state = GameState.START; // Returns to start menu
			}
			break;
		case VICTORY:
			if (!won) {
				wininit();
			}
			graphics.drawImage(win, 0, 0, null);
			if (input.pressed(Button.A)) {
				state = GameState.PLAY; // Starts game again
			}
			if (input.pressed(Button.S)) {
				state = GameState.START; // Returns to start menu
			}
			break;
		default:
			break;
		}

	}

	private void init() {
		started = true;
		coverMusic.stop();
		backgroundMusic.loop();
	}

	private void deadinit() {
		lost = true;
		backgroundMusic.stop();
		defeatMusic.loop();
	}

	private void wininit() {
		won = true;
		backgroundMusic.stop();
		victoryMusic.loop();
	}

	public Image cover() {
		return cover;
	}

}
