package com.application.afol.ui.fragments

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.models.LegoSet
import com.application.afol.ui.activities.DetailActivity
import com.application.afol.ui.adapters.LegoRecyclerViewAdapter
import com.application.afol.utility.DragManageAddAdapter
import com.application.afol.utility.gone
import com.application.afol.utility.show
import com.application.afol.vm.searchViewModel.SearchLegoViewModel
import com.application.afol.vm.searchViewModel.SearchLegoViewModelFactory
import com.google.android.material.snackbar.Snackbar
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

class SearchSetFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val searchLegoViewModelFactory: SearchLegoViewModelFactory by instance()

    private lateinit var searchLegoViewModel: SearchLegoViewModel
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 20

    private var pageCounter = 1

    var forceSearch: ((Boolean) -> Unit)? = null

    private var listOfFavoritesSets = MutableLiveData<List<LegoSet>>()

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
        legoRecyclerViewAdapter.selectedItem = { legoSet ->
            singleItemClickedReaction(legoSet)
        }
        addToMySetsOnSwipe()
        setupItemTouchHelper()
        legoRecyclerViewAdapter.endMarker = { marker ->
            if (marker) {
                pageCounter++
                Log.i("searchSet", "pageCounter increment = $pageCounter")
                forceSearch?.invoke(true)
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

    private fun singleItemClickedReaction(legoSet: LegoSet) {
        startDetailActivity(legoSet)
    }

    private fun startSearching(view: View) {
        val disposable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            view.search_edit_text_id.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    subscriber.onNext(s.toString())
                    pageCounter = 1
                    Log.i("searchSet", "\npageCounter down to one")
                }
            })
        }).debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribe {
                Log.i(
                    "searchSet",
                    " --------------------- start searching ------------------------"
                )
                forceSearch = { marker ->
                    if (marker) {
                        searchLegoViewModel.getLegoSetBySearch(it, pageSize, pageCounter)
                    }
                }
                searchLegoViewModel.getLegoSetBySearch(it, pageSize, pageCounter)
            }
        compositeDisposable.addAll(disposable)
    }


    private fun getSuccessRespond() {
        searchLegoViewModel.getSearchSuccess.observe(
            viewLifecycleOwner,
            Observer {
                if (pageCounter == 1) {
                    if (it.count < 14000) {
                        /*legoRecyclerViewAdapter.swapList(
                            it.results
                        )*/
                        user_instruction_view.gone()
                        legoRecyclerViewAdapter.listOfLegoSet = it.results
                        Log.i("searchSet", "pageCounter one swaplist")
                    } else {
                        legoRecyclerViewAdapter.clearList()
                        user_instruction_view.show()
                        Log.i("searchSet", "pageCounter one clear list")
                    }
                } else {
                    if (it.count < 14000) legoRecyclerViewAdapter.addToList(
                        it.results
                    ).also { user_instruction_view.gone() }
                    /*legoRecyclerViewAdapter.listOfLegoSet = it.results*/
                    Log.i("searchSet", "pageCounter = $pageCounter add to list")
                }
                legoRecyclerViewAdapter.notifyDataSetChanged()
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
        val alphaAdapter = AlphaInAnimationAdapter(legoRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun setupItemTouchHelper() {
        val callback = DragManageAddAdapter(
            legoRecyclerViewAdapter, context!!
        )
        ItemTouchHelper(callback).attachToRecyclerView(rv_search_id)
    }

    private fun addToMySetsOnSwipe() {
        legoRecyclerViewAdapter.swipedItem = { legoSet ->
            swipeAction(legoSet)
        }
    }

    private suspend fun getMySets() {
        searchLegoViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            listOfFavoritesSets.value = listOfMySets
        })
    }

    private fun swipeAction(legoSet: LegoSet) {
        if (!isSetInFavorites(legoSet)) {
            searchLegoViewModel.addToMySets(legoSet)
            val snackbar =
                Snackbar.make(
                    view!!,
                    legoSet.name + " " + getString(R.string.added_to_favorites_snack_bar_text),
                    Snackbar.LENGTH_LONG
                )
            snackbar.show()
        } else {
            val snackbar =
                Snackbar.make(
                    view!!,
                    legoSet.name + " " + getString(R.string.already_in_favorites_snack_bar_text),
                    Snackbar.LENGTH_LONG
                )
            snackbar.show()
        }
        legoRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun isSetInFavorites(legoSet: LegoSet): Boolean {
        listOfFavoritesSets.value?.forEach {
            if (it.set_num == legoSet.set_num) return true
        }
        return false
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }
}