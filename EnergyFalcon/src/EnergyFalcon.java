import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;
import dodge.DodgeGame;
import shooter.Shooter;

public class EnergyFalcon extends Game{
	
	private static Player p;
	
	public static void main(String[] args){
		p = new Player();
		//Test code in line below
		Arcadia.display(new Arcadia(new Game[ ] {/*new EnergyFalcon(),*/ new DodgeGame(), new Shooter() }));
	}
	
	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		p.onTick(input);
	}

	@Override
	public Image cover() {
		//Test code
		//return new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		return null;
	}

}