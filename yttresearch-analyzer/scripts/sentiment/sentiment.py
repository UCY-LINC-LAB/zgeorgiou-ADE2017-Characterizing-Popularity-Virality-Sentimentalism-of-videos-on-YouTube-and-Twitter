#!/usr/bin/python
import sys
import numpy
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

'''
	This python script read from standard input and performs
	sentiment analysis. On EOF, it computes the average,median and
	std of neg,pos,neu,compound
'''
neg_list = []
pos_list = []
neu_list = []
compound_list = []

def read(analyzer):
	for line in sys.stdin:
		vs = analyzer.polarity_scores(line)
		neg_list.append(vs['neg'])
		pos_list.append(vs['pos'])
		neu_list.append(vs['neu'])
		compound_list.append(vs['compound'])
	print ('average_neg={0:.7f}'.format(numpy.average(neg_list)))
	print ('median_neg={0:.7f}'.format(numpy.median(neg_list)))
	print ('std_neg={0:.7f}'.format(numpy.std(neg_list)))
	print ('average_pos={0:.7f}'.format(numpy.average(pos_list)))
	print ('median_pos={0:.7f}'.format(numpy.median(pos_list)))
	print ('std_pos={0:.7f}'.format(numpy.std(pos_list)))
	print ('average_neu={0:.7f}'.format(numpy.average(neu_list)))
	print ('median_neu={0:.7f}'.format(numpy.median(neu_list)))
	print ('std_neu={0:.7f}'.format(numpy.std(neu_list)))
	print ('average_compound={0:.7f}'.format(numpy.average(compound_list)))
	print ('median_compound={0:.7f}'.format(numpy.median(compound_list)))
	print ('std_compound={0:.7f}'.format(numpy.std(compound_list)))


if __name__ == '__main__':
	analyzer = SentimentIntensityAnalyzer()
	read(analyzer)
	

