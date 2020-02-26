package com.example.restaurantorganizer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.R;
import com.example.restaurantorganizer.model.OrderItem;

import java.util.List;

public class OrderOverviewAdapter extends RecyclerView.Adapter<OrderOverviewAdapter.ViewHolder>
{
    private List<OrderItem> mData;
    private LayoutInflater mInflater;
    private OrderOverviewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public OrderOverviewAdapter(Context context, List<OrderItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public OrderOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.orderitem_view, parent, false);
        return new OrderOverviewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(OrderOverviewAdapter.ViewHolder holder, int position) {
        OrderItem orderItem = mData.get(position);

        holder.tv_orderItem.setText(mData.get(position).getMenu().getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View orderItemView;

        TextView tv_orderItem;

        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            orderItemView = itemView;

            tv_orderItem = (TextView) itemView.findViewById(R.id.tv_orderItem);
            deleteButton = itemView.findViewById(R.id.deleteItem);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public OrderItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OrderOverviewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
