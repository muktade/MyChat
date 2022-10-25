package com.example.mychat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mychat.Model.Users;
import com.example.mychat.R;
import com.example.mychat.adapters.UsersAdapter;
import com.example.mychat.databinding.FragmentChatsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {


    public ChatsFragment() {
        // Required empty public constructor
    }
    FragmentChatsBinding fragmentChatsBinding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentChatsBinding = FragmentChatsBinding.inflate(inflater, container, false);

        UsersAdapter usersAdapter = new UsersAdapter(list, getContext());
        fragmentChatsBinding.chatRecyclerView.setAdapter(usersAdapter);
        database = FirebaseDatabase.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentChatsBinding.chatRecyclerView.setLayoutManager(linearLayoutManager);

        database.getReference()
                .child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Users users = dataSnapshot.getValue(Users.class);
                            users.getUserId(dataSnapshot.getKey());
                            list.add(users);
                        }
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return fragmentChatsBinding.getRoot();
    }
}