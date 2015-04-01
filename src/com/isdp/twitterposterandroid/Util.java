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
