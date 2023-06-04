package com.tuk.lightninggathering;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> postList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<String> meetingKeysList; // meetingKeys 값을 전달받을 변수

    public interface OnItemClickListener {
        void onItemClick(Post post, String meetingKeys); // meetingKeys 값을 전달받도록 수정
    }

    public PostAdapter(List<Post> postList, Context context, List<String> meetingKeysList) {
        this.postList = postList;
        this.context = context;
        this.meetingKeysList = meetingKeysList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_post, parent, false);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(0, dpToPx(10), 0, dpToPx(10)); // 상단과 하단에 10dp 간격을 설정합니다.
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        String meetingKeys = meetingKeysList.get(position); // 해당 위치의 meetingKeys 값을 가져옴

        holder.titleTextView.setText(post.getTitle());
        holder.dateTextView.setText(post.getDate());
        holder.locationTextView.setText(post.getLocation());
        holder.participantsTextView.setText(post.getMemberKeys().size() + "/" + post.getMaxMemberCount());

        holder.bind(post, meetingKeys);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView locationTextView;
        TextView participantsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            participantsTextView = itemView.findViewById(R.id.participantsTextView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Post post, String meetingKeys) {
            // 데이터 바인딩
            titleTextView.setText(post.getTitle());
            dateTextView.setText(post.getDate());
            locationTextView.setText(post.getLocation());
            participantsTextView.setText(post.getMemberKeys().size() + "/" + post.getMaxMemberCount());

            // 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = postList.get(position);
                        String meetingKeys = meetingKeysList.get(position); // 해당 위치의 meetingKeys 값을 가져옴
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(post, meetingKeys);
                        }
                    }
                }
            });
        }


    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
