package mx.edu.ittepic.ladm_u3_p2_firestoredacv.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.R
import mx.edu.ittepic.ladm_u3_p2_firestoredacv.controllers.AreaSubp

class SubdepAdapter (private val list:ArrayList<AreaSubp>, itemListener: onItemClickListenr): RecyclerView.Adapter<SubdepAdapter.ViewHolder>() {

    /************************************
     * DANIEL ALEJANDRO CALDERÓN VIGREN *
     ************************************/
    var mListener : onItemClickListenr = itemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_list,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.descr.setText(list[position].descripcion)
        holder.edif.setText(list[position].idEdificio)
        holder.piso.setText(list[position].piso.toString())
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
        var descr : TextView = item.findViewById(R.id.tv_descrp_sd)
        var ivEdit : ImageView = item.findViewById(R.id.iv_edit_sd)
        var ivDelete : ImageView = item.findViewById(R.id.iv_delete_sd)
        var edif : TextView = item.findViewById(R.id.tv_edificio_sd)
        var piso : TextView = item.findViewById(R.id.tv_piso_sd)
        var div : TextView = item.findViewById(R.id.tv_division_sd)

    }
    interface onItemClickListenr{
        fun onItemClick(areaSub: AreaSubp, i:Int)
    }
}