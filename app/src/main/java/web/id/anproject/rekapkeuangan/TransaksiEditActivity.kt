package web.id.anproject.rekapkeuangan

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaksi_edit.*
import web.id.anproject.rekapkeuangan.model.BRTransaksi
import java.util.*

class TransaksiEditActivity : AppCompatActivity() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private var tahuns: Array<String> = arrayOf()
    private var tahunIds: Array<String> = arrayOf()
    private lateinit var selectedTahun: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_edit)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val iid = intent.extras?.get("id").toString()
        val ikegiatan = intent.extras?.get("kegiatan").toString()
        val ikode = intent.extras?.get("kode").toString()
        val ipengeluaran = intent.extras?.get("pengeluaran").toString()
        val itahun= intent.extras?.get("tahun").toString()
        val itanggal = intent.extras?.get("tanggal").toString()

        toolbar_title.text = ikode + " - " + ikegiatan
        pengeluaran.setText(ipengeluaran)
        tanggal.setText(itanggal)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

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

        tanggal.keyListener = null
        tanggal.setOnClickListener {
            hideKeyBoard()
            val dateTimePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                tanggal.setText("${year}-${month+1}-${dayOfMonth}")
            }, year, month, day)
            dateTimePicker.show()
        }

        saveTransaksi.setOnClickListener {
            if (!this::selectedTahun.isInitialized) {
                val idx = (this.tahuns.indices).firstOrNull{i: Int -> itahun == this.tahuns[i]}
                if (idx != null) {
                    this.selectedTahun = this.tahunIds[idx]
                }
            }
            val kegiatan = ikegiatan
            val kode = ikode
            val pengeluaran = pengeluaran.text.toString()
            val tahun = itahun
            val tanggal = tanggal.text.toString()
            val allValid = kegiatan.isNotEmpty() && kode.isNotEmpty() && pengeluaran.isNotEmpty() &&
                    tahun.isNotEmpty() && tanggal.isNotEmpty()
            if (allValid && this::selectedTahun.isInitialized) {
                this.disposable4 = webService.transaksiUpdate(
                    iid,
                    BRTransaksi(kode, kegiatan, pengeluaran, this.selectedTahun, tanggal)
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(this, "Update transaksi berhasil", Toast.LENGTH_SHORT).show()
                        val returnIntent = Intent()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()

                    }, {
                        //                        var message = it.localizedMessage
                        Toast.makeText(this, "Kegiatan sudah diinput atau tidak ada anggaran di bulan tersebut", Toast.LENGTH_SHORT).show()
                    })
            } else {
                Toast.makeText(this, "Isikan semua data", Toast.LENGTH_SHORT).show()
            }
        }

        deleteTransaksi.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Penghapusan Data")
            builder.setMessage("Apakah anda yakin ingin menghapus dokumen ini ?")
            builder.setNegativeButton("TIDAK") {dialog, which ->
                false
            }
            builder.setPositiveButton("YA"){dialog, which ->
                this.disposable3 = webService.transaksiDelete(iid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                        val returnIntent = Intent()
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }, {
                        var message = it.localizedMessage
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    })
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::disposable1.isInitialized) {
            this.disposable1.dispose()
        }
        if (this::disposable2.isInitialized) {
            this.disposable2.dispose()
        }
        if (this::disposable3.isInitialized) {
            this.disposable3.dispose()
        }
        if (this::disposable4.isInitialized) {
            this.disposable4.dispose()
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
}
