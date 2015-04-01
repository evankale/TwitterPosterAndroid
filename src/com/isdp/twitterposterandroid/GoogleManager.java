/*
 * Copyright (c) 2015 Evan Kale
 * Email: EvanKale91@gmail.com
 * Website: www.ISeeDeadPixel.com
 *
 * This file is part of TwitterPosterAndroid.
 *
 * TwitterPosterAndroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isdp.twitterposterandroid;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleManager
{
	private static GoogleManager instance;

	private GoogleManager()
	{
	}

	public static GoogleManager getInstance()
	{
		if (instance == null)
			instance = new GoogleManager();

		return instance;
	}

	/*
	 * Google JSON search with REST guide:
	 * https://developers.google.com/custom-search/json-api/v1/using_rest
	 *
	 * Get API key here:
	 * https://console.developers.google.com/
	 *
	 * Example text message:
	 * wiki randstr 15 longos
	 * (search wiki for 15 results, use text shortening, search term = longos)
	 *
	 */

	private static final String API_KEY = "<YOUR GOOGLE API HERE>";
	public static final String SEARCH_YELP = "<YOUR YELP SEARCH ENGINE ID HERE>";
	public static final String SEARCH_WIKI = "<YOUR WIKI SEARCH ENGINE ID HERE>";
	public static final String TAG_YELP = "yelp";
	public static final String TAG_WIKI = "wiki";

	public String[] trySearch(String searchQuery, String searchEngineID)
	{
		try
		{
			String query = URLEncoder.encode(searchQuery);
			URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + API_KEY + "&cx=" + searchEngineID
					+ "&q=" + query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			InputStream inputStream = conn.getInputStream();
			String jsonStr = null;
			if (inputStream != null)
				jsonStr = Util.convertInputStreamToString(inputStream);

			JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

			String[] results = null;

			if (searchEngineID.equals(SEARCH_WIKI))
				results = parseWiki(jsonObj);

			else if (searchEngineID.equals(SEARCH_YELP))
				results = parseYelp(jsonObj);

			conn.disconnect();

			return results;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String[] parseWiki(JSONObject jsonObj)
	{
		try
		{
			ArrayList<String> results = new ArrayList<String>();

			JSONArray itemsArray = jsonObj.getJSONArray("items");

			for (int i = 0; i < itemsArray.length(); ++i)
			{
				JSONObject item = itemsArray.getJSONObject(i);
				String itemSnippet = item.getString("snippet");
				results.add(itemSnippet);
			}

			String[] resultsArray = new String[results.size()];
			return results.toArray(resultsArray);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String[] parseYelp(JSONObject jsonObj)
	{
		try
		{
			ArrayList<String> results = new ArrayList<String>();

			JSONArray itemsArray = jsonObj.getJSONArray("items");

			for (int i = 0; i < itemsArray.length(); ++i)
			{
				try
				{
					JSONObject item = itemsArray.getJSONObject(i);
					JSONObject localbusiness = item.getJSONObject("pagemap").getJSONArray("localbusiness")
							.getJSONObject(0);
					JSONObject postaladdress = item.getJSONObject("pagemap").getJSONArray("postaladdress")
							.getJSONObject(0);
					JSONObject aggregaterating = item.getJSONObject("pagemap").getJSONArray("aggregaterating")
							.getJSONObject(0);

					String textString = Util.generateRandomString(5) + "\n";
					textString += Util.truncateString(localbusiness.getString("name"), 30) + "\n";
					textString += localbusiness.getString("telephone") + "\n";
					textString += postaladdress.getString("streetaddress") + "\n";
					textString += postaladdress.getString("addresslocality") + " "
							+ postaladdress.getString("addressregion") + "\n";
					textString += postaladdress.getString("postalcode") + "\n";
					textString += "R|" + aggregaterating.getString("ratingvalue") + "\n";
					textString += "P|" + localbusiness.getString("pricerange") + "\n";

					results.add(textString);
				}
				catch (Exception e)
				{
				}
			}

			String[] resultsArray = new String[results.size()];
			return results.toArray(resultsArray);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
