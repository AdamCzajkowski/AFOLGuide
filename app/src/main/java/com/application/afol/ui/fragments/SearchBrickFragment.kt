package com.application.afol.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.ui.adapters.PartsRecyclerViewAdapter
import com.application.afol.utility.doNothing
import com.application.afol.utility.setVisibility
import com.application.afol.vm.searchBrickViewModel.SearchBrickViewModel
import com.application.afol.vm.searchBrickViewModel.SearchBrickViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_search_brick.*
import kotlinx.android.synthetic.main.fragment_search_brick.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

private const val TIMEOUT_DEBOUNCE = 200L
private const val PAGE_SIZE = 20
private const val MAX_AMOUNT_PART_LOADED = 30000

class SearchBrickFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private lateinit var searchBrickViewModel: SearchBrickViewModel

    private lateinit var partsRecyclerViewAdapter: PartsRecyclerViewAdapter

    private val searchBrickViewModelFactory: SearchBrickViewModelFactory by instance()

    private val compositeDisposable = CompositeDisposable()

    private var pageCounter = 1

    private var forceSearch: ((Boolean) -> Unit)? = null

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
        openBrickURL()
        partsRecyclerViewAdapter.endMarker = { marker ->
            if (marker) {
                pageCounter++
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

    private fun toggleNoConnectionScreen(isConnection: Boolean) {
        if (isConnection) {
            no_connection_view.setVisibility(false)
            user_instruction_view.setVisibility(true)
        } else {
            no_connection_view.setVisibility(true)
            user_instruction_view.setVisibility(false)
        }
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        partsRecyclerViewAdapter = PartsRecyclerViewAdapter()
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(partsRecyclerViewAdapter))
        }
    }

    private fun startFetching() {
        val disposable = Observable.create(ObservableOnSubscribe<String> { subsciber ->
            search_brick_edit_text_id.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    doNothing
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    doNothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    subsciber.onNext(s.toString())
                    pageCounter = 1
                }
            })
        }).debounce(TIMEOUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe {
                with(searchBrickViewModel) {
                    forceSearch = { marker ->
                        if (marker) getBricksFromSearch(pageCounter, PAGE_SIZE, it)
                    }
                    getBricksFromSearch(pageCounter, PAGE_SIZE, it)
                }
            }
        compositeDisposable.addAll(disposable)
    }

    private fun initializeLegoViewModel() {
        searchBrickViewModel =
            ViewModelProviders.of(this, searchBrickViewModelFactory)
                .get(SearchBrickViewModel::class.java)
    }

    private fun openBrickURL() {
        partsRecyclerViewAdapter.partSelectedURL = {
            val rebrickableURL = Intent(Intent.ACTION_VIEW)
            rebrickableURL.data = Uri.parse(it)
            startActivity(rebrickableURL, Bundle())
        }
    }

    private fun getSuccessRespond() =
        searchBrickViewModel.getPartsSuccess.observe(this, Observer {
            toggleNoConnectionScreen(true)
            if (search_brick_edit_text_id.text?.length != 0) {
                if (it.count == 0) {
                    view!!.no_results_view.setVisibility(true)
                    user_instruction_view.setVisibility(false)
                    partsRecyclerViewAdapter.clearList()
                } else {
                    view!!.no_results_view.setVisibility(false)
                    if (pageCounter == 1) {
                        if (it.count < MAX_AMOUNT_PART_LOADED) partsRecyclerViewAdapter.listOfParts =
                            it.results.toMutableList()
                                .also { user_instruction_view.setVisibility(false) }
                        else partsRecyclerViewAdapter.clearList().also {
                            view!!.no_results_view.setVisibility(true)
                            user_instruction_view.setVisibility(false)
                        }
                    } else {
                        if (it.count < MAX_AMOUNT_PART_LOADED) partsRecyclerViewAdapter.addToList(it.results.toMutableList()).also {
                            user_instruction_view.setVisibility(
                                false
                            )
                        }
                    }
                    partsRecyclerViewAdapter.notifyDataSetChanged()
                }
            } else {
                user_instruction_view.setVisibility(true)
                view!!.no_results_view.setVisibility(false)
                partsRecyclerViewAdapter.clearList()
            }
        })

    private fun getErrorRespond() =
        searchBrickViewModel.getPartsError.observe(viewLifecycleOwner, Observer {
            doNothing
        })

    private fun getExceptionRespond() =
        searchBrickViewModel.getPartsException.observe(viewLifecycleOwner, Observer {
            toggleNoConnectionScreen(false)
        })

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(constraint_brick_search.windowToken, 0)
    }
}
