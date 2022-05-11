package mx.edu.ittepic.ladm_u3_p2_firestoredacv.ui.add

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Area
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.AreasID
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Subdepartamento
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.databinding.FragmentAddBinding
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.utils.Util
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.utils.Util.*

class AddFragment : Fragment() {
    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/

    private var _binding: FragmentAddBinding? = null

    var areasID = ArrayList<AreasID>()

    val baseRemota = FirebaseFirestore.getInstance()
    private var areasSpinner = arrayListOf<String>()

    lateinit var aa : ArrayAdapter<String>
    lateinit var spinner: Spinner

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spinner = binding.spinner

        areasSpinner.add("Seleccione un área")
        obtenerDepartamentos()
        areasID.forEach {
            Log.i("ID areas",it.toString())
        }
        aa = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, areasSpinner)
        aa.setNotifyOnChange(true)
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner.adapter = aa

        binding.btnInsertarArea.setOnClickListener {
            insertarArea()
        }

        binding.btnInsertarSubdepto.setOnClickListener {
            insertarSubdep()
        }

        return root
    }

    private fun insertarSubdep() {
        val edificio = binding.edIdEdificio.text.toString().trim()
        val piso = binding.edPiso.text.toString().trim()

        val textSpinner = spinner.selectedItem.toString()

        if (edificio.isEmpty() || piso.isEmpty()) {
            Toast.makeText(requireContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseFirestore.getInstance()
            .collection(Util.AREA)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result ){
                    val desc = doc.getString(Util.DESCRIPCION).toString()
                    if (textSpinner == desc){
                        val datos = hashMapOf(
                            Util.IDEDIFICIO to edificio,
                            Util.PISO to piso.toInt(),
                            Util.DESCRIPCION to textSpinner,
                            Util.IDAREA to doc.id
                        )

                        FirebaseFirestore.getInstance()
                            .collection(Util.AREA)
                            .document(doc.id)
                            .collection(Util.SUBDEPARTAMENTO)
                            .add(datos)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "SE INSERTO SUBDEPARTAMENTO CON EXITO", Toast.LENGTH_SHORT).show()
                                binding.edIdEdificio.setText("")
                                binding.edPiso.setText("")
                                spinner.setSelection(0)
                            }
                            .addOnFailureListener {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Error")
                                    .setMessage("No se pudo insertar")
                                    .show()
                            }
                    }
                }
            }

    }

    private fun obtenerDepartamentos() {
        areasSpinner.clear()
        areasID.clear()
        areasSpinner.add("Seleccione un área")
        baseRemota.collection(Util.AREA)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Areas", "${document.id} => ${document.data.get(Util.DESCRIPCION)}")
                    val descripcion = document.data.get(Util.DESCRIPCION).toString()
                    areasSpinner.add(descripcion)
                }
                aa.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
    }


    private fun insertarArea() {
        val desc = binding.edDescripcion.text.toString().trim()
        val division = binding.edDivision.text.toString().trim()
        val cantEmpl = binding.edCantEmpleados.text.toString().trim()

        if (desc.isEmpty() || division.isEmpty() || cantEmpl.isEmpty()){
            Toast.makeText(requireContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val datos = hashMapOf(
            Util.DESCRIPCION to desc,
            Util.DIVISION  to division,
            Util.CANTIDAD_EMPLEADOS  to cantEmpl.toInt()
           // Util.CANTIDAD_EMPLEADOS  to cantEmpl
        )

        baseRemota.collection(Util.AREA)
            .add(datos)
            .addOnCompleteListener {
                Toast.makeText(
                    requireContext(),
                    "SE INSERTO AREA CON EXITO",
                    Toast.LENGTH_SHORT
                ).show()
                binding.edDescripcion.setText("")
                binding.edDivision.setText("")
                binding.edCantEmpleados.setText("")
                obtenerDepartamentos()
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("No se pudo insertar")
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}