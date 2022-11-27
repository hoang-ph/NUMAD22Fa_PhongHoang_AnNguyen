package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT;
import static edu.northeastern.nowornever.utils.Constants.CHILD_HABIT_IMG;
import static edu.northeastern.nowornever.utils.Constants.FAIL_UPLOAD;
import static edu.northeastern.nowornever.utils.Constants.HABIT_ID_KEY;
import static edu.northeastern.nowornever.utils.Constants.LOADING;
import static edu.northeastern.nowornever.utils.Constants.NO_IMG_NOTI;
import static edu.northeastern.nowornever.utils.Constants.ROOT_HABIT;
import static edu.northeastern.nowornever.utils.Constants.SERVER_ERROR;
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
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.nowornever.R;

public class MediaFragment extends Fragment {

    private List<HabitImage> habitImages;
    private String username;
    private RecyclerView imgRecyclerView;
    private ImageView selectedImgView;
    private Button uploadImageBtn;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri imgUri;

    private final StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpg").build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media, container, false);
        String habitUuid = getArguments().getString(HABIT_ID_KEY);
        this.username = getArguments().getString(USERNAME_KEY);
        selectedImgView = view.findViewById(R.id.selectedImg);

        habitImages = new ArrayList<>();
        imgRecyclerView = view.findViewById(R.id.imageRecyclerView);
        imgRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        HabitImgRecyclerViewAdapter adapter = new HabitImgRecyclerViewAdapter(getContext(), habitImages);
        imgRecyclerView.setAdapter(adapter);

        Button selectImgBtn = view.findViewById(R.id.selectImageBtn);
        selectImgBtn.setOnClickListener(v -> selectImage());

        uploadImageBtn = view.findViewById(R.id.uploadImageBtn);
        uploadImageBtn.setOnClickListener(v -> uploadImage());

        databaseReference = FirebaseDatabase.getInstance().getReference().child(ROOT_HABIT).child(username).child(CHILD_HABIT).child(habitUuid).child(CHILD_HABIT_IMG);
        loadAllImages();

        return view;
    }

    private void loadAllImages() {
        habitImages.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String imgID = ds.getKey();
                        String uri = (String) ds.getValue();
                        HabitImage habitImage = new HabitImage(imgID, uri);
                        habitImages.add(habitImage);
                    }
                }
                if (habitImages.size() != 0) {
                    imgRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
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
            Toast.makeText(getContext(), NO_IMG_NOTI, Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImageBtn.setText(LOADING);
        String imgID = String.valueOf(Instant.now().toEpochMilli());

        storageReference = FirebaseStorage.getInstance().getReference("images/" + this.username + "/" + imgID);
        storageReference.putFile(imgUri, metadata)
                .addOnSuccessListener(taskSnapshot -> {
                    selectedImgView.setImageURI(null);
                    imgUri = null;
                    if (uploadImageBtn.getText().equals(LOADING)) {
                        uploadImageBtn.setText(UPLOAD);
                    }

                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> databaseReference.child(imgID).setValue(uri.toString()).addOnCompleteListener(task -> {
                        Toast.makeText(getContext(), SUCCESS_UPLOAD, Toast.LENGTH_SHORT).show();
                        loadAllImages();
                    }));

                }).addOnFailureListener(e -> {
                    if (uploadImageBtn.getText().equals(LOADING)) {
                        uploadImageBtn.setText(UPLOAD);
                    }
                    Toast.makeText(getContext(), FAIL_UPLOAD, Toast.LENGTH_SHORT).show();
                });
    }

}