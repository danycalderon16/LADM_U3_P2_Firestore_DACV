package mx.edu.ittepic.ladm_u3_p2_firestoredacv.ui.areas

import android.R
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.adapters.AreasAdapter
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Area
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.databinding.FragmentAreasBinding
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.utils.Util
import java.util.ArrayList

class AreasFragment : Fragment() {
    private var array = arrayListOf<String>()

    private var _binding: FragmentAreasBinding? = null

    private val binding get() = _binding!!

    var areas = ArrayList<Area>()
    lateinit var adapter : AreasAdapter
    lateinit var rv : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAreasBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val spinner: Spinner = binding.spinnerAreas

        array.clear()

        array.add("Seleccione una opción")
        array.add("Descripción")
        array.add("División")

        val aa = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, array)
        aa.setNotifyOnChange(true)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa

        rv = binding.rvList

        obtenerAreas("","")

        adapter = AreasAdapter(areas, object : AreasAdapter.onItemClickListener{
            override fun onItemClick(area: Area, i: Int) {
                if (i==0)
                    editArea(area)
                else
                    deleteArea(area)
            }
        })

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        binding.btnBuscar.setOnClickListener {
            if (spinner.selectedItemPosition==0){
                obtenerAreas("","")
                Toast.makeText(requireContext(), "Seleccione una opción", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var opc = spinner.selectedItem.toString().trim()
            if (opc=="División") opc = Util.DIVISION
            if (opc=="Descripción") opc = Util.DESCRIPCION
            val valor = binding.campoBuscar.text.toString().trim()
            if(valor.equals("")){
                Toast.makeText(requireContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show()
                obtenerAreas("","")
            }else
                obtenerAreas(valor,opc)

            adapter.notifyDataSetChanged()
        }

        return root
    }

    private fun deleteArea(area: Area) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Área")
            .setMessage("¿Está seguro de elimnar el área ${area.descripcion}?")
            .setPositiveButton("Sí") { d, i ->
                FirebaseFirestore.getInstance()
                    .collection(Util.AREA)
                    .document(area.idArea)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Área eliminada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        mensageError("No se pudo eliminar el área")
                    }
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("No",{d,i->d.dismiss()})
            .show()
    }

    private fun editArea(area: Area) {
        val builder = android.app.AlertDialog.Builder(requireContext())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater;
        val v = inflater.inflate(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.layout.dialog_edit_area,null)
        var ed_descr = v.findViewById<EditText>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.ed_descripcion_di)
        var ed_div = v.findViewById<EditText>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.ed_division_di)
        var ed_canEm = v.findViewById<EditText>(mx.edu.ittepic.ladm_u3_p2_firestoredacv.R.id.ed_cant_empleados_di)

        ed_descr.setText(area.descripcion)
        ed_div.setText(area.division)
        ed_canEm.setText(area.cantidadEmpleados.toString())

        builder.setView(v)
            .setPositiveButton("Editar",
                DialogInterface.OnClickListener { dialog, id ->
                    area.descripcion = ed_descr.text.toString()
                    area.division = ed_div.text.toString()
                    area.cantidadEmpleados = ed_canEm.text.toString().toInt()
                    FirebaseFirestore.getInstance()
                        .collection(Util.AREA)
                        .document(area.idArea)
                        .update(Util.DESCRIPCION,ed_descr.text.toString().trim(),
                            Util.DIVISION,ed_div.text.toString().trim(),
                            Util.CANTIDAD_EMPLEADOS,ed_canEm.text.toString().toInt())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Datos Actualizados", Toast.LENGTH_SHORT).show()
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            mensageError(it.message)
                        }
                })
            .setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        builder.create()
        builder.show()
    }

    override fun onStart() {
        super.onStart()
    }
    fun obtenerAreas(condicion:String,opcion:String){
        Log.i("Condicion - opcion",condicion+" - "+opcion)
        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .addSnapshotListener { query, error ->
                if (error!=null){
                    /**Si hubo un error*/
                    mensageError(error.message)
                    return@addSnapshotListener
                }
                areas.clear()
                for (documento in query!!){
                    Log.i("######92",documento.toString())
                    val area = Area()
                    area.idArea = documento.id
                    area.descripcion = documento.getString(Util.DESCRIPCION).toString().trim()
                    area.division = documento.getString(Util.DIVISION).toString().trim()
                    area.cantidadEmpleados = documento.get(Util.CANTIDAD_EMPLEADOS).toString().toInt()
                    if(opcion == Util.DIVISION && condicion == area.division)
                        areas.add(area)
                    if(opcion == Util.DESCRIPCION && condicion == area.descripcion)
                        areas.add(area)
                    if(opcion == "" || condicion == "")
                        areas.add(area)
                }
                adapter.notifyDataSetChanged()

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mensageError(message: String?) {
        AlertDialog.Builder(requireContext())
            .setTitle("ERROR")
            .setMessage(message)
            .show()
    }
}