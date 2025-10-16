import com.bhuvanesh.network.ApiResult
import com.bhuvanesh.task.data.remote.Content
import com.bhuvanesh.task.data.remote.Holdings
import com.bhuvanesh.task.data.remote.HoldingsData
import com.bhuvanesh.task.data.uidata.HoldingsUiData
import com.bhuvanesh.task.data.uidata.TotalInvestmentUiData
import com.bhuvanesh.task.data.uidata.UIData
import com.bhuvanesh.task.data.uidata.UIState
import com.bhuvanesh.task.data.uidata.calculateTotals
import com.bhuvanesh.task.data.uidata.toHoldingsUiDataList
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserPortfolioTest {

    // 1. Mock the repository dependency
    @MockK
    private lateinit var holdingsRepository: HoldingsRepository

    // 2. The class being tested (Subject Under Test)
    private lateinit var userPortfolio: UserPortfolio

    @Before
    fun setUp() {
        // Initializes all the @MockK annotated fields
        MockKAnnotations.init(this)
        // Create a new instance of the use case before each test
        userPortfolio = UserPortfolio(holdingsRepository)
    }

    @Test
    fun `invoke - when repository returns Success - emits Loading and then Success`() = runTest {
        // ARRANGE  arranger (Arrange)
        // Create fake network data that the repository will return
        val fakeNetworkData = HoldingsData(
            data = Content(
                userHolding = listOf(
                    Holdings(
                        symbol = "TCS",
                        quantity = 10,
                        ltp = 3500.0,
                        avgPrice = 3200.0,
                        close = 3450.0
                    ),
                    Holdings(
                        symbol = "WIPRO",
                        quantity = 50,
                        ltp = 450.0,
                        avgPrice = 475.0,
                        close = 460.0
                    )
                )
            )
        )
        // Define the expected UI data after mapping and calculations
        val expectedUiData = UIData(
            holdings = listOf(
                HoldingsUiData("TCS", 3500.0, 10, 3200.0, 3450.0),
                HoldingsUiData("WIPRO", 450.0, 50, 475.0, 460.0)
            ),
            totalInvestmentUiData = TotalInvestmentUiData(
                currentValue = (3500.0 * 10) + (450.0 * 50), // 35000 + 22500 = 57500
                totalInvestment = (3200.0 * 10) + (475.0 * 50), // 32000 + 23750 = 55750
                totalPnL = 1750.0,
                todayPnL = ((3500.0 - 3450.0) * 10) + ((450.0 - 460.0) * 50) // 500 + (-500) = 0
            )
        )

        // Mock the repository's suspend function to return a Flow with the fake data
        coEvery { holdingsRepository.getHoldings() } returns flowOf(
            ApiResult.Success(
                fakeNetworkData
            )
        )

        // ACT agir (Act)
        // Call the use case and collect all emitted states into a list
        val emissions = userPortfolio.invoke().toList()

        // ASSERT affirmer (Assert)
        // Expect two emissions: Loading (from .onStart) and then Success
        assertEquals(2, emissions.size)
        assertTrue("First emission should be Loading", emissions[0] is UIState.Loading)

        val successState = emissions[1] as UIState.Success
        assertEquals("Second emission should be Success", expectedUiData, successState.data)
    }

    @Test
    fun `invoke - when repository returns Error - emits Loading and then Failure`() = runTest {
        // ARRANGE
        val errorMessage = "Network request failed"
        // Mock the repository to return a Flow with an error
        coEvery { holdingsRepository.getHoldings() } returns flowOf(
            ApiResult.Error(
                404,
                errorMessage
            )
        )

        // ACT
        val emissions = userPortfolio.invoke().toList()

        // ASSERT
        // Expect two emissions: Loading and then Failure
        assertEquals(2, emissions.size)
        assertTrue("First emission should be Loading", emissions[0] is UIState.Loading)

        val failureState = emissions[1] as UIState.Failure
        assertEquals("Second emission should be Failure", errorMessage, failureState.error)
    }

    //================================================================================
    // Tests for `toHoldingsUiDataList()`
    //================================================================================

    @Test
    fun `toHoldingsUiDataList - given a valid list of holdings, then maps correctly to UI data`() {
        // ARRANGE: A list of valid DTOs from the network
        val networkHoldings = listOf(
            Holdings("TATA", 10, 120.5, 100.0, 110.0),
            Holdings("RELIANCE", 5, 2500.0, 2450.5, 2480.0)
        )

        // ACT: Call the extension function to map the data
        val uiDataList = networkHoldings.toHoldingsUiDataList()

        // ASSERT: Check that the mapping was successful and all data is correct
        assertEquals(2, uiDataList.size)

        // Check first item
        assertEquals("TATA", uiDataList[0].symbol)
        assertEquals(120.5, uiDataList[0].ltp, 0.0)
        assertEquals(10, uiDataList[0].netQuantity)
        assertEquals(100.0, uiDataList[0].avgPrice, 0.0)
        assertEquals(110.0, uiDataList[0].close, 0.0)

        // Check second item
        assertEquals("RELIANCE", uiDataList[1].symbol)
        assertEquals(2500.0, uiDataList[1].ltp, 0.0)
        assertEquals(5, uiDataList[1].netQuantity)
    }

    @Test
    fun `toHoldingsUiDataList - given an empty list, then returns an empty list`() {
        // ARRANGE: An empty list from the network
        val networkHoldings = emptyList<Holdings?>()

        // ACT: Call the mapper
        val uiDataList = networkHoldings.toHoldingsUiDataList()

        // ASSERT: The result should also be an empty list
        assertEquals(0, uiDataList.size)
    }

    @Test
    fun `toHoldingsUiDataList - given a list with null entries, then filters them out`() {
        // ARRANGE: A mixed list containing a valid object and a null
        val networkHoldings = listOf(
            Holdings("TATA", 10, 120.5, 100.0, 110.0),
            null
        )

        // ACT: Call the mapper
        val uiDataList = networkHoldings.toHoldingsUiDataList()

        // ASSERT: The final list should only contain the one valid item
        assertEquals(1, uiDataList.size)
        assertEquals("TATA", uiDataList[0].symbol)
    }

    @Test
    fun `toHoldingsUiDataList - given holdings with null properties, then maps them to default values`() {
        // ARRANGE: A DTO with nulls for all nullable properties except the symbol
        val networkHoldings = listOf(
            Holdings("TATA", null, null, null, null)
        )

        // ACT: Call the mapper
        val uiDataList = networkHoldings.toHoldingsUiDataList()

        // ASSERT: The UI model should have default values (0 or 0.0)
        assertEquals(1, uiDataList.size)
        assertEquals("TATA", uiDataList[0].symbol)
        assertEquals(0, uiDataList[0].netQuantity)
        assertEquals(0.0, uiDataList[0].ltp, 0.0)
        assertEquals(0.0, uiDataList[0].avgPrice, 0.0)
        assertEquals(0.0, uiDataList[0].close, 0.0)
    }

    @Test
    fun `toHoldingsUiDataList - given holdings with blank or null symbols, then filters them out`() {
        // ARRANGE: A list of DTOs with invalid symbols that should be removed
        val networkHoldings = listOf(
            Holdings(null, 10, 120.0, 100.0, 110.0),
            Holdings("", 5, 2500.0, 2450.0, 2480.0),
            Holdings("VALID", 1, 10.0, 9.0, 8.0)
        )

        // ACT: Call the mapper
        val uiDataList = networkHoldings.toHoldingsUiDataList()

        // ASSERT: Only the item with the "VALID" symbol should remain
        assertEquals(1, uiDataList.size)
        assertEquals("VALID", uiDataList[0].symbol)
    }

    //================================================================================
    // Tests for `calculateTotals()`
    //================================================================================

    @Test
    fun `calculateTotals - given a valid list of UI holdings, then calculates totals correctly`() {
        // ARRANGE: A list of already-mapped UI data models
        val holdingsUiData = listOf(
            // Item 1: Profit
            HoldingsUiData("TATA", 120.0, 10, 100.0, 110.0),
            // Item 2: Loss
            HoldingsUiData("RELIANCE", 2500.0, 5, 2600.0, 2550.0)
        )
        // Expected calculations:
        // Current Value = (120 * 10) + (2500 * 5) = 1200 + 12500 = 13700
        // Total Investment = (100 * 10) + (2600 * 5) = 1000 + 13000 = 14000
        // Total PnL = (13700 - 14000) = -300
        // Today's PnL = ((120 - 110) * 10) + ((2500 - 2550) * 5) = 100 + (-250) = -150

        // ACT: Call the calculation function
        val totals = holdingsUiData.calculateTotals()

        // ASSERT: Check if all calculated totals match the expected values
        assertEquals(13700.0, totals.currentValue, 0.001)
        assertEquals(14000.0, totals.totalInvestment, 0.001)
        assertEquals(-300.0, totals.totalPnL, 0.001)
        assertEquals(-150.0, totals.todayPnL, 0.001)
    }

    @Test
    fun `calculateTotals - given an empty list, then returns all zero values`() {
        // ARRANGE: An empty list of UI data
        val holdingsUiData = emptyList<HoldingsUiData>()

        // ACT: Call the calculation function
        val totals = holdingsUiData.calculateTotals()

        // ASSERT: All properties should be zero
        assertEquals(0.0, totals.currentValue, 0.0)
        assertEquals(0.0, totals.totalInvestment, 0.0)
        assertEquals(0.0, totals.totalPnL, 0.0)
        assertEquals(0.0, totals.todayPnL, 0.0)
    }
}