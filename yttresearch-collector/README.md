# Collector
A tool that collects videos and tweets mentioning those videos over a period of two weeks.

## Core Modules
### Streaming Service
Streaming service makes use of the Twitter’s public stream endpoint [#]. 
In order to use this stream a user needs to create a Twitter Application to generate the 4 keys needed for
the authentication. 
When the user connects to this endpoint, he obtains a sample of all the
public tweets on real time. 
However, we are interested only for the tweets that contain a link
to a YouTube video. This is achieved by filtering the tweets using the regular expression
shown in the list below. 
This regular expression accepts only the links to youtube.com and
youtu.be hosts. 
```bash
# Regular Expression for YouTube Links
( h t t p s ? : \ / \ / y o u t u \ . be ) | ( h t t p s ? : \ / \ / www \ . y o u t u b e \ . com \ / w a t c h \ ? v = )
```
### Dynamic Information Service
This service provides all the functionality needed to collect the
statistics of a video once per
day from YouTube. 
Running on its own thread, it checks the database every 
10 minutes for
videos that need update. A video needs an update 
when 24 hours passed since its last update.
On every update we insert to the database the 
dynamic information of the video.
It is critical for our purposes to retrieve the dynamic 
information every 24 hours.
However,
there are cases that the process of dynamic information 
retrieval will fail because the video is
removed by the user or due to network errors. 
In any case, this service will mark that video
as incomplete and another service will remove it from the database along with its tweets and
comments. Furthermore, this service is also responsible to identify if a video has reached its
16 th day, to set its state as completed.

### Comments Service
This service fetches the most 100 relevant comments for
 each video and it is executed once
the video is added to the database. These are the comments that are
 shown more frequently to
the user as it is the default option by YouTube. These comments are
 calculated primarily by
Google+ quality factors, such as the user’s YouTube 
channel age and the number of comments
the user posted on his Google+ profile, especially the ones 
containing link to a YouTube
video. Also, these are the comments that contain quality discussions and are
 published by popular personalities.
 
 ## Technologies Used
 ### Twitter4j
 Twitter4j is a Java library for the Twitter API. It provides an easy way to authenticate with
 Twitter and use its services to connect with the streaming API.
 ### MongoDB
 MongoDB, is a free and open-source cross-platform document-oriented database
 program. Classified as a NoSQL database program, MongoDB uses JSON-like doc-
 uments with schemas. It supports map-reduce operations, widely used for processing
 large volumes of data.
 ### Gson
 Originally created by Google, Gson is a Java library that can be used to convert Java
 Objects into their JSON representation. Currently Gson is released under Apache
 Licence 2.0.
 ### Spark-Web Framework
 Spark, is a micro framework for creating web applications in Java 8. Spark is
 mainly used for creating REST API’s, but it also supports a multitude of template
 engines. Under the hood, Spark, runs an embedded Jetty server.
 ### VADER
 VADER stands for Valence Aware Dictionary and sEntiment Reasoner. It is a lex-
 icon and rule-based sentiment analysis tool that is specifically attuned to sentiments
 expressed in social media, and works well on texts from other domains.
