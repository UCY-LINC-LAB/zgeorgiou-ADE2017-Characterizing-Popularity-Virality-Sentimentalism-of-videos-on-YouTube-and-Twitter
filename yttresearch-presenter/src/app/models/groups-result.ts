import {Groups} from './groups';
export class GroupsResult {

    constructor(
        private category_name : string,
        private percentage : number,
        private lbl_wnd : number,
        private groups: Groups
    ){}


}
