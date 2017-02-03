package models;

public class PlateCaptured implements State {

	private Plate plate;

	public PlateCaptured(Plate plate) {
		this.plate = plate;
		plate.stop();
	}

	@Override
	public void move(int offset) {
		int x = plate.getCurrentPosition().x + offset;
		int y = plate.getCurrentPosition().y;
		plate.setCurrentPosition(x, y);
	}

}
