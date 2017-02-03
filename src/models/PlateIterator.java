package models;

import java.util.Iterator;
import java.util.List;

public class PlateIterator implements Iterator<Plate> {
	private List<Plate> list;
	private int count;

	public PlateIterator(List<Plate> list) {
		this.list = list;
		count = 0;
	}

	@Override
	public boolean hasNext() {
		return count < list.size();
	}

	@Override
	public Plate next() {
		if (hasNext()) {
			return list.get(count++);
		}
		return null;
	}

}
