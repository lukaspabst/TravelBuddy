
export const getMessageDetails = (type, data, t) => {
    const getRoleDescription = (role) => {
        switch (role) {
            case "Organizer":
                return t('tripRoles.organizer');
            case "Assistant":
                return t('tripRoles.assistant');
            case "Traveler":
                return t('tripRoles.traveler');
            default:
                return role;
        }
    };

    switch (type) {
        case "invite_trip":
            return {
                title: t('messageTypes.invite_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.invite_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        case "accept_trip":
            return {
                title: t('messageTypes.accept_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.accept_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        case "deny_trip":
            return {
                title: t('messageTypes.deny_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.deny_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        case "roleChange_trip":
            const roleDescription = getRoleDescription(data.roleIfRoleChange);
            return {
                title: t('messageTypes.roleChange_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip, newRole: roleDescription }),
                message: t('messageTypes.roleChange_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip, newRole: roleDescription }),
            };
        case "removeMember_trip":
            return {
                title: t('messageTypes.removeMember_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.removeMember_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        case "tripDeleted_trip":
            return {
                title: t('messageTypes.tripDeleted_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.tripDeleted_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        case "userLeft_trip":
            return {
                title: t('messageTypes.userLeft_trip.title', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
                message: t('messageTypes.userLeft_trip.message', { initiator: data.initiatorUsername, tripName: data.nameOfTrip }),
            };
        default:
            return {
                title: t('messageTypes.default.title'),
                message: t('messageTypes.default.message'),
            };
    }
};
