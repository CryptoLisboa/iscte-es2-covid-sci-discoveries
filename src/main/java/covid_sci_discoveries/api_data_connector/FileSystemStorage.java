package covid_sci_discoveries.api_data_connector;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileSystemStorage {
	static String BASE_PATH = App.BASE_PATH;
	static String JSON_STORAGE_PATH = BASE_PATH + "/storage.json";
	public final static String FILE_NAME = "FileName";
	public final static String TITLE = "Title";
	public final static String JOURNAL = "Journal";
	public final static String PUBLICATION_YEAR = "PublicationYear";
	public final static String AUTHORS_NAMES = "AuthorsNames";
	public final static String RESOURCES = "Resources";
	public final static String LINK ="link";

	@SuppressWarnings("unchecked")
	public static void storeDataToResources(String fileName, String title, String journal, String publicationYear, String authorsNames, String link) {
		try {
			JSONArray resources = getResources();
			if (resources == null) {
				resources = new JSONArray();
			}
			JSONObject jsonObject = generateJSONObject(fileName, title, journal, publicationYear, authorsNames, link);
			if (jsonObject != null) {
				resources.add(jsonObject);
				storeUpdatedResourcesToFile(resources);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private static void storeUpdatedResourcesToFile(JSONArray resourcesToStore) {
		try {
			JSONObject storage = new JSONObject();
			storage.put(RESOURCES, resourcesToStore);
			FileWriter file = null;
			try {
				file = new FileWriter(JSON_STORAGE_PATH);
				file.write(storage.toJSONString());
				if(App.withLogging)
					System.out.println("Successfully Copied JSON Object to File: " + JSON_STORAGE_PATH);
				if(App.withLogging)
					System.out.println("\nJSON Object: " + storage);

			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				try {
					file.flush();
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Boolean isResourceNew(String fileName) {
		JSONArray resourcesToHandle = getResources();
		if(resourcesToHandle == null) {			
			return true;
		}
		Boolean isNewResource = true;
		try {
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = resourcesToHandle.iterator();
			while (iterator.hasNext() && isNewResource) {
				JSONObject resource = iterator.next();
				String suspectFileName = (String) resource.get(FILE_NAME);
				if (fileName.equals(suspectFileName)) {
					isNewResource = false;
					if(App.withLogging)
						System.out.println("Previously loaded resource: " + fileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNewResource;
	}

	public static JSONArray getResources() {
		JSONArray resources = null;
		try {
			JSONObject storage = JSONHandler.getJSONObjectFromFile(JSON_STORAGE_PATH);
			resources = (JSONArray) storage.get(RESOURCES);
		} catch (Exception e) {
			return null;
		}
		return resources;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject generateJSONObject(String fileName, String title, String journal, String publicationYear, String authorsNames, String link) {
		JSONObject jsonObj = null;
		try {
			jsonObj  = new JSONObject();
			jsonObj.put(FILE_NAME, fileName);
			jsonObj.put(TITLE, title);
			jsonObj.put(JOURNAL, journal);
			jsonObj.put(PUBLICATION_YEAR, publicationYear);
			jsonObj.put(AUTHORS_NAMES, authorsNames);
			jsonObj.put(LINK, link);
		} catch (Exception e) {
			if(App.withLogging)
				System.out.println(e);
		}
		return jsonObj;
	}

	public static JSONObject getFileFromStorage(String fileName) {
		JSONObject jsonObjectStored = null;
		try {
			JSONArray resources = getResources();
			if (resources == null) {
				return jsonObjectStored;
			}
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = resources.iterator();
			while (iterator.hasNext() && jsonObjectStored == null) {
				JSONObject resource = iterator.next();
				String suspectFileName = (String) resource.get(FILE_NAME);
				if (fileName.equals(suspectFileName)) {
					jsonObjectStored = resource;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObjectStored;
	}
}
