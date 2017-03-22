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
	private GenericEnemy e;
	private Wall[] walls;
	private Image win, lose;
	
	java.applet.AudioClip clip = java.applet.Applet.newAudioClip(this.getClass().getResource("background.wav"));
	java.applet.AudioClip noise = java.applet.Applet.newAudioClip(this.getClass().getResource("covermusic.wav"));
	private boolean started = false;

	public EnergyFalcon() {

		p = new Player();
		e = new GenericEnemy(p);
		walls = new Wall[4];
		
		walls[Wall.LEFT_WALL] = new Wall(0,0,80, Game.HEIGHT, Wall.LEFT_WALL);
		walls[Wall.RIGHT_WALL] = new Wall(Wall.RIGHT_WALL_EDGE,0,80, Game.HEIGHT, Wall.RIGHT_WALL);
		walls[Wall.TOP_WALL] = new Wall(0,0,Game.WIDTH, 40, Wall.TOP_WALL);
		walls[Wall.BOTTOM_WALL] = new Wall(0,Wall.BOTTOM_WALL_EDGE, Game.WIDTH, 80, Wall.BOTTOM_WALL);
		
		
		try {
			cover = ImageIO.read(this.getClass().getResource("cover.png"));
			background = ImageIO.read(this.getClass().getResource("ArenaFix.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
			win = ImageIO.read(this.getClass().getResource("Victory Screen.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
			lose = ImageIO.read(this.getClass().getResource("Defeat Screen.png")).getScaledInstance(Game.WIDTH, Game.HEIGHT, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		noise.loop();
	}

	public static void main(String[] args) {
		Arcadia.display(new Arcadia(new EnergyFalcon()));
	}

	public void tick(Graphics2D graphics, Input input, Sound sound) {
		switch(state){
		case START:
			graphics.drawImage(cover, 0, 0, null);
			if(input.pressed(Button.A)){
				state = GameState.PLAY;
			}
			break;
		case PLAY:
			if(!started){
				init();
			}
			
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			graphics.drawImage(background, 0, 0, null);
			p.onTick(input);
			e.onTick(input);
			
			CollisionTracker.handleCollisions();
			
			p.draw(graphics);
			e.draw(graphics);
			
			
			if(p.getPlayerHealth() <= 0){
				state = GameState.DEFEAT;
			}
			
			if(e.getEnemyHealth() <= 0){
				state = GameState.VICTORY;
			}
			break;
		case DEFEAT:
			graphics.drawImage(lose,0,0,null);
			break;
		case VICTORY:
			graphics.drawImage(win,0,0,null);
			break;
		default:
			break;
		}
		}
	
	private void init(){
		noise.stop();
		clip.loop();
		started = true;
	}
	
	public Image cover() {
		return cover;
	}

}
