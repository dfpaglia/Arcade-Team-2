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

	Image cover;

	public EnergyFalcon() {

		try {
			cover = ImageIO.read(this.getClass().getResource("cover.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Arcadia.display(new Arcadia(new Game[] { new EnergyFalcon(), new DodgeGame(), new Shooter() }));
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image cover() {
		return cover;
	}
}