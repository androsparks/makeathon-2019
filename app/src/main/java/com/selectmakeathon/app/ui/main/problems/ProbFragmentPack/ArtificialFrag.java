package com.selectmakeathon.app.ui.main.problems.ProbFragmentPack;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.ProblemStatements;
import com.selectmakeathon.app.model.ProblemTrack;
import com.selectmakeathon.app.ui.main.MainActivity;
import com.selectmakeathon.app.ui.main.problems.ProblemActivity;
import com.selectmakeathon.app.ui.main.problems.ProblemChooseListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtificialFrag extends androidx.fragment.app.Fragment implements ProblemChooseListener {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    public List<ProblemStatements> list;
    public List<ProblemStatements> templist;
    private ListAdapter mAdapter;
    private ImageView backButton;
    private RecyclerView statementRecycle;
    private ProbHomeFrag.ListAdapter mListAdapter;
    public int mExpandedPosition= -1;
    public int previousExpandedPosition = -1;



    public ArtificialFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

        mDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference();
        final ArrayList<ProblemStatements> problemStatements = new ArrayList<>();

        ((ProblemActivity)getActivity()).startAnimation();

        mDatabaseReference.child("problems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ((ProblemActivity)getActivity()).stopAnimation();

                list=new ArrayList<ProblemStatements>();
                templist=new ArrayList<ProblemStatements>();
                try {
                    int i=0;
                    ProblemTrack listTrack=dataSnapshot.child("AI").getValue(ProblemTrack.class);
                    templist=listTrack.getProblemStatements();
                    for(i=0;i<templist.size();i++) {
                        ProblemStatements dp = new ProblemStatements();
                        String statement = templist.get(i).getProblemStatement();
                        String id=templist.get(i).getId();
                        String details=templist.get(i).getDetails();
                        dp.setProblemStatement(statement);
                        dp.setId(id);
                        dp.setDetails(details);
                        list.add(dp);
                    }
                    //i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAdapter=new ListAdapter(list,ArtificialFrag.this,((ProblemActivity)getActivity()).toContinue);
                statementRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
                statementRecycle.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_artificial, container, false);
        statementRecycle=view.findViewById(R.id.RecyclerStatement);
        backButton=view.findViewById(R.id.bkProb);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome=new Intent(getActivity(), ProblemActivity.class);
                startActivity(backHome);
            }
        });
        return view;
    }

    //Adapter Class
    public class ListAdapter extends RecyclerView.Adapter<ArtificialFrag.ListAdapter.ViewHolder> {
        private List<ProblemStatements> dataList;
        private ProblemChooseListener mListener;
        private boolean toContinue;

        public ListAdapter(List<ProblemStatements> dataList, ProblemChooseListener listener, boolean toContinue) {
            this.dataList = dataList;
            mListener = listener;
            this.toContinue = toContinue;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView StatementPrev;
            TextView StatIdPrev;
            LinearLayout expands;
            TextView details;
            Button choose;

            public ViewHolder(View itemView) {
                super(itemView);
                this.StatIdPrev=(TextView)itemView.findViewById(R.id.StatId);
                this.StatementPrev = (TextView) itemView.findViewById(R.id.StatName);
                this.expands=(LinearLayout)itemView.findViewById(R.id.ExpandArea);
                this.details=(TextView)itemView.findViewById(R.id.StatDeet);
                this.choose=(Button)itemView.findViewById(R.id.ChoooseProb);
            }
        }

        public ArtificialFrag.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_statement_card, parent, false);

            ArtificialFrag.ListAdapter.ViewHolder viewHolder = new ArtificialFrag.ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        public void onBindViewHolder(ArtificialFrag.ListAdapter.ViewHolder holder, final int position) {
            holder.StatementPrev.setText(dataList.get(position).getProblemStatement());
            holder.StatIdPrev.setText(dataList.get(position).getId());
            final boolean isExpanded = position==mExpandedPosition;
            holder.expands.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            final String id=dataList.get(position).getId();
            holder.details.setText(dataList.get(position).getDetails());

            if (isExpanded)
                previousExpandedPosition = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:position;
                    notifyItemChanged(previousExpandedPosition);
                    notifyItemChanged(position);

                }
            });

            if (toContinue) {
                holder.choose.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.choose.setVisibility(View.GONE);
            }

            holder.choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onChoose(id);
                }
            });
        }

        public int getItemCount() {

            return dataList.size();
        }

    }
    private void moveToAbstract(String id)
    {
        ((ProblemActivity)getActivity()).sendToAbstract(id);
    }


    @Override
    public void onChoose(String id) {
        moveToAbstract(id);
    }

}
