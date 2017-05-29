import {Groups} from './groups';
export class GroupsResult {

    constructor(
        public category_name : string,
        public category : number,
        public percentage : number,
        public lbl_wnd : number,
        public groups: Groups
    ){}


}
