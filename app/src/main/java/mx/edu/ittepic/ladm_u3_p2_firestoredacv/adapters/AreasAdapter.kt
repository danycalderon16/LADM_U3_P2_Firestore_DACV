package mx.edu.ittepic.ladm_u3_p2_firestoredacv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.R
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.Area

class AreasAdapter (private val list:ArrayList<Area>, itemListener: onItemClickListener): RecyclerView.Adapter<AreasAdapter.ViewHolder>() {

    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/
    var mListener : onItemClickListener = itemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_areas,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.descr.setText(list[position].descripcion)
        holder.canEmp.setText(list[position].cantidadEmpleados.toString())
        holder.div.setText(list[position].division)
        holder.ivEdit.setOnClickListener {
            mListener.onItemClick(list[position],0)
        }
        holder.ivDelete.setOnClickListener {
            mListener.onItemClick(list[position],1)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(item: View) :RecyclerView.ViewHolder(item){
        var descr : TextView = item.findViewById(R.id.tv_descrp_area)
        var canEmp : TextView = item.findViewById(R.id.tv_cant_emp_area)
        var div : TextView = item.findViewById(R.id.tv_division_area)
        var ivEdit : ImageView = item.findViewById(R.id.iv_edit_area)
        var ivDelete : ImageView = item.findViewById(R.id.iv_delete_area)
    }

    interface onItemClickListener{
        fun onItemClick(area: Area, i:Int)
    }
}