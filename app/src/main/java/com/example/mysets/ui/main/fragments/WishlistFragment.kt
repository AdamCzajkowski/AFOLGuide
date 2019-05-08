package com.example.mysets.ui.main.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.DetailActivity
import com.example.mysets.ui.main.LegoRecyclerViewAdapter
import com.example.mysets.view.model.wishlistViewModel.WishlistViewModel
import com.example.mysets.view.model.wishlistViewModel.WishlistViewModelFactory
import kotlinx.android.synthetic.main.fragment_wishlist.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

class WishlistFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()

    private val wishlistViewModelFactory: WishlistViewModelFactory by instance()

    private lateinit var wishlistViewModel: WishlistViewModel

    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeLegoViewModel()
        initializeRecyclerView(rv_wishlist_id)
        getAllWishlistSets()
        legoRecyclerViewAdapter.selectedItem = {
            singleItemClickedReaction(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun singleItemClickedReaction(legoSet: LegoSet) {
       startDetailActivity(legoSet)
    }

    private fun initializeLegoViewModel() {
        wishlistViewModel =
            ViewModelProviders.of(this, wishlistViewModelFactory).get(WishlistViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = legoRecyclerViewAdapter
    }

    private fun getAllWishlistSets() {
        wishlistViewModel.getAllMySets().observe(this, Observer {
            legoRecyclerViewAdapter.swapList(it)
        })
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }
}
