package com.example.restaurantorganizer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.R;
import com.example.restaurantorganizer.model.Menu;
import com.example.restaurantorganizer.model.OrderItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
{
    private List<Menu> mData;
    private List<OrderItem> selectedOrderItems;
    private LayoutInflater mInflater;
    private MenuAdapter.ItemClickListener mClickListener;

    public MenuAdapter(Context context, List<Menu> data, List<OrderItem> orderItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.selectedOrderItems = orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.selectedOrderItems = orderItems;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view, parent, false);
        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        Menu item = mData.get(position);

        holder.tv_item.setText(mData.get(position).getName());
        holder.tv_ingredients.setText(mData.get(position).getIngredients());
        if (selectedOrderItems.stream().map(OrderItem::getMenu).collect(toList())
                .stream().anyMatch(menu -> menu.getId() == item.getId())) {
            holder.myitemView.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myitemView;

        TextView tv_item;
        TextView tv_ingredients;

        ViewHolder(View itemView) {
            super(itemView);
            myitemView = itemView;

            tv_item = itemView.findViewById(R.id.tv_item);
            tv_ingredients = itemView.findViewById(R.id.tv_ingredients);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Menu getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(MenuAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
