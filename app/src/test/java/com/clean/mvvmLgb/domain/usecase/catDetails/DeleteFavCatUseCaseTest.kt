package com.clean.mvvmLgb.domain.usecase.catDetails

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.domain.mappers.CallSuccessModel
import com.clean.mvvmLgb.domain.repositories.CatDetailsRepository
import com.clean.mvvmLgb.domain.usecase.catsDetail.DeleteFavCatUseCaseImpl
import com.clean.mvvmLgb.models.catMocks.MockSuccessResponse
import com.clean.mvvmLgb.models.catMocks.toResponsePostSuccess
import com.clean.mvvmLgb.utils.TestTags
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class DeleteFavCatUseCaseTest {
    @Mock
    private lateinit var mockRepository: CatDetailsRepository

    @InjectMocks
    private lateinit var mockUseCae: DeleteFavCatUseCaseImpl


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then success on successful Delete fav data`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            val imageId = "123"
            val response = toResponsePostSuccess(MockSuccessResponse())
            `when`(mockRepository.deleteFavouriteCatApi(TestTags.FAV_ID)).thenReturn(response)

            // Collect the flow and assert emitted values
            val results = mutableListOf<NetworkResult<CallSuccessModel>>()
            mockUseCae.execute(imageId, TestTags.FAV_ID).collect { results.add(it) }

            // Assert emitted values
            assert(results.size == 2)
            assert(results[0] is NetworkResult.Loading)
            assert(results[1] is NetworkResult.Success)

            val successResult = results[1] as NetworkResult.Success
            assert(successResult.data?.successMessage == response.body()?.message)
            assert(successResult.data?.id == response.body()?.id)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on unsuccessful post data`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val error = "Invalid request"
        val imageId = "123"

        `when`(mockRepository.deleteFavouriteCatApi(TestTags.FAV_ID)).thenReturn(
            Response.error(
                400,
                error.toResponseBody("application/json".toMediaType())
            )
        )

        val result = mutableListOf<NetworkResult<CallSuccessModel>>()
        mockUseCae.execute(imageId, TestTags.FAV_ID).collect { result.add(it) }

        assert(result.size == 2) // Loading + Error states
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)

        // assertions based on the actual error received
        val errorBody = mockRepository.deleteFavouriteCatApi(TestTags.FAV_ID).errorBody()
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

        `when`(mockRepository.deleteFavouriteCatApi(TestTags.FAV_ID))
            .thenThrow(RuntimeException(simulatedErrorMessage))

        val result = mutableListOf<NetworkResult<CallSuccessModel>>()
        mockUseCae.execute(imageId, TestTags.FAV_ID).collect { result.add(it) }

        assert(result.size == 2) // Loading + Error states
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)

        //  assertions based on the simulated error
        val errorResult = result[1] as NetworkResult.Error
        assert(errorResult.message == simulatedErrorMessage)
    }

}