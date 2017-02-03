package control;

public class MediumStrategy implements Strategy{
	private final int speed = 500;
	private final int score = 5;
	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public int getMaxScore() {
		// TODO Auto-generated method stub
		return score;
	}
}
