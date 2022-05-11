package mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers

import android.content.Context

class AreaSubp {
    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/

    var idSubdept = ""
    var descripcion = ""
    var division = ""
    var idEdificio = ""
    var piso = 0
    var idArea = ""

    override fun toString(): String {
        return idArea+" - "+idSubdept +": descripcion " + descripcion +" ,"+"division " +
                division  +" ,"+"idEdificio " + idEdificio +" ,"+"piso " + piso
    }

}