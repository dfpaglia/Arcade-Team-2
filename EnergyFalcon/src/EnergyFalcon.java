import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;
import dodge.DodgeGame;
import shooter.Shooter;

public class EnergyFalcon extends Game {
	Image cover, background;
	private Player p;
	private GenericEnemy e;

	public EnergyFalcon() {
		p = new Player();
		e = new GenericEnemy();
		
		try {
			cover = ImageIO.read(this.getClass().getResource("cover.jpg"));
			background = ImageIO.read(this.getClass().getResource("Arena2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Arcadia.display(new Arcadia(new Game[] {new EnergyFalcon()}));
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		// TODO Auto-generated method stub
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.drawImage(background, 0, 0, null);
		p.onTick(input);
		e.onTick(input);
		p.draw(graphics);
		e.draw(graphics);
	}

	@Override
	public Image cover() {
		return cover;
	}

}
