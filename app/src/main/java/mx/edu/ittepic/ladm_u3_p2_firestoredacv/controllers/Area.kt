package mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class Area {
    var idArea = ""
    var descripcion = ""
    var division = ""
    //var cantidadEmpleados = ""
    var cantidadEmpleados = 0

}