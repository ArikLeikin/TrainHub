import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import com.example.trainhub.models.fireBaseModels.UserFireBaseModel
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class UserFirebaseModelTask {

    private val email: String = "test@test.com"
    private val password: String = "123456"

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testRegisterSuccess() {
        val userFbModel = UserFireBaseModel()
        val latch = CountDownLatch(1)
        var result: Boolean? = null
        var uid: String? = null

        userFbModel.register(email, password) { success ->
            result = success
            println("======= Success Success: $success")
            if (success) {
                // Get the UID of the registered user
                uid = FirebaseAuth.getInstance().currentUser?.uid
            }
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS) // wait for the callback

        assertEquals(true, result)

        // Delete the user if registration was successful
        if (result == true && !uid.isNullOrEmpty()) {
            deleteUser(uid!!)
        }
    }

    @Test
    fun testRegisterFailure() {
        val userFbModel = UserFireBaseModel()
        val email = "invalidemail"
        val password = "123"
        val latch = CountDownLatch(1)
        var result: Boolean? = null

        userFbModel.register(email, password) {
            result = it
            latch.countDown()
        }

        latch.await(5, TimeUnit.SECONDS) // wait for the callback

        assertEquals(false, result)
    }


    // fix delete function - not deleting the user
    private fun deleteUser(uid: String) {
        val adminAuth = FirebaseAuth.getInstance()
        runBlocking {
            adminAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Signed in successfully, delete user
                        adminAuth.currentUser?.delete()
                            ?.addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    println("User deleted successfully")
                                } else {
                                    println("Failed to delete user: ${deleteTask.exception?.message}")
                                }
                            }
                    } else {
                        println("Failed to sign in as admin: ${task.exception?.message}")
                    }
                }
        }
    }
}

