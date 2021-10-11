package geekbarains.material.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbarains.material.BuildConfig
import geekbarains.material.model.PODRetrofitImpl
import geekbarains.material.model.PODServerResponseData
import geekbarains.material.ui.entities.PictureOfTheDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrevPictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getData(date : String): LiveData<PictureOfTheDayData> {
        sendServerRequest(mapDate(date))
        return liveDataForViewToObserve
    }

    /**
     * Преобразует строку даты [date] из формата DD.MM.YYYY в формат YYYY-MM-DD
     * @return Строка даты в формате YYYY-MM-DD
     */

    private fun mapDate(date : String) : String {
        val str = date.split(".").toTypedArray()
        return "${str[2]}-${str[1]}-${str[0]}"
    }

    private fun sendServerRequest(date : String) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)

        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDayFromDate(apiKey,date).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value = PictureOfTheDayData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value = PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }
}