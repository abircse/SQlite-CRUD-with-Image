package com.thisisabir.sqliteimage.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thisisabir.sqliteimage.MainActivity;
import com.thisisabir.sqliteimage.R;
import com.thisisabir.sqliteimage.adapter.FoodAdapter;
import com.thisisabir.sqliteimage.model.FoodModel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FoodModel> foodList;
    private LinearLayoutManager layoutManager;
    private FoodAdapter adapter;
    private TextView nodatatext;

    // dialog widget
    private ImageView imageView;
    private EditText foodname;
    private EditText foodprice;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        nodatatext = findViewById(R.id.nodatatext);
        recyclerView = findViewById(R.id.foodrecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(adapter);

        // check data exist or not in list
        if (foodList.size() == 0)
        {
            nodatatext.setVisibility(View.VISIBLE);
        }

        //get foodlist data from sqlite database
        Cursor cursor = MainActivity.sQliteHelper.getdata("SELECT * FROM FOOD");
        foodList.clear();
        while (cursor.moveToNext()) {
            // get data from column like index 0 = column1, 1 = column2
            int id = cursor.getInt(0);
            String foodname = cursor.getString(1);
            String foodprice = cursor.getString(2);
            byte[] foodimage = cursor.getBlob(3);

            // set received data to list throw model class
            foodList.add(new FoodModel(id, foodname, foodprice, foodimage));
            nodatatext.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                final CharSequence[] items = {"Update Data", "Delete Data"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(FoodListActivity.this);
                dialog.setTitle("Chosse an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            //GET POSITION OF ROW
                            Cursor c = MainActivity.sQliteHelper.getdata("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            // Show dialog updata here
                            showDialogUpdate(FoodListActivity.this, arrID.get(position));
                        } else {

                            //GET POSITION OF ROW
                            Cursor c = MainActivity.sQliteHelper.getdata("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            // delete operation dialog method call
                            showDialogDelete(arrID.get(position));

                        }

                    }
                });
                dialog.show();
            }
        });

    }

    // show dialog for delete data
    private void showDialogDelete(final int id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FoodListActivity.this);
        alertDialogBuilder.setTitle("Warning").setMessage("Are you sure want to delete this food?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // delete operation
                try
                {
                    MainActivity.sQliteHelper.deleteData(id);
                    Toast.makeText(getApplicationContext(), "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                // call fro refresh food list
                refreshFoodList();

            }
        }).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).show();

    }


    // show dialog for update
    private void showDialogUpdate(Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_dialog);
        dialog.setTitle("Update Food");
        imageView = dialog.findViewById(R.id.dialogimagefood);
        foodname = dialog.findViewById(R.id.dialogfoodname);
        foodprice = dialog.findViewById(R.id.dialogfoodprice);
        updateButton = dialog.findViewById(R.id.dialogupdatebutton);

        // set dialog width
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set dialog width
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        // set height & width to dialog for apply
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        // dialog image click operation
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Request Permission
                ActivityCompat.requestPermissions(FoodListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 888);

            }
        });

        // update button click oepration in dialog
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    MainActivity.sQliteHelper.updateData(foodname.getText().toString().trim(), foodprice.getText().toString().trim(), MainActivity.imageViewToByte(imageView), position);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                // call fro refresh food list
                refreshFoodList();

            }
        });

    }

    // this method is fro when update data & dialog close it will refresh recyclerview
    private void refreshFoodList() {
        //get foodlist data from sqlite database
        Cursor cursor = MainActivity.sQliteHelper.getdata("SELECT * FROM FOOD");
        foodList.clear();
        while (cursor.moveToNext()) {
            // get data from column like index 0 = column1, 1 = column2
            int id = cursor.getInt(0);
            String foodname = cursor.getString(1);
            String foodprice = cursor.getString(2);
            byte[] foodimage = cursor.getBlob(3);

            // set received data to list throw model class
            foodList.add(new FoodModel(id, foodname, foodprice, foodimage));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            } else {
                Toast.makeText(FoodListActivity.this, "You dont have permission to access file storage", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // on Activity Result override method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                // set our ui image view from storage choose image
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}