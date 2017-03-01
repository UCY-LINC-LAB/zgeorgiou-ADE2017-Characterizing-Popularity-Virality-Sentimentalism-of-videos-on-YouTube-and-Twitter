# Yttresearch

**Deadline:** End of March

## Tasks:
* Collect YouTube & Twitter data
* Analyze the data

Analysis on how YouTube videos disseminate through social media

This project is divided into 3 main/core services:

1. **Collector**
2. **Analyzer**
3. **Presenter**

## 1. Video & Tweets Collector

TPS: Twitter Public Stream
* Finds new videos from TPS and stores their static data into db.
* Collects tweets that mention YouTube videos through TPS.
* Collects the dynamic information of YouTube videos once per day. Currently is cummulative
* Collects video comments.
* All data is stored into the following collections:  
  * Videos
  * Tweets
  * Comments
  * YouTubeKeys
  * TwitterApps
  * configurations
* Commands are handled via Rest API

### TODO's
1. [x] Authorize all requests using a token
2. [x] Select the youtube key with the least cost.
3. [x] Decide what to do with the comments
4. [x] Deleting incomplete videos
5. [x] If more than 2 days passed since the last update mark the video as incomplete
6. [ ] Provide more statistics for monitoring

## 2. Video & Tweets Analysis

The purpose of service is to provide different methods/endpoints to extract knowledge from the raw data collected in collector service.

The base object will be VideoRecord. VideoRecord will contain all the data needed to perform analysis

What needs to implement:
  * Map mongo data into VideoRecord Java Object

## 3. Video & Tweets Presenter

The purpose of this service is to provide a nice Graphical environment to present the extracted knowledge.
