package mx.edu.ittepic.ladm_u3_p2_firestoredacv.ui.subdeps

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.adapters.SubdepAdapter
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Area
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.AreaSubp
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.AreasID
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Subdepartamento
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
    var subdeps = ArrayList<Subdepartamento>()
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

        binding.btnBuscar.setOnClickListener {
            if (spinner.selectedItemPosition==0){
                Toast.makeText(requireContext(), "Seleccione una opción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var opc = spinner.selectedItem.toString().trim()
            if (opc=="Edificio") opc = Util.IDEDIFICIO
            if (opc=="Área") opc = Util.DESCRIPCION
            if (opc=="División") opc = Util.DIVISION
            val valor = binding.campoBuscar.text.toString().trim()
            if(valor.equals("")){
                Toast.makeText(requireContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show()
                obtenerAreaSub("","")
            }else
                obtenerAreaSub(valor,opc)

            Log.i("96",areaSubList.size.toString())

            adapter.notifyDataSetChanged()
        }

        return root
    }

    private fun obtenerDepartamentos() {

    }

    private fun obtenerAreaSub(condicion: String, opcion: String) {
        Log.i("Condicion - opcion",condicion+" - "+opcion)
        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .get()
            .addOnSuccessListener { results ->
                areaSubList.clear()
                for(document in results){
                    val areaSub = AreaSubp()
                    Log.i("######90",document.toString())
                    FirebaseFirestore.getInstance()
                        .collection(Util.AREA)
                        .document(document.id)
                        .collection(Util.SUBDEPARTAMENTO)
                        .get()
                        .addOnSuccessListener { result->
                            for (doc in result) {
                                Log.i("######127", doc.toString())
                                Log.i("######128", document.toString())
                                areaSub.idArea = doc.getString(Util.IDAREA).toString()
                                areaSub.idSubdept = doc.id
                                areaSub.division = document.getString(Util.DIVISION).toString()
                                areaSub.descripcion = doc.getString(Util.DESCRIPCION).toString()
                                areaSub.idEdificio = doc.getString(Util.IDEDIFICIO).toString()
                                areaSub.piso = doc.get(Util.PISO).toString().toInt()
                                if (opcion == Util.DIVISION && areaSub.division == condicion) {
                                    areaSubList.add(areaSub)
                                }
                                if (opcion == Util.IDEDIFICIO && areaSub.idEdificio == condicion) {
                                    areaSubList.add(areaSub)
                                }
                                if (opcion == Util.DESCRIPCION && areaSub.descripcion == condicion) {
                                    areaSubList.add(areaSub)
                                }
                                if (opcion == "" || condicion == ""){
                                    areaSubList.add(areaSub)
                                }
                                Log.i("146",areaSub.toString())
                                adapter.notifyDataSetChanged()

                            }
                        }
                }
            }
    }

    private fun editAreaSub(areaSub: AreaSubp) {
        val builder = AlertDialog.Builder(requireContext())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater;
        val v = inflater.inflate(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.layout.dialog_edit_subdep,null)
        var ed_edif = v.findViewById<EditText>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.ed_id_edificio_edit)
        var ed_piso = v.findViewById<EditText>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.ed_piso_edit)
        var spinnerEd = v.findViewById<Spinner>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.spinner_ed)


        var areas = arrayListOf<String>()
        var areasID = ArrayList<AreasID>()

        areas.add("Seleccione un Área")
        lateinit var aa : ArrayAdapter<String>

        areas.clear()
        areasID.clear()
        areas.add("Seleccione un área")
        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val descripcion = document.data.get(Util.DESCRIPCION).toString()
                    areas.add(descripcion)
                }
                aa.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }



        aa = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, areas)
        aa.setNotifyOnChange(true)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinnerEd.adapter = aa

        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .document(areaSub.idArea)
            .collection(Util.SUBDEPARTAMENTO)
            .document(areaSub.idSubdept)
            .get()
            .addOnSuccessListener { snapshot ->
                val desc = snapshot.get(Util.DESCRIPCION).toString()
                ed_edif.setText(snapshot.get(Util.IDEDIFICIO).toString())
                ed_piso.setText(snapshot.get(Util.PISO).toString())

                (1..areas.size-1).forEach {
                   if (areas[it]==areaSub.descripcion) {
                       Log.i("211",areas[it]+" - "+areaSub.descripcion)
                       spinnerEd.setSelection(it)
                   }
                }
            }

        builder.setView(v)
            .setPositiveButton("Editar",
                DialogInterface.OnClickListener { dialog, id ->
                    FirebaseFirestore.getInstance()
                        .collection(Util.AREA)
                        .document(areaSub.idArea)
                        .collection(Util.SUBDEPARTAMENTO)
                        .document(areaSub.idSubdept)
                        .update( Util.IDEDIFICIO,ed_edif.text.toString(),
                            Util.PISO,ed_piso.text.toString().toInt(),
                            Util.DESCRIPCION,spinnerEd.selectedItem.toString())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Se actualiao el subdepartamento con éxito", Toast.LENGTH_SHORT).show()
                        }

                })
            .setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        builder.create()
        builder.show()

    }
    private fun deleteAreaSub(areaSub: AreaSubp) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}