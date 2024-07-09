package com.example.foodproject.Activity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodproject.Activity.Domain.Foods;
import com.example.foodproject.Activity.Helper.ChangeNumberItemsListener;
import com.example.foodproject.Activity.Helper.ManagementListFavorite;
import com.example.foodproject.R;

import java.util.ArrayList;

public class ListFavoriteAdapter extends RecyclerView.Adapter<ListFavoriteAdapter.viewholder> {

    ArrayList<Foods> list;
    private ManagementListFavorite managementListFavorite;
    ChangeNumberItemsListener changeNumberItemsListener;

    public ListFavoriteAdapter(ArrayList<Foods> list, ManagementListFavorite managementListFavorite, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        this.managementListFavorite = managementListFavorite;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ListFavoriteAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_favorite,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListFavoriteAdapter.viewholder holder, int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.feeEachTxt.setText("$" + (list.get(position).getPrice()));

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.trashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementListFavorite.removeItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,feeEachTxt;
        ImageView pic;
        ConstraintLayout trashBtn;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachTxt = itemView.findViewById(R.id.feeEachitem);
            trashBtn = itemView.findViewById(R.id.trashBtn);

        }
    }
}
