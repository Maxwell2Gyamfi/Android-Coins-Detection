package com.example.coinsdetection

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecycleViewAdapter(private val mContext: Context, private val mData: MutableList<SavedImages>) :
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.cardview_item_image, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder:ViewHolder, position: Int) {
        viewHolder.imageViewThumbnail.setImageBitmap(mData[position].imageToSave)
        viewHolder.selectedCardView.setOnClickListener {

            val intent = Intent(mContext, SelectedHistoryImage::class.java).apply {
//                putExtra("Image", mData[position].imageToSave)
                putExtra("ID", mData[position].id)
                putExtra("Count", mData[position].totalItems)
                putExtra("Total",mData[position].totalCost)
            }
            mContext.startActivity(intent)
        }
    }
    override fun getItemCount() = mData.size

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageViewThumbnail: ImageView = view.findViewById(R.id.historyImage)
            var selectedCardView: CardView = view.findViewById(R.id.cardviewID)
        }
}



