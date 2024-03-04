package com.clean.mvvmLgb.domain.usecase.cats

import com.clean.mvvmLgb.data.NetworkResult
import com.clean.mvvmLgb.domain.mappers.CatDataModel
import com.clean.mvvmLgb.domain.repositories.CatsRepository
import com.clean.mvvmLgb.models.catMocks.MockFavouriteCatsResponse
import com.clean.mvvmLgb.models.catMocks.toResponseApiFavCats
import com.clean.mvvmLgb.utils.Constants
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
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class GetFavCatsUseCaseImplTest {

    @Mock
    private lateinit var mockCatsRepo: CatsRepository


    private lateinit var getFavCatsUseCase: GetFavCatsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getFavCatsUseCase = GetFavCatsUseCaseImpl(mockCatsRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then success on successful data retrieval`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            val response = toResponseApiFavCats(MockFavouriteCatsResponse())

            Mockito.`when`(mockCatsRepo.fetchFavouriteCats(Constants.SUB_ID)).thenReturn(response)


            val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
            getFavCatsUseCase.execute().collect { result.add(it) }


            assert(result.size == 2) // Loading + Success states
            assert(result[0] is NetworkResult.Loading)
            assert(result[1] is NetworkResult.Success)
            // assertions based on the actual data received
            val successResult = result[1] as NetworkResult.Success
            assert(successResult.data?.size == response.body()?.size)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on unsuccessful data retrieval`() =
        runTest(
            UnconfinedTestDispatcher()
        ) {
            val error = "Invalid request"
            Mockito.`when`(mockCatsRepo.fetchFavouriteCats(Constants.SUB_ID)).thenReturn(
                Response.error(
                    400,
                    error.toResponseBody("application/json".toMediaType())
                )
            )

            val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
            getFavCatsUseCase.execute().collect { result.add(it) }

            assert(result.size == 2) // Loading + Error states
            assert(result[0] is NetworkResult.Loading)
            assert(result[1] is NetworkResult.Error)
            // assertions based on the actual error received
            val errorBody = mockCatsRepo.fetchFavouriteCats(Constants.SUB_ID).errorBody()
            val errorString = errorBody?.string()
            assert(errorString == error)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on exception`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val simulatedErrorMessage = "Simulated error"
        Mockito.`when`(mockCatsRepo.fetchFavouriteCats(Constants.SUB_ID))
            .thenThrow(RuntimeException(simulatedErrorMessage))

        val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
        getFavCatsUseCase.execute().collect { result.add(it) }

        assert(result.size == 2) // Loading + Error states
        assert(result[0] is NetworkResult.Loading)
        assert(result[1] is NetworkResult.Error)
        //  assertions based on the simulated error
        val errorResult = result[1] as NetworkResult.Error
        assert(errorResult.message == simulatedErrorMessage)
    }


}
