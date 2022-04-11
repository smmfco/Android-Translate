package com.example.translation.ui.register

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.translation.R
import com.example.translation.ui.login.ui.login.LoginFragment
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener { view ->
            val userID: String = et_id.text.toString()
            val userPass: String = et_pass.text.toString()
            val userName: String = et_name.text.toString()
            val userAge: Int = Integer.parseInt(et_age.text.toString())

            val responseListener: Response.Listener<String> =
                Response.Listener<String>() { response ->
                    try {
                        val jsonObject: JSONObject = JSONObject(response)
                        val success: Boolean = jsonObject.getBoolean("success")
                        if (success) {
                            Toast.makeText(applicationContext, "회원등록에 성공하였습니다.", Toast.LENGTH_SHORT)
                                .show()
                            onBackPressed()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "회원 등록에 실패하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            val registerRequest : RegisterRequest = RegisterRequest(
                userID,
                userPass,
                userName,
                userAge,
                responseListener
            )
            val queue : RequestQueue = Volley.newRequestQueue(this)
            queue.add(registerRequest)
        }
    }
}