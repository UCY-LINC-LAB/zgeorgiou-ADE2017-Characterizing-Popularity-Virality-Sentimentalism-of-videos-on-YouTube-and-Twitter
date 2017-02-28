# Yttresearch Analysis

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

### TODO
1. Authorized all requests using a token

### What is collected?
Video:
1. video_id
2. title
3.

## 2. Video & Tweets Analysis

The purpose of service is to provide different methods/endpoints to extract knowledge from the raw data collected in collector service.

The base object will be VideoRecord. VideoRecord will contain all the data needed to perform analysis

What needs to implement:
  * Map mongo data into VideoRecord Java Object

## 3. Video & Tweets Presenter

The purpose of this service is to provide a nice Graphical environment to present the extracted knowledge.
