package com.example.vaccination

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccination.databinding.ActivityVaccinationListBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class VaccinationListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVaccinationListBinding
    lateinit var adapter: VaccinationAdapter
    val TAG = "VaccinationListActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVaccinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var vaccineList = listOf<VaccinationInfo>()
        val vaccineApi = RetrofitHelper.getInstance().create(Covid19Service::class.java)
        val vaccineCall = vaccineApi.getVaccinations(10)




        vaccineCall.enqueue(object : Callback<List<VaccinationInfo>> {
            override fun onResponse(
                call: Call<List<VaccinationInfo>>,
                response: Response<List<VaccinationInfo>>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                vaccineList = response.body() ?: listOf<VaccinationInfo>()
                val adapter = VaccinationAdapter(vaccineList)
                binding.recyclerViewVaccinations.adapter = adapter
                binding.recyclerViewVaccinations.layoutManager = LinearLayoutManager(this@VaccinationListActivity)

                // working with maps examples
                val country1 = vaccineList[0]
                val firstDay = country1.timeline.firstKey()
                val lastDay = country1.timeline.lastKey()
                country1.timeline.get(firstDay)

                country1.timeline.toList().sortedBy {
                    it.second
                }[0]
            }

            override fun onFailure(call: Call<List<VaccinationInfo>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        }
        )

        adapter = VaccinationAdapter(vaccineList)
        binding.recyclerViewVaccinations.adapter = adapter
        binding.recyclerViewVaccinations.layoutManager = LinearLayoutManager(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.vaccination_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItem_menu_name -> {
                sortByName()
                true
            }
            R.id.menuItem_menu_totalvaxxed   -> {
                sortByTotal()
                true
            }
            R.id.menuItem_menu_mostvaxxedin10days -> {
                sortByLast10()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun sortByTotal() {

        Toast.makeText(this, "Sorted total vaxxed", Toast.LENGTH_SHORT).show()
        adapter.dataSet = adapter.dataSet.sortedBy {
            val lastKey = it.timeline.lastKey()
            it.timeline[lastKey]
        }
        adapter.notifyDataSetChanged()

    }
    fun sortByName() {
        Toast.makeText(this, "Sorted by Name", Toast.LENGTH_SHORT).show()
        adapter.dataSet = adapter.dataSet.sortedBy {
            it.country
        }
        adapter.notifyDataSetChanged()

    }
    fun sortByLast10() {

        Toast.makeText(this, "Sorted by largest last 10 day increase", Toast.LENGTH_SHORT).show()
        adapter.dataSet = adapter.dataSet.sortedByDescending {
            val lastKey = it.timeline.lastKey()
            val firstKey = it.timeline.firstKey()
            val difference = firstKey.toInt() - lastKey.toInt()
            it.timeline[lastKey]
            it.timeline[firstKey]
        }
        adapter.notifyDataSetChanged()
    }
}