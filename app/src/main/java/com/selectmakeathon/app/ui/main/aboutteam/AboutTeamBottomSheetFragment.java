package com.selectmakeathon.app.ui.main.aboutteam;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.selectmakeathon.app.R;
import com.selectmakeathon.app.model.DevModel;
import com.selectmakeathon.app.ui.main.idea.bottomsheet.util.RoundedBottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AboutTeamBottomSheetFragment extends RoundedBottomSheetDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottomsheet_devs, container, false);

        ArrayList<DevModel> devModels = new ArrayList<>();

        devModels.add(new DevModel(
                "Samarth Nayyar", "UI/UX Designer", R.drawable.ic_behance, "https://www.linkedin.com/in/samarth-nayyar-20816711b/"
        ));

        devModels.add(new DevModel(
                "Yaswant Narayan", "Android Developer", R.drawable.ic_android, "https://www.linkedin.com/in/yaswant-narayan/"
        ));

        devModels.add(new DevModel(
                "Sparsh Srivastava", "UI/UX Designer", R.drawable.ic_android, "https://www.linkedin.com/in/sparshsri/"
        ));

        devModels.add(new DevModel(
                "Sanil", "Android Developer", R.drawable.ic_android, "https://www.example.com/"
        ));

        devModels.add(new DevModel(
                "Samarth Nayyar", "UI/UX Designer", R.drawable.ic_behance, "https://www.example.com/"
        ));

        devModels.add(new DevModel(
                "Yaswant Narayan", "Android Developer", R.drawable.ic_android, "https://www.example.com/"
        ));

        RecyclerView recyclerView = view.findViewById(R.id.rv_dev_info);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AboutTeamAdapter(devModels, getContext()));

        return view;
    }
}
