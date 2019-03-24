package com.selectmakeathon.app.ui.main.idea.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selectmakeathon.app.R;
import com.selectmakeathon.app.ui.main.idea.AbstractActivity;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.util.OnComponentSelectedListener;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.util.RoundedBottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ComponentsBottomSheetFragment extends RoundedBottomSheetDialogFragment implements OnComponentSelectedListener {

    private static final String ARG_COMPONENTS = "components";

    RecyclerView rvComponents;
    ArrayList<String> components = new ArrayList<>();

    public ComponentsBottomSheetFragment() {

    }

    public static ComponentsBottomSheetFragment newInstance(ArrayList<String> components) {

        ComponentsBottomSheetFragment componentsBottomSheetFragment = new ComponentsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_COMPONENTS, components);
        componentsBottomSheetFragment.setArguments(args);
        return componentsBottomSheetFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            components = getArguments().getStringArrayList(ARG_COMPONENTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_components_bottomsheet, container, false);

        rvComponents = view.findViewById(R.id.rv_bottomsheet_components);
        rvComponents.setHasFixedSize(true);
        rvComponents.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComponents.setAdapter(new ComponentsBottomSheetAdapter(components, this));

        return view;

    }

    @Override
    public void onSelected(String component) {
        ((AbstractActivity)getActivity()).onComponentSelected(component);
        dismiss();
    }
}
