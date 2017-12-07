package files.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonWriter implements IWriter {

	public JsonWriter() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(File file, ArrayList<ArrayList<String>> data, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes, String tableName) {
		// TODO Auto-generated method stub

		int sizeCol = coulmnNames.size();
		int sizeRow = data.size();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sizeCol", "" + sizeCol);
		jsonObject.put("sizeRow", "" + sizeRow);
		JSONArray array;
		for (int i = 0; i < data.size(); i++) {
			ArrayList<String> row = data.get(i);
			array = new JSONArray();
			for (int j = 0; j < row.size(); j++) {
				array.add("" + row.get(j));

			}
			jsonObject.put("Row" + i, array);
		}
		try {
			FileWriter x = new FileWriter(file);
			x.write(jsonObject.toJSONString());
			x.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
