package com.isdp.twitterposterandroid;

import java.util.StringTokenizer;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.util.Log;

public class TwitterManager
{
	private static TwitterManager instance;

	private TwitterManager()
	{
	}

	public static TwitterManager getInstance()
	{
		if (instance == null)
			instance = new TwitterManager();

		return instance;
	}

	/*
	 * Get Twitter OAuth keys here:
	 * https://apps.twitter.com/
	 *
	 */

	private static final String OAUTH_CONSUMER_KEY = "<YOUR CONSUMER KEY HERE>";
	private static final String OAUTH_CONSUMER_SECRET = "<YOUR CONSUMER SECRET HERE>";
	private static final String OAUTH_ACCESS_TOKEN = "<YOUR ACCESS TOKEN HERE>";
	private static final String OAUTH_ACCESS_TOKEN_SECRET = "<YOUR ACCESS TOKEN SECRET HERE>";
	private static final long FOLLOW_ID = 0L; //TWITTER ID OF ACCOUNT TO LISTEN TO (numeric, not hastag)

	public static final int TWITTER_CHARACTER_LIMIT = 140;

	private Configuration configuration;

	public void initTwitter()
	{
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
				.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET).setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
		configuration = configurationBuilder.build();

	}

	public void tweet(String message)
	{
		TwitterFactory tf = new TwitterFactory(configuration);
		Twitter twitter = tf.getInstance();
		try
		{
			Status status = twitter.updateStatus(message);
			Log.d("Tweet status", "Successfully updated the status to [" + status.getText() + "].");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void startTwitterStream()
	{
		TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();

		StatusListener listener = new StatusListener()
		{
			@Override
			public void onStatus(Status status)
			{
				try
				{
					Log.d("Text recieved", "@" + status.getUser().getScreenName() + " - " + status.getText() + "\n");

					StringTokenizer st = new StringTokenizer(status.getText(), " ");

					//first token indicates search engine
					String searchEngine = st.nextToken();

					//burn the next random token
					String randToken = st.nextToken();

					//next token indicates max number of results to return
					int maxResults = Integer.parseInt(st.nextToken());

					String searchString = "";
					while (st.hasMoreTokens())
					{
						searchString += st.nextToken() + " ";
					}

					if (searchEngine.equals(GoogleManager.TAG_YELP))
					{
						String[] results = GoogleManager.getInstance().trySearch(searchString,
								GoogleManager.SEARCH_YELP);

						if (results != null)
						{
							for (int i = 0; i < results.length && i < maxResults; ++i)
							{
								String tweetMsg = Util.truncateString(results[i], TWITTER_CHARACTER_LIMIT);

								Log.d("Tweeting", tweetMsg);

								tweet(tweetMsg);
							}
						}
						else if (results == null || results.length == 0)
						{
							tweet(Util.generateRandomString(7) + "\n" + "No results found!");
						}
					}
					else if (searchEngine.equals(GoogleManager.TAG_WIKI))
					{
						String[] results = GoogleManager.getInstance().trySearch(searchString,
								GoogleManager.SEARCH_WIKI);

						if (results != null)
						{
							for (int i = 0; i < results.length && i < maxResults; ++i)
							{
								String tweetMsg = Util.generateRandomString(3) + "\n" + Util.shortenText(results[i]);
								tweetMsg = Util.truncateString(tweetMsg, TWITTER_CHARACTER_LIMIT);

								Log.d("Tweeting", tweetMsg);

								tweet(tweetMsg);
							}
						}
						else if (results == null || results.length == 0)
						{
							tweet(Util.generateRandomString(7) + "\n" + "No results found!");
						}
					}
				}
				catch (Exception e)
				{
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
			{
				Log.d("onDeletionNotice", "Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses)
			{
				Log.d("onTrackLimitationNotice", "Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId)
			{
				Log.d("onScrubGeo", "Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning)
			{
				Log.d("onStallWarning", "Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex)
			{
				Log.d("onException", ex.toString());
			}
		};

		twitterStream.addListener(listener);

		FilterQuery tweetFilterQuery = new FilterQuery();

		tweetFilterQuery.follow(new long[]
		{ FOLLOW_ID });

		twitterStream.filter(tweetFilterQuery);
	}

}
