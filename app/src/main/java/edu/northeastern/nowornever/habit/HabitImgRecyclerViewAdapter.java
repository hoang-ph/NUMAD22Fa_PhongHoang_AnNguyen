package edu.northeastern.nowornever.habit;

import static edu.northeastern.nowornever.utils.Constants.DATE_FORMAT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import edu.northeastern.nowornever.R;

public class HabitImgRecyclerViewAdapter extends RecyclerView.Adapter<HabitImgRecyclerViewAdapter.HabitImgViewHolder> {

    private final List<HabitImage> habitImages;
    private final Context context;

    public HabitImgRecyclerViewAdapter(Context context, List<HabitImage> habitImages) {
        this.habitImages = habitImages;
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
        Glide.with(context).load(habitImages.get(position).getImgUri()).into(holder.habitImg);
        String imgID = habitImages.get(position).getImgID();
        long imgCreatedDateEpoch = Long.parseLong(imgID);
        Date date = new Date(imgCreatedDateEpoch);
        holder.imgCreatedDate.setText(DATE_FORMAT.format(date));
    }

    @Override
    public int getItemCount() {
        return habitImages.size();
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

}
