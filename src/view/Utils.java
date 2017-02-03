package view;

public class Utils {
	public String getFileExtension(String name) {
		int pointIndex = name.lastIndexOf(".");

		if (pointIndex == -1) {
			return null;
		} else if (pointIndex == name.length() - 1) {
			return null;
		}

		return name.substring(pointIndex + 1);

	}
}