package web.id.anproject.rekapkeuangan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth.*
import web.id.anproject.rekapkeuangan.model.BRLogin

class AuthActivity : AppCompatActivity() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnLogin.setOnClickListener {
            val bodyLogin = BRLogin(emailLogin.text.toString(), passwordLogin.text.toString())
            this.disposable = webService.login(bodyLogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val sharedEditor: SharedPreferences.Editor? = applicationContext
                        ?.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                        ?.edit()
                    var user: String = Gson().toJson(it)
                    sharedEditor?.putString("DATA_LOGIN", user)
                    sharedEditor?.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, {
                    Toast.makeText(this, "Login error", Toast.LENGTH_LONG).show()
                })
        }
    }
}
