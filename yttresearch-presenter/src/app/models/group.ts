import {Days} from './days';
import {Stat} from './stat';
export class Group {

    constructor(
        private size : number,
        private duration : Stat ,
        private total_views : Stat ,
        private total_tweets : Stat ,
        private total_likes : Stat ,
        private total_dislikes : Stat ,
        private days: Days
    ){}


}
