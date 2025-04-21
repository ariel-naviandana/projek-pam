package com.example.projekPam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private Context context;
    private List<Friend> friendsList;
    private String currentUserId;
    private ProfileUtamaActivity activity;

    public FriendAdapter(Context context, List<Friend> friendsList, String currentUserId) {
        this.context = context;
        this.friendsList = friendsList;
        this.currentUserId = currentUserId;

        if (context instanceof ProfileUtamaActivity) {
            activity = (ProfileUtamaActivity) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daftar_teman, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendsList.get(position);

        holder.fullnameText.setText(friend.getFullname());
        holder.usernameText.setText(friend.getUsername());
        holder.avatarImage.setImageResource(R.drawable.avatar);

        holder.unfollowButton.setOnClickListener(v -> {
            unfollowFriend(friend.getId());
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    private void unfollowFriend(String friendId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(currentUserId)
                .collection("friends").document(friendId)
                .delete()
                .addOnSuccessListener(aVoid -> {

                    Friend unfollowedFriend = findFriendById(friendId);
                    if (unfollowedFriend != null) {
                        friendsList.remove(unfollowedFriend);
                        notifyDataSetChanged();

                        if (activity != null) {
                            activity.addPotentialFriendBack(unfollowedFriend);
                        }
                    }
                });
    }

    private Friend findFriendById(String friendId) {
        for (Friend friend : friendsList) {
            if (friend.getId().equals(friendId)) {
                return friend;
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullnameText, usernameText;
        ImageView avatarImage, unfollowButton;

        public ViewHolder(View itemView) {
            super(itemView);
            fullnameText = itemView.findViewById(R.id.tvName);
            usernameText = itemView.findViewById(R.id.tvUsername);
            avatarImage = itemView.findViewById(R.id.ivProfile);
            unfollowButton = itemView.findViewById(R.id.unfoll);
        }
    }
}