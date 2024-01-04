import React, {useState} from 'react';
import ReactModal from 'react-modal';
import './GeneralModals.scss'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {useTranslation} from "react-i18next";
import {getMessageDetails} from "../../../MessageHandler/MessageHandler";

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

    const MessageComponent = ({ message }) => {
        const { title, message: messageContent } = getMessageDetails(message.type, message);

        return (
            <div className={`message-item ${expandedMessages.includes(message.id) ? 'expanded' : ''}`} key={message.id}>
                <div className="message-title" onClick={() => handleTitleClick(message)}>
                    {title}
                </div>
                {expandedMessages.includes(message.id) && (
                    <div className="message-content">
                        <p>{messageContent}</p>
                    </div>
                )}
            </div>
        );
    };
//TODO OPEN UND CLOSE TRIPS NUR USER MIT STATUS ACTIVE; MODAL FUNKTIONEN: DELETE,ACCEPT,DENIE the MESSAGE // NEW MEESAGE SEND TO INITIATOR IF INVITED USER REACTS TO MESSAGE, TRANSLATE FUNKTION FÜR ALLES; RELOAD BEI CLOSE DES MODALS -> ZUM CHECK OB NEUE NACHRICHTEN

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
