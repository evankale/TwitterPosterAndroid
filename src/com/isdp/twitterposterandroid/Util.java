package com.isdp.twitterposterandroid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Util
{
	private static Random rand;

	public static String generateRandomString(int length)
	{
		if (rand == null)
			rand = new Random();

		String retStr = "";

		int range = 'z' - 'a';

		for (int i = 0; i < length; ++i)
		{
			int randInRange = rand.nextInt(range);

			if (rand.nextFloat() < 0.5f)
			{
				retStr += (char) ('a' + randInRange);
			}
			else
			{
				retStr += (char) ('A' + randInRange);
			}
		}

		return retStr;
	}

	public static String convertInputStreamToString(InputStream inputStream) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	public static String shortenText(String str)
	{
		String text = "";
		boolean spaced = false;
		for (int i = 0; i < str.length(); ++i)
		{
			if (str.charAt(i) == ' ' || str.charAt(i) == '\n' || str.charAt(i) == '\t')
			{
				spaced = true;
			}
			else
			{
				if (spaced)
				{
					text += (str.charAt(i) + "").toUpperCase();
					spaced = false;
				}
				else
				{
					text += str.charAt(i);
				}
			}
		}
		return text;
	}

	public static String truncateString(String str, int length)
	{
		int minLength = Math.min(length, str.length());
		return str.substring(0, minLength);
	}
}
