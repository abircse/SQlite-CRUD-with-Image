package com.thisisabir.sqliteimage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thisisabir.sqliteimage.R;
import com.thisisabir.sqliteimage.model.FoodModel;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.Holder> {

    private Context context;
    private List<FoodModel> foodList;

    // create object for Onitemclick interface
    private OnItemClickListener mListerner;



    public FoodAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_custom_layout,parent,false);
        return new Holder(view, mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.foodName.setText(foodList.get(position).getFoodname());
        holder.foodPrice.setText(foodList.get(position).getFoodprice());
        // now process image get from database & set it in imageview throw model class
        byte[] foodImageArray = foodList.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImageArray,0,foodImageArray.length);
        holder.foodImage.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {

        if (foodList == null) {
            return 0;
        }
        return foodList.size();

    }

    class Holder extends RecyclerView.ViewHolder{

        TextView foodName, foodPrice;
        ImageView foodImage;

        Holder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            foodName = itemView.findViewById(R.id.fname);
            foodPrice = itemView.findViewById(R.id.fprice);
            foodImage = itemView.findViewById(R.id.fimage);

            // add click on itemview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!= null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    //interface
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // this will call in activity
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListerner = listener;
    }

}
