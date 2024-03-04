package com.clean.mvvmLgb.domain.usecase.catDetails

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.data.models.catDetails.PostFavCatModel
import com.clean.mvvmLgb.domain.mappers.CallSuccessModel
import com.clean.mvvmLgb.domain.repositories.CatDetailsRepository
import com.clean.mvvmLgb.domain.usecase.catsDetail.PostFavCatUseCase
import com.clean.mvvmLgb.domain.usecase.catsDetail.PostFavCatUseCaseImpl
import com.clean.mvvmLgb.models.catMocks.MockPostFavCatModel
import com.clean.mvvmLgb.models.catMocks.MockSuccessResponse
import com.clean.mvvmLgb.models.catMocks.toRequestPostFavCatData
import com.clean.mvvmLgb.models.catMocks.toResponsePostSuccess
import com.clean.mvvmLgb.utils.Constants
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class PostFavCatUseCaseTest {
    @Mock
    private lateinit var mockRepository: CatDetailsRepository

    private lateinit var mockUseCae: PostFavCatUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockUseCae = PostFavCatUseCaseImpl(mockRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit success when repository call is successful`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        // Given
        val imageId = "123"
        val response = toResponsePostSuccess(MockSuccessResponse())
        `when`(mockRepository.postFavouriteCat(PostFavCatModel(imageId, Constants.SUB_ID)))
            .thenReturn(response)
        // When
        val resultFlow = mockUseCae.execute(imageId)
        // Then
        resultFlow.collect { result ->
            if (result is NetworkResult.Success) {
                assertEquals(response.body()?.message, result.data?.successMessage)
                assertEquals(response.body()?.id, result.data?.id)
            }
        }

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on unsuccessful post data`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val error = "Invalid request"
        val imageId = "123"
        val postFavCatModel = toRequestPostFavCatData(MockPostFavCatModel())

        `when`(mockRepository.postFavouriteCat(postFavCatModel)).thenReturn(
            Response.error(
                400,
                error.toResponseBody("application/json".toMediaType())
            )
        )

        val result = mutableListOf<NetworkResult<CallSuccessModel>>()
        mockUseCae.execute(imageId).collect { result.add(it) }

        assert(result.size == 2) // Loading + Error states
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        // assertions based on the actual error received
        val errorBody = mockRepository.postFavouriteCat(postFavCatModel).errorBody()
        val errorString = errorBody?.string()
        assert(errorString == error)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on exception`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val simulatedErrorMessage = "Simulated error"
        val imageId = "123"

        `when`(mockRepository.postFavouriteCat(PostFavCatModel(imageId, Constants.SUB_ID)))
            .thenThrow(RuntimeException(simulatedErrorMessage))

        val result = mutableListOf<NetworkResult<CallSuccessModel>>()
        mockUseCae.execute(imageId).collect { result.add(it) }

        assert(result.size == 2) // Loading + Error states
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        //  assertions based on the simulated error
        val errorResult = result[1] as NetworkResult.Error
        assert(errorResult.message == simulatedErrorMessage)
    }

}