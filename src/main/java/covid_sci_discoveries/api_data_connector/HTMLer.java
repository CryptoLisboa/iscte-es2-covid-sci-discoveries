package covid_sci_discoveries.api_data_connector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ListIterator;

import org.json.simple.JSONObject;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentDate;

class HTMLer {

	static String generateHTMLFromFile(String PDF_DIR_PATH, File fileEntry, Boolean optimizeIOWithLocalResources) {
		if(App.withLogging)
			System.out.println("generateHTMLFromFile pdf named: " + fileEntry.getName() + "\n");
		String fileName = fileEntry.getName();
		String filePath = PDF_DIR_PATH + "/" + fileName;
		ContentExtractor extractor;
		String HTML_TABLE_FROM_FILE = null;
		Boolean isResourceNew = FileSystemStorage.isResourceNew(fileName);
		String title = null;
		String journal = null;
		String publicationYear = null;
		String authorsNames = null;
		String link = null;
		if (!isResourceNew) {
			JSONObject StoredFile = FileSystemStorage.getFileFromStorage(fileName);
			title = (String) StoredFile.get(FileSystemStorage.TITLE);
			journal = (String) StoredFile.get(FileSystemStorage.JOURNAL);
			publicationYear = (String) StoredFile.get(FileSystemStorage.PUBLICATION_YEAR);
			authorsNames = (String) StoredFile.get(FileSystemStorage.AUTHORS_NAMES);
			link = (String) StoredFile.get(FileSystemStorage.LINK);
		} else {
			try {
				extractor = new ContentExtractor();
				InputStream inputStream = new FileInputStream(filePath);
				extractor.setPDF(inputStream);
				// Article title, Journal name, Publication year and Authors.
				title = extractor.getMetadata().getTitle();
				journal = extractor.getMetadata().getJournal();
				DocumentDate documentDate = extractor.getMetadata().getDate(DateType.PUBLISHED);
				publicationYear = documentDate.getYear().toString();
				ListIterator<DocumentAuthor> authors = extractor.getMetadata().getAuthors().listIterator();
				DocumentAuthor author;
				authorsNames = "";
				while (authors.hasNext()) {
					author = authors.next();
					String authorName = author.getName();
					if (authorsNames.length() == 0) {
						authorsNames += authorName;
					} else {
						authorsNames += ", " + authorName;
					}
				}
				File filePDF = new File(PDF_DIR_PATH);
				link = filePDF.toURI().toString()+fileName;
				if (optimizeIOWithLocalResources) {
					FileSystemStorage.storeDataToResources(fileName, title, journal, publicationYear, authorsNames, link);
				}
			} catch (AnalysisException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		HTML_TABLE_FROM_FILE = "<tr>\n" + 
				"    <th>"+title+"</th>\n" + 
				"    <th>"+journal+"</th> \n" + 
				"    <th>"+publicationYear+"</th>\n" +
				"    <th>"+authorsNames+"</th>\n" +
				"    <th><a href=\""+ URI.create(link) +"\" target=\"_blank\">"+fileName+"</a></th>\n" +
				"  </tr>" + "\n";
		return HTML_TABLE_FROM_FILE;
	}
}
