package com.example.mysets.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.network.Result
import com.example.mysets.ui.main.LegoRecyclerViewAdapter
import com.example.mysets.view.model.LegoViewModel
import com.example.mysets.view.model.LegoViewModelFactory
import com.example.mysets.view.model.PageViewModel
import kotlinx.android.synthetic.main.fragment_lego_sets.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment(), KodeinAware {

    companion object {
        @JvmStatic
        fun newInstance(): PlaceholderFragment {
            return PlaceholderFragment()
        }
    }

    override val kodein: Kodein by kodein()

    private val legoViewModelFactory: LegoViewModelFactory by instance()

    private lateinit var legoViewModel: LegoViewModel
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lego_sets, container, false)
        initializeLegoViewModel()
        //getSuccessRespond(root)
        //getLego()
        //Log.i("test", "ccccccc")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRecyclerView()
        getSuccessRespond(view)
        getErrorRespond(view)
        getExceptionRespond(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeLegoViewModel() {
        legoViewModel =
            ViewModelProviders.of(this, legoViewModelFactory).get(LegoViewModel::class.java)
    }

    private fun initializeRecyclerView() {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        rv_lego_sets_id.layoutManager = LinearLayoutManager(context)
        rv_lego_sets_id.adapter = legoRecyclerViewAdapter
    }

    private fun getSuccessRespond(view: View) {
        legoViewModel.getLegoSuccess().observe(this, Observer {
            Log.i("test", "success")
        })
    }

    private fun getErrorRespond(view: View) {
        legoViewModel.getLegoError().observe(this, Observer {
            Log.i("test", "error")
        })
    }

    private fun getExceptionRespond(view: View) {
        legoViewModel.getLegoException().observe(this, Observer {
            Log.i("test", "exception")
        })
    }

    private fun getLego() {
        val lego1 = LegoSet(
            "44444",
            "title",
            2000,
            1111,
            "https://rebrickable.com/media/sets/8421-1.jpg",
            "https://rebrickable.com/sets/42070-1/6x6-all-terrain-tow-truck/"
        )
        val list = mutableListOf(lego1)
        legoRecyclerViewAdapter.swapList(list)
    }
}
