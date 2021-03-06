package web.id.anproject.rekapkeuangan

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_transaksi.*
import web.id.anproject.rekapkeuangan.model.BRTransaksi
import java.util.*
import kotlin.collections.ArrayList

class AddTransaksiActivity : AppCompatActivity() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private var kegiatans: Array<String>  = arrayOf()
    private var kegiatanIds: Array<String> = arrayOf()
    private var tahuns: Array<String> = arrayOf()
    private var tahunIds: Array<String> = arrayOf()
    private lateinit var selectedKegiatan: String
    private lateinit var selectedTahun: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaksi)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        kode.isEnabled = false

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

        this.disposable2 = webService.kegiatans()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.kegiatans.forEachIndexed { index, s -> this.kegiatans += s }
                it.ids.forEachIndexed { index, s -> this.kegiatanIds += s }

            }, {
                var message = it.localizedMessage
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            })

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        tanggal.keyListener = null
        tanggal.setOnClickListener {
            hideKeyBoard()
            val dateTimePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                tanggal.setText("${year}-${month+1}-${dayOfMonth}")
            }, year, month, day)
            dateTimePicker.show()
        }

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

        optionKegiatan.keyListener = null
        optionKegiatan.setOnClickListener {
            hideKeyBoard()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Kegiatan")
            builder.setItems(this.kegiatans, DialogInterface.OnClickListener { dialog, which ->
                optionKegiatan.setText(this.kegiatans[which])
                this.selectedKegiatan = kegiatanIds[which]
                this.disposable3 = webService.getAnggaranById(kegiatanIds[which])
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        kode.setText(it.kode)
                        anggaran.setText("ANGGARAN : " + "Rp."+"%,d".format(it.anggaran))
                    }, {
                        var message = it.localizedMessage
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    })
            })
            builder.show()
        }

        saveTransaksi.setOnClickListener {
            val kegiatan = optionKegiatan.text.toString()
            val kode = kode.text.toString()
            val pengeluaran = pengeluaran.text.toString()
            val tahun = optionTahun.text.toString()
            val tanggal = tanggal.text.toString()
            val allValid = kegiatan.isNotEmpty() && kode.isNotEmpty() && pengeluaran.isNotEmpty() &&
                    tahun.isNotEmpty() && tanggal.isNotEmpty()
            if (allValid) {
                this.disposable4 = webService.transaksi(BRTransaksi(
                    kode, kegiatan, pengeluaran, this.selectedTahun, tanggal
                ))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(this, "Penambahan transaksi berhasil", Toast.LENGTH_SHORT).show()
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
