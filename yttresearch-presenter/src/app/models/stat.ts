export class Stat {

    constructor(
        private average:number,
        private median:number,
        private std:number
    ){}


    public toString = (): String => {
        return {"average" : String(this.average)}.toString();
    }
}
