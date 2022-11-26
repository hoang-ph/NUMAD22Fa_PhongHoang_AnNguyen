package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.CHILD_IMG_ID;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class MediaFragment extends Fragment {

    private List<String> imgIDs;
    private RecyclerView imgRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        String habitUuid = getArguments().getString(HABIT_ID_KEY);
        String username = getArguments().getString(USERNAME_KEY);

        imgIDs = new ArrayList<>();
        imgRecyclerView = view.findViewById(R.id.imageRecyclerView);
        imgRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        HabitImgRecyclerViewAdapter adapter = new HabitImgRecyclerViewAdapter(getContext(), imgIDs, username);
        imgRecyclerView.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid);
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot ds = task.getResult();

                    imgIDs = (List<String>) ds.child(CHILD_IMG_ID).getValue();
                    if (imgIDs != null) {
                        imgRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}