package web.id.anproject.rekapkeuangan


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardview.radius = 20.0f
        anggaranBulanIniBox.radius = 10.0f
        transaksiBulanIniBox.radius = 10.0f
        sisaBulanIniBox.radius = 10.0f
        onLoad()
        this.disposable = webService.statistic()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                anggaran_bulan.text = "Rp."+"%,d".format(it.anggaranBulan)
                transaksi_bulan.text = "Rp."+"%,d".format(it.pengeluaranBulan)
                sisa_bulan.text = "Rp."+"%,d".format(it.sisaAnggaran)
                val terealisasi = it.pengeluaranBulan.toFloat() / it.anggaranBulan.toFloat() * 100f
                val tidakTerealisasi = it.sisaAnggaran.toFloat() / it.anggaranBulan.toFloat() * 100f
                persentage1.text = String.format("%.1f", terealisasi) + "%"
                persentage2.text = String.format("%.1f", tidakTerealisasi) + "%"
                onFinishLoad()
            }, {
                var message = it.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onFinishLoad()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::disposable.isInitialized) {
            this.disposable.dispose()
        }
    }

    fun onLoad() {
        cardview.visibility = View.GONE
        logo_pemkot.visibility = View.GONE
        logoText.visibility = View.GONE
        anggaranBulanIniBox.visibility = View.GONE
        transaksiBulanIniBox.visibility = View.GONE
        sisaBulanIniBox.visibility = View.GONE
        pb.visibility = View.VISIBLE
    }

    fun onFinishLoad() {
        cardview.visibility = View.VISIBLE
        logo_pemkot.visibility = View.VISIBLE
        logoText.visibility = View.VISIBLE
        anggaranBulanIniBox.visibility = View.VISIBLE
        transaksiBulanIniBox.visibility = View.VISIBLE
        sisaBulanIniBox.visibility = View.VISIBLE
        pb.visibility = View.GONE
    }

}
