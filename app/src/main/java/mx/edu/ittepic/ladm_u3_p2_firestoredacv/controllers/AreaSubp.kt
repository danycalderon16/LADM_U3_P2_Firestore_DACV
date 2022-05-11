package mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers

import android.content.Context

class AreaSubp {
    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/

    var descripcion = ""
    var division = ""
    var idEdificio = ""
    var piso = 0

    override fun toString(): String {
        return "descripcion " + descripcion +" ,"+"division " +
                division  +" ,"+"idEdificio " + idEdificio +" ,"+"piso " + piso
    }

}