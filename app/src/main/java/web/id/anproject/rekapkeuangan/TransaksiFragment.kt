package web.id.anproject.rekapkeuangan


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_transaksi.*
import web.id.anproject.rekapkeuangan.adapter.TransaksiAdapter

class TransaksiFragment : Fragment() {

    private val TRANSAKSI_FOR_RESULT: Int = 1
    private val webService: WebService = WebService.create()
    private lateinit var disposable: Disposable
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private lateinit var selectedBulan: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaksi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionBulan.setText("")

        transaksiRv.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener {
            val intent = Intent(context, AddTransaksiActivity::class.java)
            startActivityForResult(intent, TRANSAKSI_FOR_RESULT)
        }

        getTransaksi()

        optionBulan.keyListener = null
        optionBulan.setOnClickListener {
            val bulans = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Bulan")
            builder.setItems(bulans, DialogInterface.OnClickListener { dialog, which ->
                optionBulan.setText(bulans[which])
                this.selectedBulan = (which + 1).toString()
                getTransaksiBulan()
            })
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TRANSAKSI_FOR_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                getTransaksi()
            }
        }
    }

    fun getTransaksi() {
        onLoad()
        this.disposable = webService.transaksi()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                transaksiRv.adapter = TransaksiAdapter(it, requireContext())
                transaksiRv.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {
                        val intent = Intent(context, TransaksiEditActivity::class.java)
                        intent.putExtra("id", it.transaksi[position].id)
                        intent.putExtra("kode", it.transaksi[position].kode)
                        intent.putExtra("kegiatan", it.transaksi[position].kegiatan)
                        intent.putExtra("pengeluaran", it.transaksi[position].pengeluaran)
                        intent.putExtra("tanggal", it.transaksi[position].tanggal)
                        intent.putExtra("tahun", it.transaksi[position].tahun)
                        startActivityForResult(intent, TRANSAKSI_FOR_RESULT)
                    }
                })
                onFinishLoad()
            }, {
                var message = it.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onFinishLoad()
            })
    }

    fun getTransaksiBulan() {
        onLoad()
        this.disposable1 = webService.transaksi(this.selectedBulan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                transaksiRv.adapter = TransaksiAdapter(it, requireContext())
                transaksiRv.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {
                        val intent = Intent(context, TransaksiEditActivity::class.java)
                        intent.putExtra("id", it.transaksi[position].id)
                        intent.putExtra("kode", it.transaksi[position].kode)
                        intent.putExtra("kegiatan", it.transaksi[position].kegiatan)
                        intent.putExtra("pengeluaran", it.transaksi[position].pengeluaran)
                        intent.putExtra("tanggal", it.transaksi[position].tanggal)
                        intent.putExtra("tahun", it.transaksi[position].tahun)
                        startActivityForResult(intent, TRANSAKSI_FOR_RESULT)
                    }
                })
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

    fun onLoad() {
        transaksiRv.visibility = View.GONE
        fab.visibility = View.GONE
        pb.visibility = View.VISIBLE
    }

    fun onFinishLoad() {
        transaksiRv.visibility = View.VISIBLE
        fab.visibility = View.VISIBLE
        pb.visibility = View.GONE
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

}
