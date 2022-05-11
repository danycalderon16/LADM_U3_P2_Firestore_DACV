package mx.edu.ittepic.ladm_u3_p2_firestoredacv.ui.subdeps

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.adapters.SubdepAdapter
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.AreaSubp
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.databinding.FragmentSubdepsBinding
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.utils.Util
import java.util.ArrayList
import java.util.zip.ZipEntry

class SubdepsFragment : Fragment() {
    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/
    private var array = arrayListOf<String>()

    private var _binding: FragmentSubdepsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var areaSubList = ArrayList<AreaSubp>()
    lateinit var adapter : SubdepAdapter
    lateinit var rv : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentSubdepsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val spinner: Spinner = binding.spinnerSubdep

        array.add("Seleccione una opción")
        array.add("Edificio")
        array.add("Área")
        array.add("División")

        val aa = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, array)
        aa.setNotifyOnChange(true)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner.adapter = aa
        rv = binding.rvList

        obtenerAreaSub("","")

        adapter = SubdepAdapter(areaSubList, object : SubdepAdapter.onItemClickListenr{
            override fun onItemClick(areaSub: AreaSubp, i: Int) {
                if (i==0)
                    editAreaSub(areaSub)
                else
                    deleteAreaSub(areaSub)
            }

        })

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        return root
    }

    private fun obtenerAreaSub(condicion: String, opcion: String) {
        Log.i("Condicion - opcion",condicion+" - "+opcion)
        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .get()
            .addOnSuccessListener { results ->
                for(document in results){
                    val areaSub = AreaSubp()
                    Log.i("######90",document.toString())
                    FirebaseFirestore.getInstance()
                        .collection(Util.AREA)
                        .document(document.id)
                        .collection(Util.SUBDEPARTAMENTO)
                        .get()
                        .addOnSuccessListener { result->
                            for (doc in result){
                                Log.i("######98",doc.toString())
                                Log.i("######99",document.toString())
                                areaSub.division = document.getString(Util.DIVISION).toString()
                                areaSub.descripcion = doc.getString(Util.IDAREA).toString()
                                areaSub.idEdificio = doc.getString(Util.IDEDIFICIO).toString()
                                areaSub.piso = doc.get(Util.PISO).toString().toInt()
                                areaSubList.add(areaSub)
                                adapter.notifyDataSetChanged()
                                Log.i("105",areaSubList.size.toString())

                            }
                        }
                }
                Log.i("109",areaSubList.size.toString())
            }
    }

    private fun editAreaSub(areaSub: AreaSubp) {

    }
    private fun deleteAreaSub(areaSub: AreaSubp) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}