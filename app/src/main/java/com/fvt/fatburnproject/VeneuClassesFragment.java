package com.fvt.fatburnproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VeneuClassesFragment extends Fragment {

    public static VeneuClassesFragment instance;
    ArrayList<VeneuModel> list;
    DatabaseReference myRefVenueClass;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String TAG;

    public ArrayList<VeneuModel> getList() {
        return list;
    }

    public void setList(ArrayList<VeneuModel> list) {
        this.list = list;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        instance=this;
        TAG="***Venve";
        list=new ArrayList<>();
        recyclerView=root.findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter=new MyAdapter(getContext(),getActivity(),list);
        recyclerView.setAdapter(myAdapter);
        myRefVenueClass = FirebaseDatabase.getInstance().getReference("Classes").child("VeneuClass");
        myRefVenueClass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.e(TAG,"datasnapshot 1 id is "+dataSnapshot1.getKey());
                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                            Log.e(TAG,"datasnapshot 2 id is "+dataSnapshot2.getKey());
                            VeneuModel veneuModel=new VeneuModel();
                            veneuModel.setVenue(dataSnapshot1.getKey());
                            veneuModel.setDay(dataSnapshot2.getKey());
                            veneuModel.setTotalSeats(dataSnapshot2.child("seats").getValue(String.class));
                            veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                            if(dataSnapshot2.getKey().equalsIgnoreCase("types")){

                            }
                            else{
                                list.add(veneuModel);
                            }
                        }
                    }
                    myAdapter.notifyDataSetChanged();

                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("***mainfrag","past frag on start");
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<VeneuModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView name,day,wbxTime;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
//                image=view.findViewById(R.id.select_image);
//                image.setVisibility(View.GONE);
                name=view.findViewById(R.id.day);
                day=view.findViewById(R.id.fbx_time);
//                day.setVisibility(View.GONE);
                wbxTime=view.findViewById(R.id.wbx_time);
                wbxTime.setVisibility(View.GONE);
                LinearLayout linearLayout=view.findViewById(R.id.types_layout);
                linearLayout.setVisibility(View.GONE);
                TextView textView=view.findViewById(R.id.select_type_text);
                textView.setVisibility(View.GONE);



            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<VeneuModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.revenue_class_item, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText(data.get(position).getDay()+" Time "+data.get(position).getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity,AdminVeneuClassDetailActivity.class).putExtra("data",list.get(position)));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    aleartDialog(list.get(position));
                    return false;
                }
            });



        }
        @Override
        public int getItemCount() {
//        return  5;
            return data.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    public void aleartDialog(VeneuModel veneuModel){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Are you sure to delete this class?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                myRefVenueClass.child(veneuModel.getVenue()).child(veneuModel.getDay()).removeValue();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
