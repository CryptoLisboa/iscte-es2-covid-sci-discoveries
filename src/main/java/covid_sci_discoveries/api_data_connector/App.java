package covid_sci_discoveries.api_data_connector;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

import cgi.cgi_lib;

public class App 
{
	public static String BASE_PATH = System.getProperty("user.dir");
	static String PDF_DIR_PATH = BASE_PATH + "/pdfs";
	static String RESOURCES_DIR_PATH = BASE_PATH + "/resources_covid_sci_discoveries";
	static String jsonPath = BASE_PATH + "/configuration.json";

	static String HTML_TABLE_INIT = "<table style=\"width:100%\">\n" + "  <tr>\n" + "    <th>Title</th>\n" + "    <th>Journal Name</th> \n" + "    <th>Year</th>\n" + "    <th>Authors</th>\n" + "    <th>Link</th>\n" + "  </tr>" + "\n";
	static String HTML_TABLE_END = "</table>";

	private static Boolean optimizeIOWithLocalResources = false;
	public static Boolean withLogging = false;
	private static Boolean wordpressCGI = false;

	public static void main( String[] args )
	{
		loadConfigurationFromJsonFile();
		if(withLogging)
			System.out.println("INIT");
		if (wordpressCGI) {
			System.out.println(cgi_lib.Header());
			System.out.println(cgi_lib.HtmlTop("Covid Scientific Discoveries Repository"));
		}
		final File folder = new File(PDF_DIR_PATH);
		if(withLogging)
			System.out.println("Started iterating on folder: " + PDF_DIR_PATH);
		String HTML_TABLE_CONTENT_FROM_FILE = "";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				String HTML_TABLE_FROM_FILE = HTMLer.generateHTMLFromFile(PDF_DIR_PATH, fileEntry, optimizeIOWithLocalResources );
				HTML_TABLE_CONTENT_FROM_FILE += HTML_TABLE_FROM_FILE;
			}
		}
		String HTML_FINAL_TABLE = HTML_TABLE_INIT + HTML_TABLE_CONTENT_FROM_FILE + HTML_TABLE_END;
		System.out.println(HTML_FINAL_TABLE);
		if(withLogging)
			System.out.println("Finished iterating on folder: " + PDF_DIR_PATH);
		if (wordpressCGI) {
			System.out.println(cgi_lib.HtmlBot());
		}
		else {
			File f = new File("CovidCriteria.html");
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(HTML_FINAL_TABLE);
				bw.close();
				Desktop.getDesktop().browse(f.toURI());
			} catch (IOException e) {
				if(withLogging)
					System.out.println(e);
			}
		}
	}

	private static void loadConfigurationFromJsonFile() {
		// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
		JSONObject jsonObject = JSONHandler.getJSONObjectFromFile(jsonPath);
		// A JSON object
		optimizeIOWithLocalResources = (Boolean) jsonObject.get("OptimizeIOWithLocalResources");
		withLogging  = (Boolean) jsonObject.get("WithLogging");
		wordpressCGI   = (Boolean) jsonObject.get("WordpressCGI");
		if(withLogging)
			System.out.println("Configurations loaded");
	}
}
