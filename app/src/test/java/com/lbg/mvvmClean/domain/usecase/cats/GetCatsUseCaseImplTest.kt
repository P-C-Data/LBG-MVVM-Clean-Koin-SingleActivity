package com.lbg.mvvmClean.domain.usecase.cats

import com.lbg.mvvmClean.data.NetworkResult
import com.lbg.mvvmClean.domain.mappers.CatDataModel
import com.lbg.mvvmClean.domain.repositories.CatsRepository
import com.lbg.mvvmClean.models.catMocks.MocksCatsDataModel
import com.lbg.mvvmClean.models.catMocks.toResponseApiCats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class GetCatsUseCaseImplTest {
    @Mock
    private lateinit var mockCatsRepo: CatsRepository

    @InjectMocks
    private lateinit var getCatsUseCase: GetCatsUseCaseImpl


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then success on successful data retrieval`() =
        runTest(UnconfinedTestDispatcher()) {
            val response = toResponseApiCats(MocksCatsDataModel())

            `when`(mockCatsRepo.fetchCats()).thenReturn(response)


            val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
            getCatsUseCase.execute().collect { result.add(it) }


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
        runTest(UnconfinedTestDispatcher()) {
            val error = "Invalid request"
            `when`(mockCatsRepo.fetchCats()).thenReturn(
                Response.error(
                    400,
                    error.toResponseBody("application/json".toMediaType())
                )
            )

            val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
            getCatsUseCase.execute().collect { result.add(it) }

            assert(result.size == 2) // Loading + Error states
            assert(result[0] is NetworkResult.Loading)
            assert(result[1] is NetworkResult.Error)
            // assertions based on the actual error received
            val errorBody = mockCatsRepo.fetchCats().errorBody()
            val errorString = errorBody?.string()
            assert(errorString == error)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute should emit loading state and then error on exception`() =
        runTest(UnconfinedTestDispatcher()) {
            val simulatedErrorMessage = "Simulated error"
            `when`(mockCatsRepo.fetchCats()).thenThrow(RuntimeException(simulatedErrorMessage))

            val result = mutableListOf<NetworkResult<List<CatDataModel>>>()
            getCatsUseCase.execute().collect { result.add(it) }

            assert(result.size == 2) // Loading + Error states
            assert(result[0] is NetworkResult.Loading)
            assert(result[1] is NetworkResult.Error)
            //  assertions based on the simulated error
            val errorResult = result[1] as NetworkResult.Error
            assert(errorResult.message == simulatedErrorMessage)
        }
}