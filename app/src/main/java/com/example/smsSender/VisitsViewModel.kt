package com.example.smsSender

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ListenableWorker.Result.Success
import com.example.smsSender.room.SmsWithContact
import com.example.smsSender.room.scheduledSms.ScheduledSms
import com.example.smsSender.room.scheduledSms.ScheduledSmsDao
import com.example.smsSender.smsWorker.SmsScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisitsViewModel @Inject constructor(private val dao: ScheduledSmsDao) : ViewModel() {

    // teoretycznie fajnie by bylo to rozdzielic na repo jescze ale uznalem ze przy tak malej ilosci kodu nie jest to konieczne

    // bedzie tutaj znajdowac sie lista dat kiedy trzeba wyslac sms dla jednej visit
    private val _visitModel = MutableStateFlow(VisitModel())
    val visitModel = _visitModel.asStateFlow()

//    val listOfPlannedSms: StateFlow<List<ScheduledSms>> =
//        dao.getScheduleSmsOrderByDateOfCompletionAsc()
//            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // pewna lista danych dla wszystkich zaplanowanych sms-ów z wszystkimi danymi
    // potrzebna dla wyswietlenia
    val listOfPlannedSmsWithFullInfo: StateFlow<List<SmsWithContact>> =
        dao.getUpcomingSmsWithContact()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun saveVisit(context: Context, phoneNumber: String, model: VisitModel, message: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val batch = model.listOfSmsDates.map { date ->
                ScheduledSms(
                    idOfContact = model.contactId,
                    timeOfVisit = model.timeOfVisit ?: 0,
                    timeOfSms = date
                )
            }

            val ids = dao.insertAll(batch)

            val batchWithIds = batch.mapIndexed { i, sms ->
                sms.copy(id = ids[i].toInt())
            }

            val idMap = SmsScheduler.planSms(
                context = context,
                phoneNumber = phoneNumber,
                smsList = batchWithIds,
                message = message
            )

            idMap.forEach { (smsId, workId) ->
                dao.updateWorkId(smsId, workId)
            }

            clearAll()
        }
    }

    fun deleteWorkReqById(id: Int, ctx: Context) {
        viewModelScope.launch {
            dao.getWorkRequestId(id).let { workId ->
                SmsScheduler.cancelWork(workId, ctx)
            }
        }
    }

    fun deletePlannedSms(sms: ScheduledSms) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteFutureSms(sms)
        }
    }

    fun addContactId(id: Int) {
        _visitModel.update { currentState -> currentState.copy(contactId = id) }
    }

    fun addDateOfVisit(newDate: Long) {
        _visitModel.update { currentState -> currentState.copy(timeOfVisit = newDate) }
    }

    fun addSmsDateToList(date: Long) {
        _visitModel.update { currentState -> currentState.copy(listOfSmsDates = currentState.listOfSmsDates + date) }
    }

    fun removeSmsDateFromList(indexToRemove: Int) {
        _visitModel.update { currentState ->
            currentState.copy(listOfSmsDates = currentState.listOfSmsDates.filterIndexed { index, _ -> index != indexToRemove })
        }
    }

    fun updateSmsDate(indexToUpdate: Int, newValue: Long) {
        _visitModel.update { currentState ->
            currentState.copy(listOfSmsDates = currentState.listOfSmsDates.mapIndexed { index, l ->
                if (index == indexToUpdate) newValue else l
            })
        }
    }

    private fun clearAll() {
        _visitModel.value = VisitModel()
    }
}


