package com.e.buymybook;

import android.content.Intent;
import android.os.Bundle;

import com.e.buymybook.Adapter.AdapterForAllBooks;
import com.e.buymybook.Model.ModelForAllBooks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<ModelForAllBooks> nameListData;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
   public String name;

    RecyclerView BooksRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        readDataFromFirebase();
        BooksRecyclerView = findViewById(R.id.BooksRecyclerView);
        BooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //String[] Languages = {"java","c++","android","php","javascript","java","c++","android","php","javascript","java","c++","android","php","javascript","java","c++"};
        BooksRecyclerView.setAdapter(new AdapterForAllBooks(nameListData));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBooks.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void OnClickTxtView(String selectedItem) {

    }

    private void readDataFromFirebase() {
        nameListData = new ArrayList<>();
        nameListData.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    name = ds.getKey();//show all data of first all data from database
                    nameListData.add(new ModelForAllBooks(name));
                }
                BooksRecyclerView.setAdapter(new AdapterForAllBooks(nameListData));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
