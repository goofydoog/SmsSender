package com.example.smsSender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smsSender.room.contact.Contact
import com.example.smsSender.room.contact.ContactDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor (private val dao: ContactDao) : ViewModel() {

    private val _contactDetails = MutableStateFlow(ContactModel())
    val contactDetails = _contactDetails.asStateFlow()

    val contacts: StateFlow<List<Contact>> = dao.getContactsOrderByFirstName()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addContactToBd(contact: Contact, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.insertContact(contact)
            onSuccess()
        }
    }

    fun deleteContact(contact: Contact, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.deleteContact(contact)
            onSuccess()
        }
    }

    fun updateContact(contact: Contact, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dao.updateContact(contact)
            onSuccess()
        }
    }


    fun editPhoneNumber(newNumber: String) {
        _contactDetails.value = _contactDetails.value.copy(
            phoneNumber = newNumber
        )
    }

    fun transferData(contact: Contact) {
        _contactDetails.value = ContactModel(
            id = contact.id,
            firstName = contact.firstName,
            lastName = contact.lastName,
            phoneNumber = contact.phoneNumber
        )
    }

    fun editName(firstName: String) {
        _contactDetails.value = _contactDetails.value.copy(
            firstName = firstName
        )
    }

    fun editLastName(lastName: String) {
        _contactDetails.value = _contactDetails.value.copy(
            lastName = lastName
        )
    }

    fun clearData() {
        _contactDetails.value = ContactModel()
    }

}