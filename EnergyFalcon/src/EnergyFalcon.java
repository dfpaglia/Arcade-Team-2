import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;

public class EnergyFalcon extends Game {
	Image cover, background;

	private Player p;
	private GenericEnemy e;
	private PlayerHealth h;
	int hCount = 4;

	java.applet.AudioClip clip = java.applet.Applet.newAudioClip(this.getClass().getResource("music.wav"));
	java.applet.AudioClip noise = java.applet.Applet.newAudioClip(this.getClass().getResource("sound.aiff"));
	boolean playing = false;

	public EnergyFalcon() {

		p = new Player();
		e = new GenericEnemy(p);
		h = new PlayerHealth();

		try {
			cover = ImageIO.read(this.getClass().getResource("cover.jpg"));
			background = ImageIO.read(this.getClass().getResource("ArenaFix.png")).getScaledInstance(Game.WIDTH,
					Game.HEIGHT, 0);
			;
		} catch (IOException e) {
			e.printStackTrace();
		}

		noise.loop();
	}

	public static void main(String[] args) {

		Arcadia.display(new Arcadia(new Game[] { new EnergyFalcon() }));

	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.drawImage(background, 0, 0, null);
		graphics.drawImage(h.healthDraw(hCount), -20, -20, null);
		p.onTick(input);
		e.onTick(input);
		p.draw(graphics);
		e.draw(graphics);
		if(!playing){
			noise.stop();
			clip.loop();
			playing = true;
		}
	}

	@Override
	public Image cover() {
		return cover;
	}

}
