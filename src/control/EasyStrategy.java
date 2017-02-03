package control;

public class EasyStrategy implements Strategy {
	private final int Speed = 1000;
	private final int score = 3;

	@Override
	public int getSpeed() {
		return Speed;
	}

	@Override
	public int getMaxScore() {
		// TODO Auto-generated method stub
		return score;
	}
}
