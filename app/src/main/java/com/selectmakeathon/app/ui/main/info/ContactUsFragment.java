package com.selectmakeathon.app.ui.main.info;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selectmakeathon.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends androidx.fragment.app.Fragment {

    private TextView textEmail;
    private TextView textNumber;

    private ImageView imageBroswer;
    private ImageView imageFb;
    private ImageView imageInsta;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textEmail = view.findViewById(R.id.contact_text_button_email);
        textNumber = view.findViewById(R.id.contact_text_button_number);

        imageBroswer = view.findViewById(R.id.contact_img_browser);
        imageFb = view.findViewById(R.id.contact_img_facebook);
        imageInsta = view.findViewById(R.id.contact_img_insta);

        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "makeinvit@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Contact us"));
            }
        });

        textNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9097513482"));
                startActivity(intent);
            }
        });

        imageBroswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("http://www.selectmakeathon.com/");
            }
        });

        imageFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.facebook.com/Selectvit/");
            }
        });

        imageInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl("https://www.instagram.com/selectmakeathon/");
            }
        });
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
