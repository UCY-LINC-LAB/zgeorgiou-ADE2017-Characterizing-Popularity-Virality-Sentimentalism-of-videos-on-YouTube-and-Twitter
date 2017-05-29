import {Stat} from './stat';
export class GroupDay {

    constructor(
        public day:number,
        public views_added:Stat,
        public tweets_added:Stat,
        public retweets_added:Stat,
        public ratio_original_tweets_total_tweets:Stat,
        public likes_added:Stat,
        public dislikes_added:Stat,
        public ratio_likes:Stat,
        public average_user_followers:Stat,
        public average_user_friends:Stat,
        public average_user_days_created_before_video:Stat,
        public tweets_sentiment_neu:Stat,
        public tweets_sentiment_neg:Stat,
        public tweets_sentiment_pos:Stat,
        public tweets_sentiment_compound:Stat,
    ){}

}
