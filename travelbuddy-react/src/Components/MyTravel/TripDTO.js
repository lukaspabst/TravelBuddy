export class TripDTO {
    constructor(id, nameTrip, maxPersons, startDate, endDate,costs,destination) {
        this.id = id;
        this.nameTrip = nameTrip;
        this.maxPersons = maxPersons;
        this.startDate = startDate;
        this.endDate = endDate;
        this.costs = costs;
        this.destination = destination;
    }
}
