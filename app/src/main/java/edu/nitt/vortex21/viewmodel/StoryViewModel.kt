package edu.nitt.vortex21.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nitt.vortex21.helpers.Resource
import edu.nitt.vortex21.model.StoryResponse
import edu.nitt.vortex21.repository.StoryRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class StoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {
    val storyResponse = MutableLiveData<Resource<StoryResponse>>()

    fun fetchStoriesOfCategory(category: String) {
        viewModelScope.launch {
            storyResponse.postValue(Resource.Loading())
            try {
                val response = repository.fetchStoriesOfCategory(category)
                storyResponse.postValue(handleStoryResponse(response))
            } catch (e: Exception) {
                storyResponse.postValue(Resource.Error("No Internet"))
            }

        }
    }

    private fun handleStoryResponse(response: Response<StoryResponse>): Resource<StoryResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        val jsonObject = JSONObject(response.errorBody()!!.toString())
        val message = jsonObject.getString("message")
        return Resource.Error(message)
    }
}