package web.id.anproject.rekapkeuangan


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rekap.*
import web.id.anproject.rekapkeuangan.adapter.RekapAdapter

class RekapFragment : Fragment() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private var tahuns: Array<String> = arrayOf()
    private lateinit var selectedTahun: String
    private lateinit var selectedBulan: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rekap, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rekapRv.layoutManager = LinearLayoutManager(activity)

        this.disposable1 = webService.tahuns()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                it.tahuns.forEachIndexed { index, s -> this.tahuns += s }
            }, {
                var message = it.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            })

        optionTahun.keyListener = null
        optionTahun.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Tahun")
            builder.setItems(this.tahuns, DialogInterface.OnClickListener { dialog, which ->
                optionTahun.setText(tahuns[which])
                this.selectedTahun = tahuns[which]
                getRekap()
            })
            builder.show()
        }

        optionBulan.keyListener = null
        optionBulan.setOnClickListener {
            val bulans = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Bulan")
            builder.setItems(bulans, DialogInterface.OnClickListener { dialog, which ->
                optionBulan.setText(bulans[which])
                this.selectedBulan = bulans[which]
                getRekap()
            })
            builder.show()
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

    fun getRekap() {
        if ((this::selectedBulan.isInitialized && this.selectedBulan.isNotEmpty()) && (this::selectedTahun.isInitialized && this.selectedTahun.isNotEmpty())) {
            this.disposable2 = webService.getRekap(this.selectedTahun, this.selectedBulan)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    rekapRv.adapter = RekapAdapter(it, requireContext())
                    val terealisasi = it.pengeluaranBulan.toFloat() / it.anggaranBulan.toFloat() * 100f
                    val tidakTerealisasi = it.sisaAnggaran.toFloat() / it.anggaranBulan.toFloat() * 100f
                    anggaran_bulan.text = "Rp."+"%,d".format(it.anggaranBulan)
                    transaksi_bulan.text = "Rp."+"%,d".format(it.pengeluaranBulan) + "(" + String.format("%.1f", terealisasi) + "%)"
                    sisa_bulan.text = "Rp."+"%,d".format(it.sisaAnggaran) + "(" + String.format("%.1f", tidakTerealisasi) + "%)"

                    anggaranBulanIniBox.visibility = View.VISIBLE
                    transaksiBulanIniBox.visibility = View.VISIBLE
                    sisaBulanIniBox.visibility = View.VISIBLE
                }, {
                    var message = it.localizedMessage
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
        }
    }

}
