package srex.engines;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import srex.models.Page;
import srex.models.Query;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BingSearchAPI extends SearchEngine {


	private final static String mBingURL = "https://api.datamarket.azure.com/Bing/Search/v1/Web?Query=%27";
	private final static String mNumberOfResults = "50";
	private final static String mResultFormat = "json";

	class BingSearchResults {

		public ResultsContent d;

		public class ResultsContent {
			public Result[] results;
			public String __next;
		}

		public class Result {
			public String ID;
			public String Title;
			public String Description;
			public String DisplayUrl;
			public String Url;
			public Metadata __metadata;

		}

		public class Metadata {
			public String uri;
			public String type;
		}

	}

	@Override
	public Page[] getPages(Query query, int size) throws IOException {
		return getBingPages(query.getQuery());
	}
	

	private BingSearchResults getBingResults(String searchString) throws IOException {

		// Replace empty characters in the search string for equivalent ASCII value
		searchString = searchString.replaceAll(" ", "%20");

		// Define Bing credentials
		String accountKey="FKCnseSiVMVut/uO7otnQ20cIbhbEvlzCndml7lfSVw";
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		// Define query URL for Bing service
		URL url = new URL(mBingURL + searchString + "%27&" + "$top=" + mNumberOfResults + "&$format=" + mResultFormat);

		// Define the HTTP connection
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
		conn.setRequestProperty("Accept", "application/json");


		BufferedReader br = new BufferedReader(
				new InputStreamReader(
					conn.getInputStream()
				));

		String output = "";
		String temp = "";

		while ((temp = br.readLine()) != null) output += temp;

		Gson json = (new GsonBuilder()).create();
		BingSearchResults results = json.fromJson(output, BingSearchResults.class);
		conn.disconnect();

		return results;
	}



	private Page[] getBingPages(String searchString) throws IOException {

		BingSearchResults results = getBingResults(searchString);

		Page[] pages = new Page[results.d.results.length];

		for (int i=0; i < results.d.results.length; i++) {
			pages[i] = new Page(results.d.results[i].Url, results.d.results[i].Title, results.d.results[i].Description);
		}
		return pages;

    }
	
}

