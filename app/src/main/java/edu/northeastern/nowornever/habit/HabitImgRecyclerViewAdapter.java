package edu.northeastern.nowornever.habit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import edu.northeastern.nowornever.R;

public class HabitImgRecyclerViewAdapter extends RecyclerView.Adapter<HabitImgRecyclerViewAdapter.HabitImgViewHolder> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());

    private final List<String> imgIDs;
    private final String username;
    private final Context context;

    public HabitImgRecyclerViewAdapter(Context context, List<String> imgIDs, String username) {
        this.imgIDs = imgIDs;
        this.username = username;
        this.context = context;
    }

    @NonNull
    @Override
    public HabitImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new HabitImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitImgViewHolder holder, int position) {
        String imgID = imgIDs.get(position);
        Bitmap imgBitmap = generateImgBitmap(imgID);
        holder.habitImg.setImageBitmap(imgBitmap);
        long imgCreatedDateEpoch = Long.parseLong(imgID);
        Date date = new Date(imgCreatedDateEpoch);
        holder.imgCreatedDate.setText(DATE_FORMAT.format(date));
    }

    @Override
    public int getItemCount() {
        return imgIDs.size();
    }

    public static class HabitImgViewHolder extends RecyclerView.ViewHolder {

        private final ImageView habitImg;
        private final TextView imgCreatedDate;

        public HabitImgViewHolder(@NonNull View itemView) {
            super(itemView);
            habitImg = itemView.findViewById(R.id.habitImg);
            imgCreatedDate = itemView.findViewById(R.id.imgCreatedDate);
        }
    }

    private Bitmap generateImgBitmap(String imgID) {
        final Bitmap[] result = {null};
        String localTempImgID = UUID.randomUUID().toString();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + this.username + "/" + imgID + ".jpg");
        try {
            File localFile = File.createTempFile(localTempImgID, ".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    result[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result[0];
    }

}
