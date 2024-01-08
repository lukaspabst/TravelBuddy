import React, {useState} from 'react';
import ReactModal from 'react-modal';
import './GeneralModals.scss'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTimes, faTrash} from "@fortawesome/free-solid-svg-icons";
import {useTranslation} from "react-i18next";
import {getMessageDetails} from "../../../MessageHandler/MessageHandler";
import {Button} from "../../../Containers/Button/Button";
import {API_BASE_URL} from "../../../config";
import axios from "axios";

const InboxModal = ({ trackedMessages, isOpen, closeModal }) => {

    const { t } = useTranslation();
    const [expandedMessages, setExpandedMessages] = useState([]);
    const handleTitleClick = (message) => {
        setExpandedMessages((prevExpanded) => {
            if (prevExpanded.includes(message.id)) {
                return prevExpanded.filter((id) => id !== message.id);
            } else {
                return [...prevExpanded, message.id];
            }
        });
    };
    const acceptInvitation = (message) => {
        const updateStatusEndpoint = `${API_BASE_URL}/api/trips/${message.tripId}/updateMemberStatus`;
        const username = message.username;

        axios.patch(updateStatusEndpoint, { username, status: 'Active' }, {withCredentials:true})
            .then(response => {
                console.log(`Accepted invitation for message ID: ${message.id}`);
                saveMessage(message,"accept_trip");
                deleteMessage(message);
            })
            .catch(error => {
                console.error('Error accepting invitation:', error);
            });
    };

    const denyInvitation = (message) => {
        const removeFromTripEndpoint = `${API_BASE_URL}/api/trips/removeFromTrip/${message.tripId}`;

        axios.patch(removeFromTripEndpoint, {},{withCredentials:true})
            .then(response => {
                console.log(`Denied invitation for message ID: ${message.id}`);
                saveMessage(message,"deny_trip");
                deleteMessage(message);
            })
            .catch(error => {
                console.error('Error accepting invitation:', error);
            });
    };
    const saveMessage = (message,tripType) => {
        const saveMessageEndpoint = `${API_BASE_URL}/api/messages/save`;

        axios.post(saveMessageEndpoint, {
            type: tripType,
            tripId: message.tripId,
            username: message.initiatorUsername,
        },{withCredentials:true})
            .then(response => {
                console.log('Message saved successfully:', response.data);
            })
            .catch(error => {
                console.error('Error saving message:', error);
            });
    };
    const deleteMessage = (message) => {
        const deleteMessageEndpoint = `${API_BASE_URL}/api/messages/delete/${message.id}`;

        axios.delete(deleteMessageEndpoint,{withCredentials:true})
            .then(response => {
                console.log('Message deleted successfully:', response.data);
                closeModal();
                window.location.reload();
            })
            .catch(error => {
                console.error('Error deleting message:', error);
            });
    };

    const MessageComponent = ({ message }) => {
        const { title, message: messageContent } = getMessageDetails(message.type, message, t);

        return (
            <div className={`message-item ${expandedMessages.includes(message.id) ? 'expanded' : ''}`} key={message.id}>
                <div className="message-title" onClick={() => handleTitleClick(message)}>
                    {title}
                    {message.type !== 'invite_trip' && !expandedMessages.includes(message.id) && (
                        <div className="delete-icon">
                            <FontAwesomeIcon icon={faTrash} onClick={() => deleteMessage(message)}/>
                            <div className="deleteIcon-tooltip">
                                {t('general.deleteMessage')}
                            </div>
                        </div>
                    )}
                </div>
                {expandedMessages.includes(message.id) && (
                    <div className="message-content">
                        <p>{messageContent}</p>
                        {message.type === 'invite_trip' && (
                            <div className="invitation-buttons">
                                <Button buttonStyle="btn--small-avatar" onClick={() => acceptInvitation(message)}>{t('general.acceptTrip')}</Button>
                                <Button buttonStyle="btn--small-avatar" onClick={() => denyInvitation(message)}>{t('general.denyTrip')}</Button>
                            </div>
                        )}
                        {message.type !== 'invite_trip' && (
                            <div className="delete-button">
                                <Button buttonStyle="btn--small-delete" onClick={() => deleteMessage(message)}>{t('general.deleteMessage')}</Button>
                            </div>
                        )}
                    </div>
                )}
            </div>
        );
    };

    return (
        <ReactModal
            ariaHideApp={false}
            isOpen={isOpen}
            className="userInbox-modal"
            overlayClassName="userInbox-overlay"
        >
            <div className="modal-header">
                <h2>{t('general.userMessageInbox')}</h2>
                <div className="close-icon-container">
                    <FontAwesomeIcon icon={faTimes} onClick={closeModal} className="close-icon" />
                </div>
            </div>
            <div className="messages-inbox-container">
                {trackedMessages.length === 0 ? (
                    <p>{t('general.emptyInbox')}</p>
                ) : (
                    trackedMessages.map((message) => <MessageComponent key={message.id} message={message} />)
                )}
            </div>
        </ReactModal>
    );
};

export default InboxModal;
