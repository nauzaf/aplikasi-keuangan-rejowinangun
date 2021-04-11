package web.id.anproject.rekapkeuangan


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_anggaran.*
import web.id.anproject.rekapkeuangan.adapter.AnggaranAdapter
import web.id.anproject.rekapkeuangan.model.Login

class AnggaranFragment : Fragment() {

    private val ANGGARAN_FOR_RESULT: Int = 2
    private val webService: WebService = WebService.create()
    private lateinit var disposable: Disposable
    private lateinit var disposable1: Disposable
    private lateinit var disposable2: Disposable
    private lateinit var disposable3: Disposable
    private lateinit var disposable4: Disposable
    private lateinit var selectedBulan: String
    lateinit var loginData: Login

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anggaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionBulan.setText("")

        anggaranRv.layoutManager = LinearLayoutManager(activity)

        val dataLogin = requireContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE).getString("DATA_LOGIN", null)
        val gson = Gson()

        loginData = gson.fromJson(dataLogin, Login::class.java)

        if (loginData.role_id.equals("2")) {
            fab.visibility = View.GONE
        }

        getAnggaran()

        fab.setOnClickListener {
            val intent = Intent(context, AddAnggaranActivity::class.java)
            startActivityForResult(intent, ANGGARAN_FOR_RESULT)
        }

        optionBulan.keyListener = null
        optionBulan.setOnClickListener {
            val bulans = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Bulan")
            builder.setItems(bulans, DialogInterface.OnClickListener { dialog, which ->
                optionBulan.setText(bulans[which])
                this.selectedBulan = bulans[which]
                getAnggaranBulan()
            })
            builder.show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANGGARAN_FOR_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                getAnggaran()
            }
        }
    }

    fun getAnggaran() {
        onLoad()
        this.disposable = webService.anggaran()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                anggaranRv.adapter = AnggaranAdapter(it, requireContext())
                anggaranRv.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {
                        val bulanRealisasi = it.anggaran[position].bulanRealisasi
                            .replace("\"", "")
                            .replace("[", "")
                            .replace("]", "")
                        val intent = Intent(context, AnggaranEditActivity::class.java)
                        intent.putExtra("id", it.anggaran[position].id)
                        intent.putExtra("kode", it.anggaran[position].kode)
                        intent.putExtra("kegiatan", it.anggaran[position].kegiatan)
                        intent.putExtra("anggaran", it.anggaran[position].anggaran)
                        intent.putExtra("volume", it.anggaran[position].volume)
                        intent.putExtra("tahun", it.anggaran[position].tahun)
                        intent.putExtra("bulan_realisasi", bulanRealisasi)
                        intent.putExtra("role_id", loginData.role_id)
                        startActivityForResult(intent, ANGGARAN_FOR_RESULT)
                    }
                })
                onFinishLoad()
            }, {
                var message = it.localizedMessage
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onFinishLoad()
            })
    }

    fun getAnggaranBulan() {
        onLoad()
        this.disposable1 = webService.anggaran(this.selectedBulan)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                anggaranRv.adapter = AnggaranAdapter(it, requireContext())
                anggaranRv.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {
                        val bulanRealisasi = it.anggaran[position].bulanRealisasi
                            .replace("\"", "")
                            .replace("[", "")
                            .replace("]", "")
                        val intent = Intent(context, AnggaranEditActivity::class.java)
                        intent.putExtra("id", it.anggaran[position].id)
                        intent.putExtra("kode", it.anggaran[position].kode)
                        intent.putExtra("kegiatan", it.anggaran[position].kegiatan)
                        intent.putExtra("anggaran", it.anggaran[position].anggaran)
                        intent.putExtra("volume", it.anggaran[position].volume)
                        intent.putExtra("tahun", it.anggaran[position].tahun)
                        intent.putExtra("bulan_realisasi", bulanRealisasi)
                        intent.putExtra("role_id", loginData.role_id)
                        startActivityForResult(intent, ANGGARAN_FOR_RESULT)
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
        anggaranRv.visibility = View.GONE
        pb.visibility = View.VISIBLE
    }

    fun onFinishLoad() {
        anggaranRv.visibility = View.VISIBLE
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
