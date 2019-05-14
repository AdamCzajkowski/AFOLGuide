package com.example.mysets.ui.main.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.activities.DetailActivity
import com.example.mysets.ui.main.adapters.LegoRecyclerViewAdapter
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModel
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_sets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

class MySetsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val mySetsViewModelFactory: MySetsViewModelFactory by instance()

    private lateinit var mySetsViewModel: MySetsViewModel
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_sets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeLegoViewModel()
        initializeRecyclerView(rv_my_sets_id)
        getAllMySetsFromDatabase()
        legoRecyclerViewAdapter.selectedItem = {
            singleItemClickedReaction(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun getAllMySetsFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            getAllMySets()
        }
    }

    private fun initializeLegoViewModel() {
        mySetsViewModel =
            ViewModelProviders.of(this, mySetsViewModelFactory).get(MySetsViewModel::class.java)
    }

    private fun singleItemClickedReaction(legoSet: LegoSet) {
        startDetailActivity(legoSet)
        //startWithAnimationActivity(1, legoSet)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = legoRecyclerViewAdapter
    }

    private suspend fun getAllMySets() {
        mySetsViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            legoRecyclerViewAdapter.swapList(listOfMySets)
        })
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }

    /*private fun startWithAnimationActivity(resultCode: Int, legoSet: LegoSet) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, Pair.create(view, getString(R.string.lego_image_transition)))
        AppCompatActivity().startActivityFromFragment(
           this,
           DetailActivity.getIntent(context!!, legoSet),
           resultCode,
           options.toBundle())
*/
    /*startActivity(DetailActivity.getIntent(context!!, legoSet), ActivityOptionsCompat.makeSceneTransitionAnimation(
         this, view!!, getString(R.string.lego_image_transition)
     ).toBundle())*/
}

