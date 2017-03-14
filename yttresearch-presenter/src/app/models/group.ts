import {GroupDay} from './group-day';
import {Stat} from './stat';
export class Group {

    constructor(
        private size : number,
        private duration : Stat ,
        private total_views : Stat ,
        private total_tweets : Stat ,
        private total_likes : Stat ,
        public total_dislikes : Stat ,
        public negative_sentiment : Stat ,
        public positive_sentiment : Stat ,
        public neutral_sentiment : Stat ,
        public compound_sentiment : Stat ,
        public days: Array<GroupDay>
    ){}



}
