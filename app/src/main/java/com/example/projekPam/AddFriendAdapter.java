package com.example.projekPam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {

    private Context context;
    private List<Friend> friendList;
    private String currentUserId;

    public AddFriendAdapter(Context context, List<Friend> friendList, String currentUserId) {
        this.context = context;
        this.friendList = friendList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tambah_teman, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendList.get(position);

        holder.usernameText.setText(friend.getUsername());
        holder.avatarImage.setImageResource(R.drawable.avatar);

        holder.addButton.setOnClickListener(v -> {
            addFriend(friend.getId());
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    private void addFriend(String friendId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(friendId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String fullname = documentSnapshot.getString("fullname");

                        Friend friend = new Friend(friendId, username, fullname);

                        db.collection("users").document(currentUserId)
                                .collection("friends").document(friendId)
                                .set(friend)
                                .addOnSuccessListener(aVoid -> {

                                    friendList.removeIf(f -> f.getId().equals(friendId));
                                    notifyDataSetChanged();

                                    ((ProfileUtamaActivity) context).addFriendToFriendsList(friend);

                                    Toast.makeText(context, "Menambahkan " + fullname + " ke daftar teman", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        ImageView avatarImage, addButton;

        public ViewHolder(View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.tvUsername);
            avatarImage = itemView.findViewById(R.id.ivAvatar);
            addButton = itemView.findViewById(R.id.btnAddFriend);
        }
    }
}
