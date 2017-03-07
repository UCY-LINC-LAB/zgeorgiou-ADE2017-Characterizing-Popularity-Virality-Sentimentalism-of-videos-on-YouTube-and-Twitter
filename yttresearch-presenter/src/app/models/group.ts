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
        public days: Array<GroupDay>
    ){}



}
