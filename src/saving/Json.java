package saving;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import control.PlateFactory;
import models.Plate;
import view.Board;

public class Json {

	private FileWriter fWriter;
	private Gson gson;
	private String prettyJsonString;
	private PlateFactory plateFactory;

	public Json(PlateFactory plateFactory) {
		this.plateFactory = plateFactory;
		fWriter = null;
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	public void save() throws IOException {
		JsonClass jC = new JsonClass(plateFactory.getArray(),
				plateFactory.getUsablePlate());
		try {
			
			prettyJsonString = gson.toJson(jC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File("plates.json");
		// System.out.println(file.getPath());
		fWriter = new FileWriter(file);
		fWriter.write(prettyJsonString);
		fWriter.flush();
		fWriter.close();

	}

	public void load(File f) {
		try {
			JsonClass jC = gson.fromJson(new FileReader(f), JsonClass.class);
			plateFactory.setLoadedPlates(jC.list1, jC.list2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class JsonClass {
	List<Plate> list1, list2;

	public JsonClass(List<Plate> list1, List<Plate> list2) {
		this.list1 = list1;
		this.list2 = list2;
	}
}