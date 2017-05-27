## Collector

The main functionality of this RESTful web service is to find YouTube
videos, collect their information and monitor their activity 
on Twitter. 

It uses the Twitter public stream to _catch_ tweets 
containing URLs to YouTube videos.

The information collected is described in section: [[collections]] 

The interaction with the program is achieved using HTTP requests.
For safety reasons, a token is needed to perform all the requests.
For access, please contact zgeorg03@cs.ucy.ac.cy

For more information check [usage](use guide) page.

### Features
* Monitor a specified number of videos
* It is written to be stateless and scalable.

![Collector Design](https://github.com/zgeorg03/yttresearch-service/blob/master/video-tweets-collector/img/video-tweets-collector-design.png)