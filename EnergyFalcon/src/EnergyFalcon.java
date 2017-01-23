import java.awt.Graphics2D;
import java.awt.Image;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;
import dodge.DodgeGame;
import shooter.Shooter;

public class EnergyFalcon extends Game{
	
	public static void main(String[] args){
		Arcadia.display(new Arcadia(new Game[ ] {new DodgeGame(), new Shooter() }));
	}
	
	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		
	}

	@Override
	public Image cover() {
		return null;
	}

}