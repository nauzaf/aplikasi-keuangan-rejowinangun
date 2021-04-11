package web.id.anproject.rekapkeuangan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mToggle: ActionBarDrawerToggle

    private val HomeFragment = HomeFragment()
    private val AnggaranFragment = AnggaranFragment()
    private val TransaksiFragment = TransaksiFragment()
    private val RekapFragment = RekapFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dataLogin = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).getString("DATA_LOGIN", null)
        if (dataLogin == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_main)

            mToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar_main, R.string.open, R.string.close)
            drawer_layout.addDrawerListener(mToggle)
            mToggle.syncState()

            nav_view.setNavigationItemSelectedListener(this)
            addFragment(HomeFragment)

            btnKeluar.setOnClickListener {
                val sharedEditor: SharedPreferences.Editor? = this.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    .edit()
                sharedEditor?.remove("DATA_LOGIN")
                sharedEditor?.apply()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                toolbar_title.text = "HOME"
                addFragment(HomeFragment)
            }
            R.id.anggaran -> {
                toolbar_title.text = "ANGGARAN"
                addFragment(AnggaranFragment)
            }
            R.id.transaksi -> {
                toolbar_title.text = "TRANSAKSI"
                addFragment(TransaksiFragment)
            }
            R.id.rekap -> {
                toolbar_title.text = "REKAP"
                addFragment(RekapFragment)
            }
            else -> true
        }
        drawer_layout.closeDrawers()
        return true
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commitAllowingStateLoss()
    }
}
