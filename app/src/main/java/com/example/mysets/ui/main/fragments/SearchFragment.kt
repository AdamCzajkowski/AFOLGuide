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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.activities.DetailActivity
import com.example.mysets.ui.main.adapters.LegoRecyclerViewAdapter
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModel
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val searchLegoViewModelFactory: SearchLegoViewModelFactory by instance()

    private lateinit var searchLegoViewModel: SearchLegoViewModel
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        startSearching(view)
        initializeLegoViewModel()
        getSuccessRespond()
        getErrorRespond()
        getExceptionRespond()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRecyclerView(rv_search_id)
        legoRecyclerViewAdapter.selectedItem = {legoSet ->
            singleItemClickedReaction(legoSet)
        }
        super.onViewCreated(view, savedInstanceState)
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

    private fun singleItemClickedReaction(legoSet: LegoSet) {
        startDetailActivity(legoSet)
    }

    private fun startSearching(view: View) {
        val disposable = Observable.create(ObservableOnSubscribe<String> { subsciber ->
            view.search_edit_text_id.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    subsciber.onNext(s.toString())
                }
            })
        }).debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe { searchLegoViewModel.getLegoSetBySearch(it, 20) }

        compositeDisposable.addAll(disposable)
    }

    private fun getSuccessRespond() {
        searchLegoViewModel.getSearchSuccess.observe(
            viewLifecycleOwner,
            Observer {
                Log.i("search", it.count.toString())
                if(it.count < 14000) legoRecyclerViewAdapter.swapList(it.results)
                else {
                    legoRecyclerViewAdapter.clearList()
                }
            }
        )
    }

    private fun getErrorRespond() {
       searchLegoViewModel.getSearchError.observe(
           viewLifecycleOwner,
           Observer {
               Log.i("searchError", it)
           }
       )
    }

    private fun getExceptionRespond() {
        searchLegoViewModel.getSearchException.observe(
            viewLifecycleOwner,
            Observer {
                Log.i("searchException", it.message.toString())
            }
        )
    }

    private fun initializeLegoViewModel() {
        searchLegoViewModel =
            ViewModelProviders.of(this, searchLegoViewModelFactory)
                .get(SearchLegoViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = legoRecyclerViewAdapter
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }
}