package com.example.mysets.ui.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.ThemesResult
import com.example.mysets.ui.main.adapters.ThemesRecyclerViewAdapter
import com.example.mysets.view.model.themesViewModel.ThemesViewModel
import com.example.mysets.view.model.themesViewModel.ThemesViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_themes.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

class ThemesFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val themesViewModelFactory: ThemesViewModelFactory by instance()

    private lateinit var themesViewModel: ThemesViewModel

    private lateinit var themesRecyclerViewAdapter: ThemesRecyclerViewAdapter

    private val pageSize = 700

    private var page = 1

    private var listOfThemes = mutableListOf<ThemesResult.Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_themes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeLegoViewModel()
        initializeRecyclerView(rv_themes)
        getErrorRespond()
        getExceptionRespond()
        getSuccessRespond()
        getThemes(page, pageSize)
        selectedItem()
        searchTheme()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun selectedItem() {
        themesRecyclerViewAdapter.selectedItem = {
            Toast.makeText(context, "${it.name} selected", Toast.LENGTH_LONG).show()
        }
    }

    private fun searchTheme() {
        search_edit_text_theme_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = listOfThemes.filter { it.name.contains(s.toString()) }
                themesRecyclerViewAdapter.swapList(filteredList.toMutableList())
            }
        })
    }

    private fun initializeLegoViewModel() {
        themesViewModel =
            ViewModelProviders.of(this, themesViewModelFactory).get(ThemesViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        themesRecyclerViewAdapter = ThemesRecyclerViewAdapter()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val alphaAdapter = AlphaInAnimationAdapter(themesRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }


    private fun getSuccessRespond() {
        themesViewModel.getThemesSuccess.observe(viewLifecycleOwner,
            Observer {
                listOfThemes = it.results.toMutableList()
                listOfThemes.sortBy { it.name }
                themesRecyclerViewAdapter.swapList(listOfThemes)
            })
    }

    private fun getErrorRespond() {
        themesViewModel.getThemesError.observe(this,
            Observer {
                Log.i("themeError", it)
            })
    }

    private fun getExceptionRespond() {
        themesViewModel.getThemesException.observe(this,
            Observer {
                Log.i("themeException", it.toString())
            })
    }

    private fun getThemes(page: Int, pageSize: Int) {
        themesViewModel.getThemes(page, pageSize)
    }
}