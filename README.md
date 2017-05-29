# Yttresearch
This project contains all the tools developed for the purposes of my bachelor thesis on
_Characterizing the Popularity, Virality and Sentimentalism of Video Content Categories on YouTube and Twitter_



* Collect YouTube & Twitter data
* Analyze the data

Analysis on how YouTube videos disseminate through social media

This project is divided into 2 main/core services:

1. **Collector**
2. **Analyzer**

## 1. Video & Tweets Collector

### Features
TPS: Twitter Public Stream
* Finds new videos from TPS and stores their static data into db.
* Collects tweets that mention YouTube videos through TPS.
* Collects the dynamic information of YouTube videos once per day. Currently is cummulative
* Collects video comments.
* Removes incomplete Videos video comments.
* All data is stored into the following collections:  
  * Videos
  * Tweets
  * Comments
  * YouTubeKeys
  * TwitterApps
  * configurations
* Commands are handled via Rest API - Authentication Token Needed


## 2. Analyzer

### Features
* Sentiment Analysis
* Statistical Analysis
* Videos Information
* Groups Information
* Classification
* Feature Importance Analysis

