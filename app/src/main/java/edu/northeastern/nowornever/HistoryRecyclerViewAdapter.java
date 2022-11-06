package edu.northeastern.nowornever;

import static edu.northeastern.nowornever.utils.Constants.STICKER_RESOURCES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.nowornever.model.sticker.Sticker;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.StickerViewHolder> {

    private List<Sticker> stickers;
    private Context context;

    public HistoryRecyclerViewAdapter(Context context, List<Sticker> stickers) {
        this.context = context;
        this.stickers = stickers;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Integer stickerResource = STICKER_RESOURCES.get(stickers.get(position).type);
        holder.stickerImg.setImageResource(stickerResource);
        String stickerInfo = stickers.get(position).getStickerInfo();
        holder.stickerInfo.setText(stickerInfo);
    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public static class StickerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView stickerImg;
        private final TextView stickerInfo;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerImg = itemView.findViewById(R.id.stickerImg);
            stickerInfo = itemView.findViewById(R.id.stickerInfo);
        }
    }

}

