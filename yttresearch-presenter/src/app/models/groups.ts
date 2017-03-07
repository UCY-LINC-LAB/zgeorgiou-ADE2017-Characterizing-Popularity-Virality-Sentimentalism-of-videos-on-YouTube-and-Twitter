import {Group} from './group';
export class Groups {

    constructor(
        private popular : Group,
        private viral : Group ,
        private recent : Group ,
        private random : Group ,
        private popular_viral  : Group ,
        private popular_not_viral : Group ,
        private viral_not_popular: Group
    ){}


}
