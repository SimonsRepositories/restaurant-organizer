package com.example.restaurantorganizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantorganizer.model.Item;

import org.w3c.dom.Text;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
    private List<Item> mData;
    private LayoutInflater mInflater;
    private ItemAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    ItemAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        Item item = mData.get(position);

        holder.tv_item.setText(mData.get(position).getTitle());
        holder.tv_ingredients.setText(mData.get(position).getIngredients());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myitemView;

        TextView tv_item;
        TextView tv_ingredients;

        ViewHolder(View itemView) {
            super(itemView);
            myitemView = itemView;

            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
            tv_ingredients = (TextView) itemView.findViewById(R.id.tv_ingredients);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Item getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
