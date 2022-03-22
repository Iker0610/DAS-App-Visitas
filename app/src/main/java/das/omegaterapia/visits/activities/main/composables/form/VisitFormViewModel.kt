package das.omegaterapia.visits.activities.main.composables.form

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.entities.Client
import das.omegaterapia.visits.model.entities.Direction
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.model.entities.VisitData
import das.omegaterapia.visits.utils.isNonEmptyText
import das.omegaterapia.visits.utils.isValidPhoneNumber
import das.omegaterapia.visits.utils.isZIP
import java.time.ZonedDateTime
import javax.inject.Inject


@HiltViewModel
class VisitFormViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val initialVisitCard: VisitCard? = savedStateHandle["initialVisitCard"]


    // Variables para guardar los datos
    var isVIP by mutableStateOf(initialVisitCard?.visitData?.isVIP ?: false)
    var visitDate: ZonedDateTime by mutableStateOf(initialVisitCard?.visitData?.visitDate ?: ZonedDateTime.now())

    var clientNameText by mutableStateOf(initialVisitCard?.client?.name ?: "")
    var clientSurnameText by mutableStateOf(initialVisitCard?.client?.surname ?: "")
    var clientCompanions = mutableStateListOf(*(initialVisitCard?.visitData?.companions?.toTypedArray() ?: arrayOf("")))

    var phoneText by mutableStateOf(initialVisitCard?.client?.phoneNum ?: "")
    var addressText by mutableStateOf(initialVisitCard?.client?.direction?.address ?: "")
    var townText by mutableStateOf(initialVisitCard?.client?.direction?.town ?: "")
    var zipCodeText by mutableStateOf(initialVisitCard?.client?.direction?.zip ?: "")

    var observationText by mutableStateOf(initialVisitCard?.visitData?.observations ?: "")


    // Variable para comprobación de datos
    val isNameValid by derivedStateOf { isNonEmptyText(clientNameText) }
    val isSurnameValid by derivedStateOf { isNonEmptyText(clientSurnameText) }

    val isPhoneValid by derivedStateOf { isValidPhoneNumber(phoneText) }
    val isAddressValid by derivedStateOf { addressText.isNotBlank() }
    val isTownValid by derivedStateOf { townText.isNotBlank() }
    val isZIPValid by derivedStateOf { isZIP(zipCodeText) }

    val areAllValid by derivedStateOf { isNameValid && isSurnameValid && isAddressValid && isTownValid && isZIPValid && isPhoneValid }


    fun generateVisitCard(): VisitCard {
        val clientData = Client(
            name = clientNameText,
            surname = clientSurnameText,
            phoneNum = phoneText,
            direction = Direction(addressText, townText, zipCodeText)
        )

        val visitData: VisitData

        if (initialVisitCard != null) {
            visitData = initialVisitCard.visitData.copy(
                mainClientPhone = clientData.phoneNum,
                companions = clientCompanions.filter { it.isNotBlank() },
                visitDate = visitDate,
                isVIP = isVIP,
                observations = observationText
            )
        } else {
            visitData = VisitData(
                mainClientPhone = clientData.phoneNum,
                companions = clientCompanions.filter { it.isNotBlank() },
                visitDate = visitDate,
                isVIP = isVIP,
                observations = observationText
            )
        }

        return VisitCard(visitData, clientData)
    }
}