TwitterPosterAndroid
======================
- Author: Evan Kale
- Email: evankale91@gmail.com
- Web: ISeeDeadPixel.com
- Blog: evankale.blogspot.ca

Summary:
- The end goal of this project is to get google search results via SMS.
- This app acts as a relay server that listens to a twitter account for
  a tweet (that is SMS'ed from a mobile phone); interprets it as a search
  query; sends it to a google custom search; then tweets out to a twitter
  account that is followed by the twitter account linked to the mobile phone.
  
Setup requirements:
- Google API key (fill this in GoogleManager.java)
- Google custom search engine ID (fill this in GoogleManager.java)
- Twitter OAuth keys (fill this in TwitterManager.java)
