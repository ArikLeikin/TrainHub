import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.trainhub.R

class SignUpFragment : Fragment() {

    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize EditText fields
        emailEditText = view.findViewById(R.id.etRegisterEmail)
        passwordEditText = view.findViewById(R.id.etRegisterPassword)

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            // Handle login button click
            // Example: validate inputs and perform login/signup action
            val email = emailEditText?.text.toString()
            val password = passwordEditText?.text.toString()
            // Your login/signup logic here
        }

    }
}
