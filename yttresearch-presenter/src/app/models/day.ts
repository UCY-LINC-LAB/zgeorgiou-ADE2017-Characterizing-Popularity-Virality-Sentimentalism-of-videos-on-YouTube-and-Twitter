import {Stat} from './stat';
export class Day {

    constructor(
        private views_added:Stat,
        private tweets_added:Stat,
        private retweets_added:Stat,
        private ratio_original_tweets_total_tweets:Stat,
        private likes_added:Stat,
        private dislikes_added:Stat,
        private ratio_likes:Stat,
        private average_user_followers:Stat,
        private average_user_friends:Stat,
        private average_user_days_created_before_video:Stat,
    ){}


    public toString = (): String => {
        return {"Day":this.views_added}.toString();
    }
}
