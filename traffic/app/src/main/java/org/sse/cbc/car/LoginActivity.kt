package org.sse.cbc.car

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.sse.cbc.car.utils.SharedPreferencesUtils
import shem.com.materiallogin.DefaultLoginView
import shem.com.materiallogin.DefaultRegisterView
import shem.com.materiallogin.MaterialLoginView

class LoginActivity : AppCompatActivity() {

    private val preferences by lazy { SharedPreferencesUtils(this) }

    private fun View.yum(text: String, length: Int = Snackbar.LENGTH_SHORT): Snackbar {
        return Snackbar.make(this, text, length)
    }

    private fun onRegister(user: String, password: String) {
        preferences.username = user
        preferences.isLogin = true
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    private fun onLogin(user: String, password: String) {
        preferences.username = user
        preferences.isLogin = true
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (preferences.isLogin){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        ((login as MaterialLoginView).registerView as DefaultRegisterView).setListener { registerUser, registerPass,
                                                                                         registerPassRep ->
            val passRep = registerPassRep.editText!!.text.toString()
            val pass = registerPass.editText!!.text.toString()
            val user = registerUser.editText!!.text.toString()
            if (passRep != pass) {
                login.yum("两次密码不同！").show()
            } else if (pass == "" || user == "") {
                login.yum("无效的用户名或密码！").show()
            } else {
                onRegister(user, pass)
            }
        }
        ((login as MaterialLoginView).loginView as DefaultLoginView).setListener { loginUser, loginPass ->
            val pass = loginPass.editText!!.text.toString()
            val user = loginUser.editText!!.text.toString()
            if (user == "" || pass == "") {
                login.yum("无效的用户名或密码！").show()
            } else {
                onLogin(user, pass)
            }
        }
        findViewById<TextView>(R.id.user_mode).setOnClickListener {
            preferences.isPassenger = true
            var intent = Intent(this@LoginActivity, ValidActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
