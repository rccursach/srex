package srex.engines.implementations;

import srex.engines.SearchEngine;
import srex.models.Page;
import srex.models.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GoogleSearchAPI extends SearchEngine {

	private String key = "";
	private String cx = "";
	
	private String url = "https://www.googleapis.com/customsearch/v1";
	private Scanner s;
	
	
	public GoogleSearchAPI(String api_key, String api_cx) {
		super();
		this.key = api_key != null ? api_key : "";
		this.cx = api_cx != null ? api_cx : "";
	}

	@Override
	public Page[] getPages(Query query, int size) throws IOException {
		query.getQuery();
		Page[][] pages = new Page[size][10];
		Page[] result = new Page[size*10];
		String u = url+"?cx="+cx+"&key="+key+"&q="+query;
		for (int i=1; i < size*10; i+=10)
		{
			pages[(i-1)/10] = getPagesFromJson(getJsonFromUrl(u+"&start="+i));
		}
		
		for (int i=0; i<size; i++)
		{
			for (int j=0; j < 10; j++)
			{
				result[i*10+j] = pages[i][j];
			}
		}
		
		return result;
	}
	
	
	private Page[] getPagesFromJson(String json)
	{
		Page[] pages = new Page[10];
		JSONObject rootObject = (JSONObject) JSONValue.parse(json);
		JSONArray items = (JSONArray) rootObject.get("items");
		
		for (int i=0; i < 10; i++)
		{
			JSONObject t = (JSONObject) items.get(i);
			pages[i] = new Page((String)t.get("link"), (String)t.get("title"), (String)t.get("snippet"));
		}
		return pages;
	}
	
	
	private String getJsonFromUrl(String url) throws IOException
	{
		URL jsonUrl = new URL(url);
		String json = "";
		s = new Scanner(jsonUrl.openStream());
		while (s.hasNext()) {
			json += s.nextLine();
		}
		return json;
	}
	
}
