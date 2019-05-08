package com.example.mysets.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.mysets.R
import com.example.mysets.databinding.ActivityDetailBinding
import com.example.mysets.models.LegoSet
import com.example.mysets.view.model.detailViewModel.DetailViewModel
import com.example.mysets.view.model.detailViewModel.DetailViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_wishlist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class DetailActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val LEGO_SET = "legoSet"
        fun getIntent(context: Context, legoSet: LegoSet): Intent =
            Intent(
                context,
                DetailActivity::class.java
            )
                .putExtra(LEGO_SET, legoSet)
    }

    override val kodein: Kodein by kodein()

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModelFactory: DetailViewModelFactory by instance()

    private lateinit var detailViewModel: DetailViewModel

    private val legoSet: LegoSet by lazy { intent.getParcelableExtra<LegoSet>(LEGO_SET) }

    var isInWishlistMarker: ((Boolean) -> Unit)? = null

    var isInMySetsMarker: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
        setUpDetailsToolbar()
        setUpDetailsCollapsingToolbar()
        bindView()
        checkIsInAnyDatabase()
        initializeMySetsFAB()
        initializeWishlistFAB()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setUpDetailsToolbar() {
        setSupportActionBar(lego_details_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setUpDetailsCollapsingToolbar() {
        with(lego_details_collapsing_toolbar_id) {
            setCollapsedTitleTextColor(android.graphics.Color.WHITE)
            setExpandedTitleColor(android.graphics.Color.WHITE)
        }
    }

    private fun checkIsInAnyDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            isInMySets()
            isInWishlist()
        }
    }

    private suspend fun isInMySets() {
        detailViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            listOfMySets?.forEach {
                if (it.set_num == legoSet.set_num) {
                    isInMySetsMarker?.invoke(true)
                    return@Observer
                }
            }
            isInMySetsMarker?.invoke(false)
            return@Observer
        })
    }

    private suspend fun isInWishlist() {
        detailViewModel.getListOfWishlist().observe(this, Observer { listOfWishlist ->
            listOfWishlist?.forEach {
                if (it.set_num == legoSet.set_num) {
                    isInWishlistMarker?.invoke(true)
                    return@Observer
                }
            }
            isInWishlistMarker?.invoke(false)
            return@Observer
        })
    }

    private fun bindView() {
        binding.legoSet = legoSet
    }


    private fun toastMessage(message: Int) {
        Toast.makeText(
            this,
            "${legoSet.name} ${getString(message)}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setImageButtonIcon(imageButton: FloatingActionButton, drawable: Int) {
        imageButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                drawable
            )
        )
    }

    private fun initializeMySetsFAB() {
        isInMySetsMarker = { marker ->
            if(marker) {
                setImageButtonIcon(my_sets_button, R.drawable.mine_icon)
            } else {
                setImageButtonIcon(my_sets_button, R.drawable.un_mine_icon)
            }
            my_sets_button.setOnClickListener {
                if(marker) {
                    detailViewModel.removeFromMySets(legoSet)
                    setImageButtonIcon(my_sets_button, R.drawable.un_mine_icon)
                    toastMessage(R.string.removed_from_my_sets)
                } else {
                    detailViewModel.addToMySets(legoSet)
                    setImageButtonIcon(my_sets_button, R.drawable.mine_icon)
                    toastMessage(R.string.added_to_my_sets)
                }
            }
        }
    }

    private fun initializeWishlistFAB() {
        isInWishlistMarker = { marker ->
            if(marker) {
                setImageButtonIcon(wishlist_button, R.drawable.wishlist_added_icon)
            } else {
                setImageButtonIcon(wishlist_button, R.drawable.wishlist_unadded_icon)
            }
            wishlist_button.setOnClickListener {
                if(marker) {
                    detailViewModel.removeFromWishlist(legoSet)
                    setImageButtonIcon(wishlist_button, R.drawable.wishlist_unadded_icon)
                    toastMessage(R.string.removed_from_wishlist)
                } else {
                    detailViewModel.addToWishlist(legoSet)
                    setImageButtonIcon(wishlist_button, R.drawable.wishlist_added_icon)
                    toastMessage(R.string.added_to_wishlist)
                }
            }
        }
    }
/*
    private fun setDrawableImageButton(
        marker: Boolean,
        imageButton: FloatingActionButton,
        drawableON: Int,
        drawableOFF: Int
    ) {
        if (marker) {
            setImageButtonIcon(imageButton, drawableON)
        } else {
            setImageButtonIcon(imageButton, drawableOFF)
        }
    }

    private fun initializeMySetsButton(marker: Boolean) {
        Log.i("test", "work fine!")
        setDrawableImageButton(
            marker,
            my_sets_button,
            R.drawable.mine_icon,
            R.drawable.un_mine_icon
        )
    }

    private fun initializeWishlistButton(marker: Boolean) {
        setDrawableImageButton(
            marker,
            wishlist_button,
            R.drawable.wishlist_added_icon,
            R.drawable.wishlist_unadded_icon
        )
    }

    private fun onClickMySetsButtonReaction(marker: Boolean) {
        if (marker) {
            detailViewModel.removeFromMySets(legoSet)
            toastMessage(R.string.removed_from_my_sets)
        } else {
            detailViewModel.addToMySets(legoSet)
            toastMessage(R.string.added_to_my_sets)
        }
    }

    private fun onClickWishlistButtonReaction(marker: Boolean) {
        if (marker) {
            detailViewModel.removeFromWishlist(legoSet)
            setImageButtonIcon(wishlist_button, R.drawable.wishlist_unadded_icon)
            toastMessage(R.string.removed_from_wishlist)
        } else {
            detailViewModel.addToWishlist(legoSet)
            setImageButtonIcon(wishlist_button, R.drawable.wishlist_added_icon)
            toastMessage(R.string.added_to_wishlist)
        }
    }*/
}