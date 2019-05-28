package com.example.mysets.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.ui.main.adapters.PartsRecyclerViewAdapter
import com.example.mysets.view.model.searchBrickViewModel.SearchBrickViewModel
import com.example.mysets.view.model.searchBrickViewModel.SearchBrickViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_search_brick.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class SearchBrickFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val searchBrickViewModelFactory: SearchBrickViewModelFactory by instance()

    private lateinit var searchBrickViewModel: SearchBrickViewModel

    private lateinit var partsRecyclerViewAdapter: PartsRecyclerViewAdapter

    private val compositeDisposable = CompositeDisposable()

    private var pageCounter = 1

    private val pageSize = 20

    var forceSearch: ((Boolean) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeLegoViewModel()
        getSuccessRespond()
        getExceptionRespond()
        getErrorRespond()

        return inflater.inflate(R.layout.fragment_search_brick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView(rv_search_brick_id)
        partsRecyclerViewAdapter.endMarker = { marker ->
            if (marker) {
                pageCounter++
                Log.i("searchSet", "pageCounter increment = $pageCounter")
                forceSearch?.invoke(true)
            } else {
                forceSearch?.invoke(false)
            }
        }
        startFetching()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        partsRecyclerViewAdapter = PartsRecyclerViewAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val alphaAdapter = AlphaInAnimationAdapter(partsRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun startFetching() {
        val disposable = Observable.create(ObservableOnSubscribe<String> { subsciber ->
            search_brick_edit_text_id.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    subsciber.onNext(s.toString())
                    pageCounter = 1
                    Log.i("searchPart", "\npageCounter down to one")

                }
            })
        }).debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe {
                Log.i(
                    "searchPart",
                    " --------------------- start searching ------------------------"
                )
                forceSearch = { marker ->
                    if (marker) {
                        searchBrickViewModel.getBricksFromSearch(pageCounter, pageSize, it)
                    }
                }
                searchBrickViewModel.getBricksFromSearch(pageCounter, pageSize, it)
            }
        compositeDisposable.addAll(disposable)
    }

    private fun initializeLegoViewModel() {
        searchBrickViewModel =
            ViewModelProviders.of(this, searchBrickViewModelFactory)
                .get(SearchBrickViewModel::class.java)
    }

    private fun getSuccessRespond() {
        searchBrickViewModel.getPartsSuccess.observe(this, Observer {
            if (pageCounter == 1) {
                if (it.count < 30000) partsRecyclerViewAdapter.swapList(it.results.toMutableList())
                else partsRecyclerViewAdapter.clearList()
            } else {
                if (it.count < 30000) partsRecyclerViewAdapter.addToList(it.results.toMutableList())
            }
        })
    }

    private fun getErrorRespond() {
        searchBrickViewModel.getPartsError.observe(viewLifecycleOwner, Observer {
            Log.i("searchPart", "error ${it}")
        })
    }

    private fun getExceptionRespond() {
        searchBrickViewModel.getPartsException.observe(viewLifecycleOwner, Observer {
            Log.i("searchPart", "exception ${it.message}")
        })
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
