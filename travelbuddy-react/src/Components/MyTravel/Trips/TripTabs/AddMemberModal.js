import React, { useState } from 'react';
import Modal from 'react-modal';
import { useTranslation } from 'react-i18next'; // Import translation hook
import './Modal.scss';
import {Button} from "../../../../Containers/Button/Button";
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import axios from "axios";
function AddMemberModal({ isOpen, closeModal }) {
    const { t } = useTranslation(); // Translation hook

    const [username, setUsername] = useState('');
    const [role, setRole] = useState('');

    function addMember() {
        // Assuming you have an API endpoint for adding a member
        // You can replace 'YOUR_API_ENDPOINT' with the actual endpoint
        axios.post('YOUR_API_ENDPOINT', { username, role })
            .then(response => {
                console.log('Member added successfully');
                setUsername('');
                setRole('');
                closeModal();
            })
            .catch(error => console.error('Error adding member:', error));
    }

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={closeModal}
            className="addMember-modal" // Apply a custom class for styling
            overlayClassName="addMember-overlay" // Apply a custom class for the overlay
        >
            <div className="modal-header">
                <h2>{t('tripMembersGrid.addMember')}</h2>
                <div className="close-icon-container">
                    <FontAwesomeIcon icon={faTimes} onClick={closeModal} className="close-icon"/>
                </div>
            </div>
            <form>
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}
                       placeholder={t('tripMembersGrid.usernamePlaceholder')}/>
                <select className="select-trip-member-role" value={role} onChange={(e) => setRole(e.target.value)}>
                    <option value="">{t('tripMembersGrid.selectRole')}</option>
                    <option value="member">{t('tripMembersGrid.memberRole')}</option>
                    <option value="trip-assistant">{t('tripMembersGrid.tripAssistantRole')}</option>
                </select>
                <Button buttonStyle="btn--outline" onClick={addMember}>{t('tripMembersGrid.addMember')}</Button>
            </form>
        </Modal>
    );
}

export default AddMemberModal;
