package com.example.restaurantorganizer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantorganizer.R;
import com.example.restaurantorganizer.model.Menutype;

import java.util.List;

public class MenutypeAdapter extends RecyclerView.Adapter<MenutypeAdapter.ViewHolder>
{
    private List<Menutype> mData;
    private LayoutInflater mInflater;
    private MenutypeAdapter.ItemClickListener mClickListener;

    public MenutypeAdapter(Context context, List<Menutype> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View menuTypeView;

        TextView tv_menutype;

        ViewHolder(View itemView) {
            super(itemView);
            menuTypeView = itemView;

            tv_menutype = (TextView) itemView.findViewById(R.id.tv_menutype);
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
