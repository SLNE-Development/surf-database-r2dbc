package dev.slne.surf.database

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test to verify that multiple transactions don't produce 
 * "Transaction characteristics can't be changed" warnings with MariaDB.
 */
class TransactionWarningTest : DatabaseTestBase() {

    @Test
    fun multipleTransactionsDoNotCauseWarnings() = runBlocking {
        // Execute multiple transactions in sequence
        // Each should work without causing MariaDB warnings
        
        suspendTransaction {
            exec("SELECT 1") { result ->
                assertEquals(1, result.get(1))
            }
        }
        
        suspendTransaction {
            exec("SELECT 2") { result ->
                assertEquals(2, result.get(1))
            }
        }
        
        suspendTransaction {
            exec("SELECT 3") { result ->
                assertEquals(3, result.get(1))
            }
        }
    }
    
    @Test
    fun nestedQueriesInSingleTransaction() = runBlocking {
        // Execute multiple queries within a single transaction
        suspendTransaction {
            exec("SELECT 1") { result ->
                assertEquals(1, result.get(1))
            }
            
            exec("SELECT 2") { result ->
                assertEquals(2, result.get(1))
            }
            
            exec("SELECT 3") { result ->
                assertEquals(3, result.get(1))
            }
        }
    }
}
