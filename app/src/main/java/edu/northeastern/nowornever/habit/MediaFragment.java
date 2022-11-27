package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.CHILD_IMG_ID;
import static edu.northeastern.nowornever.utils.Constants.FAIL_UPLOAD;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.LOADING;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SUCCESS_UPLOAD;
import static edu.northeastern.nowornever.utils.Constants.UPLOAD;
import static edu.northeastern.nowornever.utils.Constants.USERNAME_KEY;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class MediaFragment extends Fragment {

    private List<String> imgIDs;
    private String username;
    private RecyclerView imgRecyclerView;
    private ImageView selectedImgView;
    private Button uploadImageBtn;
    private StorageReference storageReference;
    private Uri imgUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        String habitUuid = getArguments().getString(HABIT_ID_KEY);
        this.username = getArguments().getString(USERNAME_KEY);
        selectedImgView = view.findViewById(R.id.selectedImg);

        imgIDs = new ArrayList<>();
        imgRecyclerView = view.findViewById(R.id.imageRecyclerView);
        imgRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        HabitImgRecyclerViewAdapter adapter = new HabitImgRecyclerViewAdapter(getContext(), imgIDs, username);
        imgRecyclerView.setAdapter(adapter);

        Button selectImgBtn = view.findViewById(R.id.selectImageBtn);
        selectImgBtn.setOnClickListener(v -> selectImage());

        uploadImageBtn = view.findViewById(R.id.uploadImageBtn);
        uploadImageBtn.setOnClickListener(v -> uploadImage());

        // TODO: redo this functionality to load all images - reimplement upload image and firebase architecture
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectedImgLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> selectedImgLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imgUri = data.getData();
                        selectedImgView.setImageURI(imgUri);
                    }
                }
            });


    private void uploadImage() {
        if (imgUri == null) {
            Toast.makeText(getContext(), "An image has to be selected", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageBtn.setText(LOADING);
        String imgID = String.valueOf(Instant.now().toEpochMilli());

        storageReference = FirebaseStorage.getInstance().getReference("images/" + this.username + "/" + imgID);
        storageReference.putFile(imgUri)
                .addOnSuccessListener(taskSnapshot -> {
                    selectedImgView.setImageURI(null);
                    imgUri = null;
                    Toast.makeText(getContext(), SUCCESS_UPLOAD, Toast.LENGTH_SHORT).show();
                    if (uploadImageBtn.getText().equals(LOADING)) {
                        uploadImageBtn.setText(UPLOAD);
                    }
                }).addOnFailureListener(e -> {
                    if (uploadImageBtn.getText().equals(LOADING)) {
                        uploadImageBtn.setText(UPLOAD);
                    }
                    Toast.makeText(getContext(), FAIL_UPLOAD, Toast.LENGTH_SHORT).show();
                });

    }

}