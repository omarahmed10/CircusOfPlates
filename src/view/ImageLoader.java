package view;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

public class ImageLoader {
	public static ImageIcon loadImage(String imageName) {
		String path;
		File f = null;
		try {
			String workingDir = System.getProperty("user.dir");
			path = new File(workingDir + File.separator + "src" + File.separator
					+ "resources").getCanonicalPath();
			f = new File(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon ii = new ImageIcon(
				f.getAbsolutePath() + File.separator + imageName);
		return ii;
	}
}
