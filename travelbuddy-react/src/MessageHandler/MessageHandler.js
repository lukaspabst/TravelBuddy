export const getMessageDetails = (type, data) => {
    switch (type) {
        case "invite_trip":
            return {
                title: `${data.initiatorUsername} invited you to join the trip ${data.nameOfTrip}`,
                message: `You have been invited by ${data.initiatorUsername} to join the trip ${data.nameOfTrip}. Click to accept.`,
            };
        // Add more cases for other message types as needed
        default:
            return {
                title: "Default Title",
                message: "Default Message",
            };
    }
};
