package web.id.anproject.rekapkeuangan

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_anggaran_edit.*
import web.id.anproject.rekapkeuangan.model.BRAnggaran

class AnggaranEditActivity : AppCompatActivity() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private var tahuns: Array<String> = arrayOf()
    private var tahunIds: Array<String> = arrayOf()
    private lateinit var selectedTahun: String
    private var selectedBulan: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anggaran_edit)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val iid = intent.extras?.get("id").toString()
        val ikegiatan = intent.extras?.get("kegiatan").toString()
        val ikode = intent.extras?.get("kode").toString()
        val ianggaran = intent.extras?.get("anggaran").toString()
        val ivolume = intent.extras?.get("volume").toString()
        val itahun = intent.extras?.get("tahun").toString()
        val ibulan = intent.extras?.get("bulan_realisasi").toString()
        val irole = intent.extras?.get("role_id").toString()
        val ibulans = ibulan.split(",").toTypedArray()

        toolbar_title.text = ikode + " - " + ikegiatan
        anggaran.setText(ianggaran)
        volume.setText(ivolume)
        optionTahun.setText(itahun)

        if ("Januari" in ibulans) {
            januari.isChecked = true
        }

        if ("Februari" in ibulans) {
            februari.isChecked = true
        }

        if ("Maret" in ibulans) {
            maret.isChecked = true
        }

        if ("April" in ibulans) {
            april.isChecked = true
        }

        if ("Mei" in ibulans) {
            mei.isChecked = true
        }

        if ("Juni" in ibulans) {
            juni.isChecked = true
        }

        if ("Juli" in ibulans) {
            juli.isChecked = true
        }

        if ("Agustus" in ibulans) {
            agustus.isChecked = true
        }

        if ("September" in ibulans) {
            september.isChecked = true
        }

        if ("Oktober" in ibulans) {
            oktober.isChecked = true
        }

        if ("November" in ibulans) {
            november.isChecked = true
        }

        if ("Desember" in ibulans) {
            desember.isChecked = true
        }

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
            if (irole.equals("2")) {
                Toast.makeText(this, "Anda tidak memiliki akses untuk mengubah", Toast.LENGTH_SHORT).show()
            } else {
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

                var vol = 0

                if (volume.text.isNotEmpty()) {
                    vol = Integer.parseInt(volume.text.toString())
                }

                if (!this::selectedTahun.isInitialized) {
                    val idx = (this.tahuns.indices).firstOrNull{i: Int -> itahun == this.tahuns[i]}
                    if (idx != null) {
                        this.selectedTahun = this.tahunIds[idx]
                    }
                }

                if (selectedBulan.count() != vol) {
                    Toast.makeText(this, "Volume dan bulan realisasi tidak cocok", Toast.LENGTH_SHORT).show()
                } else if (!this::selectedTahun.isInitialized) {
                    Toast.makeText(this, "Tahun belum dipilih", Toast.LENGTH_SHORT).show()
                } else {
                    val keg = ikegiatan
                    val kod = ikode
                    val anggar = anggaran.text.toString()
                    val tahun = optionTahun.text.toString()
                    val allValid = keg.isNotEmpty() && kod.isNotEmpty() && anggar.isNotEmpty() &&
                            tahun.isNotEmpty() && selectedBulan.count() != 0
                    if (allValid) {
                        val bodyAnggaran = BRAnggaran(kod, keg, Integer.parseInt(anggar), this.selectedTahun, selectedBulan, vol)
                        this.disposable2 = webService.anggaranUpdate(iid, bodyAnggaran)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                Toast.makeText(this, "Update anggaran berhasil", Toast.LENGTH_SHORT).show()
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

        deleteAnggaran.setOnClickListener {
            if (irole.equals("2")) {
                Toast.makeText(this, "Anda tidak memiliki akses untuk menghapus", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Penghapusan Data")
                builder.setMessage("Apakah anda yakin ingin menghapus dokumen ini ?")
                builder.setNegativeButton("TIDAK") {dialog, which ->
                    false
                }
                builder.setPositiveButton("YA"){dialog, which ->
                    this.disposable3 = webService.anggaranDelete(iid)
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
