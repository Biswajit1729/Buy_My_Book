package com.e.buymybook.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.e.buymybook.MainActivity;
import com.e.buymybook.Model.ModelForAllBooks;
import com.e.buymybook.R;

import java.util.List;


public class AdapterForAllBooks extends RecyclerView.Adapter<AdapterForAllBooks.ProgrammingViewHolder> {

    public Context context;
    private  final List<ModelForAllBooks> nameList;
    public AdapterForAllBooks(List<ModelForAllBooks> nameList)
    {
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_books_layout, parent, false);
        context = parent.getContext();
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {

        holder.txtTitle.setText(nameList.get(position).getName());
        final ModelForAllBooks list = nameList.get(position);
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = list.getName();
                ((MainActivity) context).OnClickTxtView(selectedItem);
            }
        });
    }



    @Override
    public int getItemCount() {

        return  nameList.size();
    }

    //View holder Class

    class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView txtTitle;
        ProgrammingViewHolder(View itemView) {
            super(itemView);
            txtTitle =itemView.findViewById(R.id.TitleTxtView);
            imgView =itemView.findViewById(R.id.imgView);

        }
    }
}
