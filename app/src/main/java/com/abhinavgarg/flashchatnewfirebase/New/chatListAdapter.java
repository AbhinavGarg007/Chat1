package com.abhinavgarg.flashchatnewfirebase.New;



        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.abhinavgarg.flashchatnewfirebase.InstantMessage;
        import com.abhinavgarg.flashchatnewfirebase.R;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;

        import java.util.ArrayList;

public class chatListAdapter extends BaseAdapter {


    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListner = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public chatListAdapter(Activity mActivity, DatabaseReference ref, String mDisplayName) {
        this.mActivity = mActivity;
        this.mDatabaseReference = ref.child("messages");
        this.mDisplayName = mDisplayName;
        this.mDatabaseReference.addChildEventListener(mListner);

        this.mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{

        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {

        DataSnapshot snapshot =mSnapshotList.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName =(TextView)view.findViewById(R.id.author);
            holder.body = (TextView) view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            view.setTag(holder);
        }
        final InstantMessage message =  getItem(position);
        final ViewHolder holder =(ViewHolder) view.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe,holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg =message.getMessage();
        holder.body.setText(msg);


        return view;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder){

        if(isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.RED);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void  cleanUp(){

        mDatabaseReference.removeEventListener(mListner);
    }

}
