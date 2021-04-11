package web.id.anproject.rekapkeuangan

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_anggaran.*
import web.id.anproject.rekapkeuangan.model.BRAnggaran

class AddAnggaranActivity : AppCompatActivity() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private var tahuns: Array<String> = arrayOf()
    private var tahunIds: Array<String> = arrayOf()
    private lateinit var selectedTahun: String
    private var selectedBulan: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_anggaran)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.disposable1 = webService.tahuns()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.tahuns.forEachIndexed { index, s -> this.tahuns += s }
                it.ids.forEachIndexed { index, s -> this.tahunIds += s  }
            }, {
                var message = it.localizedMessage
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            })

        optionTahun.keyListener = null
        optionTahun.setOnClickListener {
            hideKeyBoard()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Tahun")
            builder.setItems(this.tahuns, DialogInterface.OnClickListener { dialog, which ->
                optionTahun.setText(tahuns[which])
                this.selectedTahun = tahunIds[which]
            })
            builder.show()
        }

        saveAnggaran.setOnClickListener {

            selectedBulan.clear()

            if (januari.isChecked) {
                selectedBulan.add("Januari")
            }

            if (februari.isChecked) {
                selectedBulan.add("Februari")
            }

            if (maret.isChecked) {
                selectedBulan.add("Maret")
            }

            if (april.isChecked) {
                selectedBulan.add("April")
            }

            if (mei.isChecked) {
                selectedBulan.add("Mei")
            }

            if (juni.isChecked) {
                selectedBulan.add("Juni")
            }

            if (juli.isChecked) {
                selectedBulan.add("Juli")
            }

            if (agustus.isChecked) {
                selectedBulan.add("Agustus")
            }

            if (september.isChecked) {
                selectedBulan.add("September")
            }

            if (oktober.isChecked) {
                selectedBulan.add("Oktober")
            }

            if (november.isChecked) {
                selectedBulan.add("November")
            }

            if (desember.isChecked) {
                selectedBulan.add("Desember")
            }

            Log.e("HAAAAA", selectedBulan.count().toString())
            var vol = 0

            if (volume.text.isNotEmpty()) {
                vol = Integer.parseInt(volume.text.toString())
            }

            if (selectedBulan.count() != vol) {
                Toast.makeText(this, "Volume dan bulan realisasi tidak cocok", Toast.LENGTH_SHORT).show()
            } else {
                val keg = kegiatan.text.toString()
                val kod = kode.text.toString()
                val anggar = anggaran.text.toString()
                val tahun = optionTahun.text.toString()
                val allValid = keg.isNotEmpty() && kod.isNotEmpty() && anggar.isNotEmpty() &&
                        tahun.isNotEmpty() && selectedBulan.count() != 0
                if (allValid) {
                    val bodyAnggaran = BRAnggaran(kod, keg, Integer.parseInt(anggar), this.selectedTahun, selectedBulan, vol)
                    this.disposable2 = webService.anggaran(bodyAnggaran)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Toast.makeText(this, "Penambahan anggaran berhasil", Toast.LENGTH_SHORT).show()
                            val returnIntent = Intent()
                            setResult(Activity.RESULT_OK, returnIntent)
                            finish()
                        }, {
                            var message = it.localizedMessage
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        })
                } else {
                    Toast.makeText(this, "Isikan semua data", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun hideKeyBoard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::disposable1.isInitialized) {
            this.disposable1.dispose()
        }
        if (this::disposable2.isInitialized) {
            this.disposable2.dispose()
        }
    }
}
