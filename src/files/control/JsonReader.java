package files.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader implements IReader {
	private ArrayList<String> coulmnNames, coulmnTypes;

	public JsonReader() {
		coulmnNames = new ArrayList<String>();
		coulmnTypes = new ArrayList<String>();
	}

	@Override
	public ArrayList<ArrayList<String>> load(File file) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		JSONParser jsonParser = new JSONParser();
		Object object;
		try {
			object = jsonParser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) object;
			int sizeRow = Integer.parseInt(((String) jsonObject.get("sizeRow")));
			int sizeCol = Integer.parseInt(((String) jsonObject.get("sizeCol")));
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < sizeRow; i++) {
				jsonArray = (JSONArray) jsonObject.get("Row" + i);
				ArrayList<String> row = new ArrayList<String>();
				for (int j = 0; j < sizeCol; j++) {
					row.add((String) jsonArray.get(j));
				}
				data.add(row);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public ArrayList<String> getCoulmnNames() {
		return coulmnNames;
	}

	@Override
	public ArrayList<String> getCoulmnTypes() {
		return coulmnTypes;
	}

}
