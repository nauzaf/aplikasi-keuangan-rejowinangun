package web.id.anproject.rekapkeuangan


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_anggaran.*
import web.id.anproject.rekapkeuangan.adapter.AnggaranAdapter

class AnggaranFragment : Fragment() {

    private val webService: WebService = WebService.create()
    private lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anggaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anggaranRv.layoutManager = LinearLayoutManager(activity)
        onLoad()
        this.disposable = webService.anggaran()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                anggaranRv.adapter = AnggaranAdapter(it, requireContext())
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
        anggaranRv.visibility = View.GONE
        pb.visibility = View.VISIBLE
    }

    fun onFinishLoad() {
        anggaranRv.visibility = View.VISIBLE
        pb.visibility = View.GONE
    }

}
