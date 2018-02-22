package com.example.liyang.a1d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Li Yang on 11/12/2017.
 */

public class RecyclerRoute extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<String> mRecyclerViewItems = new ArrayList<>();
    private FirebaseDatabase mref;
    private DatabaseReference rootRef;
    public static HashMap<String,String> savedfav = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_setup);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        this.setTitle("Favorites");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mref = FirebaseDatabase.getInstance();
        rootRef = mref.getReference();
        rootRef.keepSynced(true);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ParkingInfo info = postSnapshot.getValue(ParkingInfo.class);
                    if(info.getSaved() == 1){
                        mRecyclerViewItems.add(info.getParkingname());
                        RecyclerView.Adapter adapter = new MyAdapter(new ArrayList<>(mRecyclerViewItems));
                        mRecyclerView.setAdapter(adapter);
                        savedfav.put(info.getParkingname(),info.getId());

                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    //Creates the saved route on ShowRoute.Class
    public void whenClick(View v){
        int length = mRecyclerViewItems.size();
        for(int i =0; i<length;i++){
            displayResult.ChosenPark[0] = mRecyclerViewItems.get(i);
        }
        Intent ShowRoute = new Intent(this, com.example.liyang.a1d.ShowRoute.class);
        startActivity(ShowRoute);
    }
}