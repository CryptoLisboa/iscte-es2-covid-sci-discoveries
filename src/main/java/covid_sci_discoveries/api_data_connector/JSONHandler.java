package covid_sci_discoveries.api_data_connector;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONHandler {
	public static JSONObject getJSONObjectFromFile(String jsonFilePath) {
		JSONObject jsonObjectFromFile = null;
		JSONParser parser = new JSONParser();
		if(App.withLogging)
			System.out.println("Loading JSONObject from file path: " + jsonFilePath);
		try {
			Object obj = parser.parse(new FileReader(jsonFilePath));
			// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
			jsonObjectFromFile = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			if(App.withLogging)
				System.out.println("New storage will be generated");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObjectFromFile;
	}
}
