package com.example.restaurantorganizer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.R;
import com.example.restaurantorganizer.model.Menutype;

import java.util.List;

public class MenutypeAdapter extends RecyclerView.Adapter<MenutypeAdapter.ViewHolder>
{
    private List<Menutype> mData;
    private LayoutInflater mInflater;
    private MenutypeAdapter.ItemClickListener mClickListener;
    private Menutype selectedMenutype;

    public MenutypeAdapter(Context context, List<Menutype> data, Menutype selectedMenutype) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.selectedMenutype = selectedMenutype;
    }

    @Override
    public MenutypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.menutype_view, parent, false);
        return new MenutypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenutypeAdapter.ViewHolder holder, int position) {
        Menutype menuType = mData.get(position);

        holder.tv_menutype.setText(mData.get(position).getName());
        if(selectedMenutype.getId() == menuType.getId()) {
            holder.card_menutype.setCardBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View menuTypeView;

        TextView tv_menutype;
        CardView card_menutype;

        ViewHolder(View itemView) {
            super(itemView);
            menuTypeView = itemView;

            tv_menutype = itemView.findViewById(R.id.tv_menutype);
            card_menutype = itemView.findViewById(R.id.card_menutype);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Menutype getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(MenutypeAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
