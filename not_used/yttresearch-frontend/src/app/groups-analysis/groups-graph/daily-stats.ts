import {Graph} from '../../utils/graph-interface';
export class DailyStats implements Graph {
    private options = {};

    private avg_tweets = [];
    private median_tweets = [];
    private tweets_error = [];

    private avg_views = [];
    private median_views = [];
    private views_error = [];

    constructor(total_videos:number,daily:any){
        let title = { text : `Stats for ${total_videos} videos` };
        let yAxis = { text : 'Value' };
        let xAxis = { text : 'Days' };
        let exporting =  { enabled: true };

        for(let i=0;i<daily[0].day;i++){
            this.views_error.push([null,null]);
            this.tweets_error.push([null,null]);
        }
        
        for( let obj of daily){
            let day = obj.day

            this.avg_tweets.push([day,obj.avg_tweets]);
            this.median_tweets.push([day,obj.tweets_median]);

            let halfStdTweets = obj.avg_tweets_std /2;
            this.tweets_error.push([obj.avg_tweets-halfStdTweets,obj.avg_tweets+halfStdTweets]);

            this.avg_views.push([day,obj.avg_views]);
            this.median_views.push([day,obj.views_median]);

            let halfStdViews = obj.avg_views_std /2;
            this.views_error.push([obj.avg_views-halfStdViews,obj.avg_views+halfStdViews]);
        }


        let series = [ 
            {
                name: 'Average Tweets',
                type: 'spline',
                data: this.avg_tweets,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name}</span>: <b>{point.y:.1f}</b> ' },
                visible : false
            }, {
                name : 'Tweets Error',
                type : 'errorbar',
                data : this.tweets_error,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} range</span>: <b>{point.low:.1f}-{point.high:.1f}</b> ' }
            },
             {
                name: 'Tweets Median',
                data: this.median_tweets,
                type: 'spline',
                visible:false,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name}</span>: <b>{point.y:.1f}</b> ' }
            }, {
                name: 'Average Views',
                type: 'spline',
                data: this.avg_views,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name}</span>: <b>{point.y:.1f}</b> ' }
            },{
                 name: 'Views Error',
                 type: 'errorbar',
                 data: this.views_error,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name} range</span>: <b>{point.low:.1f}-{point.high:.1f}</b> ' }
            }, {
                name: 'Views Median',
                type: 'spline',
                data: this.median_views,
                tooltip: { pointFormat: '<span style="font-weight: bold; color: {series.color}">{series.name}</span>: <b>{point.y:.1f}</b> ' }
            }, 
        ];
        this.options['title'] =title;
        this.options['yAxis'] =yAxis;
        this.options['xAxis'] =xAxis;
        this.options['series'] =series;
        this.options['exporting'] =exporting;
    }

    getGraph(){
        return this.options;
    }

}