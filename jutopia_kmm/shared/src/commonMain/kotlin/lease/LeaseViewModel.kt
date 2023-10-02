package lease


import co.touchlab.kermit.Logger
import common.TmpUserInfo
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
private val log = Logger.withTag("LeaseAPI")

sealed class ApiResponse {
    object Success : ApiResponse()
    object InsufficientPoints : ApiResponse()
    object ServerError: ApiResponse()

}

class LeaseViewModel : ViewModel() {
    private val _selectedSeat = MutableStateFlow<Seat?>(null)

    val selectedSeat: StateFlow<Seat?> = _selectedSeat

    private val _seats = MutableStateFlow<List<Seat>>( listOf() )

    val seats: StateFlow<List<Seat>> = _seats

    var apiResponse = MutableStateFlow<ApiResponse?>(null)

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun toggleDialog(show: Boolean) {
        _showDialog.value = show
    }
    fun clearApiResponse() {
        apiResponse.value = null
    }

    private val apiService = LeaseApiService()
    init {
        viewModelScope.launch {
            try {
                val response = apiService.getAllSeats(TmpUserInfo.getSchool(), TmpUserInfo.getGrade(), TmpUserInfo.getClassNumber())
                val apiResponse = Json.decodeFromString<ListResponse>(response.bodyAsText())
                val seatList: List<Seat>? = apiResponse.body
                log.i{"${seatList}"}
                _seats.emit(seatList!!)
            } catch (e:Exception){
                log.i {"연결실패, ${e}"}
            }
        }
    }


    fun reserveSeat(id: String) {
        viewModelScope.launch {
            try {
                val response = apiService.setSeat(id)
                val apiResponse = Json.decodeFromString<Response>(response.bodyAsText())
                log.i { "${apiResponse.result}" }
                log.i { " 결과 코드 $apiResponse.result.resultCode" }
                when (apiResponse.result.resultCode) {

                    200 -> {
                        this@LeaseViewModel.apiResponse.emit(ApiResponse.Success)
                        val seat: Seat? = apiResponse.body
                        val updatedSeats = _seats.value.map {
                            if (it.id.equals(id)) it.copy(seatStatus = "INUSE")
                            else it
                        }
                        _seats.emit(updatedSeats)
                    }
                    1002 -> {
                        this@LeaseViewModel.apiResponse.emit(ApiResponse.InsufficientPoints)
                    }
                    else -> {
                        this@LeaseViewModel.apiResponse.emit(ApiResponse.ServerError)
                    }
                }

            } catch (e:Exception){
                log.i {"연결실패, ${e}"}
                this@LeaseViewModel.apiResponse.emit(ApiResponse.ServerError)
            }
        }
    }

    fun selectSeat(seat: Seat) {
        viewModelScope.launch {
            try {
                val response = apiService.getSeat(seat.id)
                val apiResponse = Json.decodeFromString<Response>(response.bodyAsText())
                val seat: Seat? = apiResponse.body
                log.i { "${seat}" }
            } catch (e:Exception){
                log.i {"연결실패, ${e}"}
            }
            _selectedSeat.emit(seat)
        }
    }

    fun clearSelectedSeat() {
        viewModelScope.launch {
            _selectedSeat.emit(null)
        }
    }
}