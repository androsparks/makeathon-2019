package com.selectmakeathon.app.ui.main.problems.ProbFragmentPack;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.IMediaControllerCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.ProblemStatements;
import com.selectmakeathon.app.model.ProblemTrack;
import com.selectmakeathon.app.model.Problems;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.ui.main.info.InfoActivity;
import com.selectmakeathon.app.ui.main.problems.ProblemActivity;
import com.selectmakeathon.app.util.CustomTransformerView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


public class ProbHomeFrag extends androidx.fragment.app.Fragment {

    private DiscreteScrollView cardStack;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ListAdapter mListAdapter;
    public ArrayList<String> data =new ArrayList<String>();
    private ArrayList<String> imageList=new ArrayList<String>();
    private static final String TAG = "ProbHomeFrag";
    private ImageView backButton;


    public ProbHomeFrag() {
    }

    @Override
    public void onStart() {
        super.onStart();
        mListAdapter = new ListAdapter(data);
        cardStack.setAdapter(mListAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        //ArrayList<ProblemStatements> problemStatements = new ArrayList<>();
       // problemStatements.add(new ProblemStatements("Statement1name", "compname",2,"this is the worst","ST01" ));
        //problemStatements.add(new ProblemStatements("Statement2name", "compnameasd", 3,"this is the best","ST02"));

       // ProblemTrack problemTrack = new ProblemTrack();
        //problemTrack.setProblemStatements(problemStatements);

        //mDatabaseReference.child("problems").child("Safety").setValue(problemTrack);

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_prob_home, container, false);

        backButton=(ImageView)view.findViewById(R.id.navf);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome=new Intent(getActivity(), MainActivity.class);
                startActivity(backHome);
            }
        });

        //Card Stacker
        cardStack = view.findViewById(R.id.TaskRec);
        cardStack.setHasFixedSize(true);
        cardStack.setItemTransformer(new CustomTransformerView.Builder()
                .setMinScale(0.85f)
                .setElevation((10), (4))
                .setOverlapDistance((8))
                .build());
        data.clear();

        data.add("Artificial Intelligence");
        data.add("Emerging Technologies");
        data.add("Energy Management");
        data.add("Health Care");
        data.add("IoT and Automation");
        data.add("Safety and Security");
        mListAdapter = new ListAdapter(data);
        cardStack.setAdapter(mListAdapter);
        return view;

    }

    //Adapter Class
    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private ArrayList<String> dataList;
        public ListAdapter(ArrayList<String> data) {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewText;

            public ViewHolder(View itemView) {
                super(itemView);
                this.textViewText = (TextView) itemView.findViewById(R.id.TrackText);
            }
        }

        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {
            holder.textViewText.setText(dataList.get(position));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                            ArtificialFrag artificialFrag=new ArtificialFrag();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,artificialFrag)
                                    .commit();
                            break;
                        case 1:
                            EmergingFragment emergingFragment=new EmergingFragment();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,emergingFragment)
                                    .commit();
                            break;
                        case 2:
                            EnergyFrag energyFrag=new EnergyFrag();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,energyFrag)
                                    .commit();
                            break;
                        case 3:
                            HealthFrag healthFrag=new HealthFrag();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,healthFrag)
                                    .commit();
                            break;
                        case 4:
                            InternetOfThingsFrag internetOfThingsFrag=new InternetOfThingsFrag();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,internetOfThingsFrag)
                                    .commit();
                            break;
                        case 5:
                            SafetyFrag safetyFrag=new SafetyFrag();
                            getFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .addSharedElement(cardStack,cardStack.getTransitionName())
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragholder,safetyFrag)
                                    .commit();
                            break;
                        default:

                    }

                }
            });
        }

        public int getItemCount() {

            return dataList.size();
        }

    }
}

