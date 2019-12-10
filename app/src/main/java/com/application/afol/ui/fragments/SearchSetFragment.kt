package com.application.afol.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.models.LegoSet
import com.application.afol.ui.activities.DetailActivity
import com.application.afol.ui.adapters.LegoRecyclerViewAdapter
import com.application.afol.utility.doNothing
import com.application.afol.utility.setVisibility
import com.application.afol.utility.showSnackbar
import com.application.afol.vm.searchViewModel.SearchLegoViewModel
import com.application.afol.vm.searchViewModel.SearchLegoViewModelFactory
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

private const val MAX_VALUE_SEARCHED_ITEMS = 14000
private const val TIMEOUT_DEBOUNCE = 200L

class SearchSetFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private lateinit var searchLegoViewModel: SearchLegoViewModel

    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    private val searchLegoViewModelFactory: SearchLegoViewModelFactory by instance()

    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 20

    private var pageCounter = 1

    private var listOfFavoritesSets = MutableLiveData<List<LegoSet>>()

    private var forceSearch: ((Boolean) -> Unit)? = null

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
        GlobalScope.launch(Dispatchers.Main) { getMySets() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRecyclerView(rv_search_id)
        legoRecyclerViewAdapter.selectedItem = { singleItemClickedReaction(it) }
        addToMySetsOnAction()

        legoRecyclerViewAdapter.endMarker = { marker ->
            if (marker) {
                pageCounter++.also { forceSearch?.invoke(true) }
            } else {
                forceSearch?.invoke(false)
            }
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

    private fun startSearching(view: View) {
        val disposable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            view.search_edit_text_id.addTextChangedListener(object : TextWatcher {
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

                override fun onTextChanged(
                    search: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    subscriber.onNext(search.toString())
                    pageCounter = 1
                }
            })
        }).debounce(TIMEOUT_DEBOUNCE, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe {
                forceSearch = { marker ->
                    if (marker) {
                        searchLegoViewModel.getLegoSetBySearch(it, pageSize, pageCounter)
                    }
                }
                searchLegoViewModel.getLegoSetBySearch(it, pageSize, pageCounter)
            }
        compositeDisposable.addAll(disposable)
    }

    private fun getSuccessRespond() =
        searchLegoViewModel.getSearchSuccess.observe(
            viewLifecycleOwner,
            Observer {
                with(user_instruction_view) {
                    if (pageCounter == 1) {
                        if (it.count < MAX_VALUE_SEARCHED_ITEMS) {
                            setVisibility(false)
                            legoRecyclerViewAdapter.listOfLegoSet = it.results
                        } else {
                            setVisibility(true)
                            legoRecyclerViewAdapter.clearList()
                        }

                    } else {
                        if (it.count < MAX_VALUE_SEARCHED_ITEMS) legoRecyclerViewAdapter.addToList(
                            it.results
                        ).also { setVisibility(false) }
                    }
                    legoRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        )

    private fun getErrorRespond() =
        searchLegoViewModel.getSearchError.observe(
            viewLifecycleOwner,
            Observer {
                doNothing
            }
        )


    private fun getExceptionRespond() =
        searchLegoViewModel.getSearchException.observe(
            viewLifecycleOwner,
            Observer {
                doNothing
            }
        )

    private fun startDetailActivity(legoSet: LegoSet) =
        startActivity(DetailActivity.getIntent(requireContext(), legoSet))

    private fun singleItemClickedReaction(legoSet: LegoSet) =
        startDetailActivity(legoSet)

    private fun initializeLegoViewModel() {
        searchLegoViewModel =
            ViewModelProviders.of(this, searchLegoViewModelFactory)
                .get(SearchLegoViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        val alphaAdapter = AlphaInAnimationAdapter(legoRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun addToMySetsOnAction() =
        with(legoRecyclerViewAdapter) {
            favoriteIconItem = { legoSet ->
                if (!isSetInFavorites(legoSet)) addToFavorites(legoSet)
                else removeFromFavorites(legoSet)
                notifyDataSetChanged()
                GlobalScope.launch(Dispatchers.Main) { getMySets() }
            }
        }

    private suspend fun getMySets() =
        searchLegoViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            listOfFavoritesSets.value = listOfMySets
            legoRecyclerViewAdapter.listOfFavoritesLegoSet =
                listOfFavoritesSets.value?.toMutableList()
        })

    private fun removeFromFavorites(legoSet: LegoSet) {
        if (isSetInFavorites(legoSet)) {
            listOfFavoritesSets.value?.find { searchedLegoSet -> searchedLegoSet.set_num == legoSet.set_num }
                ?.let { searchLegoViewModel.removeFromMySets(it) }
            with(legoRecyclerViewAdapter.listOfFavoritesLegoSet) {
                this?.find { searchedLegoSet -> searchedLegoSet.set_num == legoSet.set_num }?.let {
                    this.remove(it)
                }
            }
            constraint_search_fragment.showSnackbar(legoSet.name + " " + getString(R.string.deleted_item_snack_bar_text))
        } else doNothing
    }

    private fun addToFavorites(legoSet: LegoSet) {
        if (!isSetInFavorites(legoSet)) {
            searchLegoViewModel.addToMySets(legoSet)
            legoRecyclerViewAdapter.listOfFavoritesLegoSet?.add(legoSet)
            constraint_search_fragment.showSnackbar(legoSet.name + " " + getString(R.string.added_to_favorites_snack_bar_text))
        } else doNothing
    }

    private fun isSetInFavorites(legoSet: LegoSet): Boolean {
        listOfFavoritesSets.value?.forEach {
            if (it.set_num == legoSet.set_num) return true
        }
        return false
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
