package covid_sci_discoveries.api_data_connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;

import org.jdom.Element;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.content.model.ContentStructure;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentDate;

/**
 * Hello world!
 *
 */
public class App 
{
	static String BASE_PATH = System.getProperty("user.dir");
	static String PDF_DIR_PATH = BASE_PATH + "/pdfs";
	static String HTML_PREFIX = "<!DOCTYPE html>\n" + 
			"<html>\n" + 
			"<body>";
	static String HTML_SUFIX = "</body>\n" + 
			"</html>";
	static String HTML_TABLE_INIT = "<table style=\"width:100%\">\n" + 
			"  <tr>\n" + 
			"    <th>Title</th>\n" + 
			"    <th>Journal Name</th> \n" + 
			"    <th>Year</th>\n" + 
			"    <th>Authors</th>\n" + 
			"  </tr>";
	static String HTML_TABLE_END = "</table>";
	public static void main( String[] args )
	{
		System.out.println("INIT");
		final File folder = new File(PDF_DIR_PATH);
		System.out.println("Started iterating on folder: " + PDF_DIR_PATH);
		String HTML_TABLE_CONTENT = "";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				String fileName = fileEntry.getName();
				String filePath = PDF_DIR_PATH + "/" + fileName;
				System.out.println(fileName + "\n");
				ContentExtractor extractor;
				try {
					extractor = new ContentExtractor();
					InputStream inputStream = new FileInputStream(filePath);
					extractor.setPDF(inputStream);
					// Article title, Journal name, Publication year and Authors.
					String title = extractor.getMetadata().getTitle();
					System.out.println("Title: " + title);
					String journal = extractor.getMetadata().getJournal();
					System.out.println("Journal: " + journal);
					DocumentDate documentDate = extractor.getMetadata().getDate(DateType.PUBLISHED);
					String publicationYear = documentDate.getYear().toString();
					System.out.println("publicationYear: " + publicationYear);
					ListIterator<DocumentAuthor> authors = extractor.getMetadata().getAuthors().listIterator();
					DocumentAuthor author;
					String authorsNames = "";
					while (authors.hasNext()) {
						author = authors.next();
						String authorName = author.getName();
						if (authorsNames.length() == 0) {
							authorsNames += authorName;
						} else {
							authorsNames += ", " + authorName;
						}
					}
					if (HTML_TABLE_CONTENT.length() == 0) {
						HTML_TABLE_CONTENT = "<tr>\n" + 
								"    <th>"+title+"</th>\n" + 
								"    <th>"+journal+"</th> \n" + 
								"    <th>"+publicationYear+"</th>\n" +
								"    <th>"+authorsNames+"</th>\n" +
								"  </tr>";
					} else {
						HTML_TABLE_CONTENT += "\n"+"<tr>\n" + 
								"    <th>"+title+"</th>\n" + 
								"    <th>"+journal+"</th> \n" + 
								"    <th>"+publicationYear+"</th>\n" +
								"    <th>"+authorsNames+"</th>\n" +
								"  </tr>";
					}
					System.out.println("Authors Names: " + authorsNames  + "\n");
				} catch (AnalysisException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		String HTML_FINAL_TABLE = HTML_TABLE_INIT + HTML_TABLE_CONTENT + HTML_TABLE_END;
		System.out.println(HTML_FINAL_TABLE);
		System.out.println("Finished iterating on folder: " + PDF_DIR_PATH);
		/*
    	ContentExtractor extractor = new ContentExtractor();
		InputStream inputStream = new FileInputStream("path/to/pdf/file");
		extractor.setPDF(inputStream);
		Element result = extractor.getContentAsNLM();

			<dependency>
	    <groupId>pl.edu.icm.cermine</groupId>
	    <artifactId>cermine-impl_2.12</artifactId>
	    <version>1.13</version>
	</dependency>
		 */
	}
}
