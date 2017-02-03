package models;

public class PlateProjected implements State {
	Plate plate;

	public PlateProjected(Plate plate, int vi, int direction) {
		this.plate = plate;
		plate.project(vi, direction);
	}

	@Override
	public void move(int offset) {
		project();
	}

	private void project() {

		double xSquared = ((2 / 9.8) * Math.pow(plate.getInitialVelocity(), 2)
				* (plate.getCurrentPosition().y - plate.getOriginPosition().y));
		int x = plate.getOriginPosition().x
				+ plate.getDirection() * (int) Math.sqrt(xSquared);
		int y = plate.getCurrentPosition().y;
		y += 4;
		plate.setCurrentPosition(x, y);
		// if (x > 300 || y > 300) {
		// vis = false;
		// }
	}
}
