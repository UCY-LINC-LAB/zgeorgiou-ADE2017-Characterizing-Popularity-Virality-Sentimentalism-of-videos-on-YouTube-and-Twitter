# Analyzer
Analyzer is a tool that performs a multitude number of operations over the data collected by
the Collector tool. It is accessible via its restful API. Firstly, it processes videos,tweets and
comments to construct our data set. It also groups together the videos, produces graphs and
statistics, and extract the features used in the classification and feature importance analysis.
For processing, this tool utilizes the aggregation framework provided by MongoDB. We use
a processing pipeline, to filter and tranform our data, according to our needs.

## Core Operations
### Completed Videos Monitoring
This service constantly checks the database for completed videos. These are the
videos that have reached their 16th day. Once this service finds completed videos, it
sends the batch of videos to another service for processing, that uses the aggregation
framework provided by MongoDB in order to create our models.
### DB Communication Service
This service, acts as a middleware between the database and the main program. It
provides an abstraction layer for the data retrieval, offering various queries, needed
for the analysis. These queries are builded as aggregation functions. MongoDB,
provides map-reduce operations to perform aggregation. In general, map-reduce
operations have two phases: a map stage that processes each document and emits
one or more objects for each input document and a reduce phase that combines the
output of the map operation.
### Sentiment Analysis Service
This service, extracts the sentiment of the comments of each video. It uses the
VADER tool, described earlier, to compute the average negative,neutral and positive
sentiment found in the comments of each video.
### Classification & Feature Importance Analysis
This service trains and evaluates two binary classifiers using Gradient Boosting De-
cision tree and produces the Precision-Recall graphs, F1 scores and the contribution
of each feature. The classifier is implemented in python, using sci-kit package.
### Plots Service
This service creates the desired plots. It uses matplotlib, a package for python, to
create customizable 2D plots for the purposes of our research.
### Statistical Analysis Service
This service offers various functions to perform statistic operations, such as comput-
ing the average, median and standard deviation in a list of numbers. It is used for the
construction of our models.


