package control;

public class DifficultStrategy implements Strategy {
	private final int Speed = 200;
	private final int score = 10;

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
