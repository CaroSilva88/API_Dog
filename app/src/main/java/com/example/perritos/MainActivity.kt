package com.example.perritos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perritos.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImage = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerview()
    }

    private fun initRecyclerview() {
        adapter = DogAdapter(dogImage)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }


    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
                //conversion de Json al response para inflar en la vista
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //las corrutinas sirven para crear un hilo secundario por el cual se va a hacer la llamada a internet
    //de manera asincrona, de esta manera el hilo principal no colapsa

    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {

            val call = getRetrofit().create(APIService::class.java).getDogsByBreeds(query)
            val puppies : DogsResponse? = call.body()
            runOnUiThread{
            if (call.isSuccessful){
                val  images:List<String> = puppies?.images ?: emptyList()
                dogImage.clear()
                dogImage.addAll(images)
                adapter.notifyDataSetChanged()

            }else{
                showError()
            }
        }
    }

    }
    private fun showError(){
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean{
        if (!query.isNullOrEmpty()){
            searchByName(query.toLowerCase())
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean{
        return true
    }

}
