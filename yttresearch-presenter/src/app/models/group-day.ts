import {Stat} from './stat';
export class GroupDay {

    constructor(
        public day:number,
        private views_added:Stat,
        private tweets_added:Stat,
        private retweets_added:Stat,
        private ratio_original_tweets_total_tweets:Stat,
        public likes_added:Stat,
        private dislikes_added:Stat,
        private ratio_likes:Stat,
        private average_user_followers:Stat,
        private average_user_friends:Stat,
        private average_user_days_created_before_video:Stat,
        private tweets_sentiment_neu:Stat,
        private tweets_sentiment_neg:Stat,
        private tweets_sentiment_pos:Stat,
        private tweets_sentiment_compound:Stat,
    ){}

}
