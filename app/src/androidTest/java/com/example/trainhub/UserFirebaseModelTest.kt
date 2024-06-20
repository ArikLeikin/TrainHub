//import android.content.ContentValues.TAG
//import android.nfc.Tag
//import android.util.Log
//import com.example.trainhub.models.entities.User
//import kotlinx.coroutines.runBlocking
//import org.junit.Test
//import org.junit.Assert.*
//import com.example.trainhub.models.fireBaseModels.UserFireBaseModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.protobuf.Internal.BooleanList
//import java.util.Date
//import java.util.concurrent.CountDownLatch
//import java.util.concurrent.TimeUnit
//
//class UserFirebaseModelTask {
//
//
//    private val userFbModel: UserFireBaseModel = UserFireBaseModel()
//    private val email: String = "test@test.com"
//    private val password: String = "123456"
//    private val latch = CountDownLatch(1)
//    private val uid: String? = null
//    private val user: User? = null
//    private var userAuth: FirebaseUser?= null
//
//    @Test
//    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
//    }
//
//    @Test
//    fun testRegisterSuccess() {
//        val latch = CountDownLatch(1)
//        var result: String? = null
//        var uid: String? = null
//
//        userFbModel.register(email, password) { uidResult ->
//            println("======= Success UID: $uidResult")
////            userAuth = result
//            uid = uidResult
//            latch.countDown()
//        }
//
//        latch.await(5, TimeUnit.SECONDS) // wait for the callback
//        assertNotNull(uid)
//    }
//
//    @Test
//    fun testSignInSuccess(){
//        var result: Boolean = false
//        userFbModel.signIn(email,password){ signedIn ->
//            if(signedIn)
//                result = true
//            latch.countDown()
//        }
//        val success = latch.await(5, TimeUnit.SECONDS)
//
//        assertEquals(true, success)
//        assertEquals(true, result != null)
//
//        Log.i(TAG,"Test signInSuccess UID: $result")
//    }
//
//    @Test
//    fun testAddUserSuccess() {
//        val newUser = User(
//            id = "testUid",
//            email = email,
////            password = password,
//            profileImageUrl = "test_profile_image_url",
//            lastUpdated = System.currentTimeMillis()
//        )
//
//        val latch = CountDownLatch(1)
//        var result: Boolean? = null
//
//        userFbModel.addUserDocument(newUser) { success ->
//            result = success
//            latch.countDown()
//        }
//
//        latch.await(5, TimeUnit.SECONDS) // wait for the callback
//
//        assertEquals(true, result)
//    }
//
//    @Test
//    fun testRegisterFailure() {
//        val userFbModel = UserFireBaseModel()
//        val email = "invalidemail"
//        val password = "123"
//        val latch = CountDownLatch(1)
//        var result: String? = null
//
//        userFbModel.register(email, password) {
////            result = it
//            latch.countDown()
//        }
//
//        latch.await(5, TimeUnit.SECONDS) // wait for the callback
//
//        assertNull(result)
//    }
//
//    @Test
//    fun testDeleteUserAuth(){
//        var result: Boolean = false
//        val latch = CountDownLatch(1)
//        userFbModel.deleteUserAuth() {
//            result = true
//            latch.countDown()
//        }
//        latch.await(5, TimeUnit.SECONDS)
//        assertEquals(true, result)
//    }
//
//    @Test
//    fun f(){
//        testRegisterSuccess()
//        testDeleteUserAuth()
//    }
//
//}
