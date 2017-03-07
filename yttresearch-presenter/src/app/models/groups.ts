import {Group} from './group';
export class Groups {

    constructor(
        public popular : Group,
        public viral : Group ,
        public recent : Group ,
        public random : Group ,
        public popular_viral  : Group ,
        public popular_not_viral : Group ,
        public viral_not_popular: Group
    ){}


}
