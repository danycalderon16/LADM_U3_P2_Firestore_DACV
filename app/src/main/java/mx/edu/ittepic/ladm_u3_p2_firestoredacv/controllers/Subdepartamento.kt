package mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log

class Subdepartamento {
    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/
    var idSubdepto = ""
    var idEdificio = ""
    var piso = 0
    var idArea = ""

    override fun toString(): String {
        return "IDsubd: "+ idSubdepto + ", " +"IDEdif: "+ idEdificio +
                ", " +"Piso: "+ piso + ", " + "IDarea: "+idArea
    }
}