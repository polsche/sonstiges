export class StundenEintrag {
    id: number;

    constructor(
        id,
        datum: string,
        kommenZeit: Date,
        gehenZeit: Date,
        leistung: string,
        createdAt: Date,
        updatedAt: Date,
        benutzer: string)
        {
            this.id = id;
        }
        
}
