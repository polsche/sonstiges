export class StundenEintrag {
    id: number;

    constructor(
        id,
        datum: Date,
        kommenZeit: Date,
        gehenZeit: Date,
        leistung: string,
        createdAt: Date,
        updatedAt: Date,
        benutzer: string)
        { this.id = id; }
        
}
