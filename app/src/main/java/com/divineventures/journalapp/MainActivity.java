package com.divineventures.journalapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView T_name, T_mail;
    private ImageView pic;
    private FloatingActionButton fab;
    int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // T_name = findViewById(R.id.userName);
        //T_mail = findViewById(R.id.userMail);
        pic = findViewById(R.id.userPix);
        fab = findViewById(R.id.fab);

        // setting onClickListeners for the buttons
        fab.setOnClickListener(this);
        pic.setOnClickListener(this);

        //get instance of FirebaseAuth
        auth = FirebaseAuth.getInstance();

        setUserPic();
    }

    public void getUserDetails() {
        //get user details
        //String name = getIntent().getExtras().getString("userName");
        String email = getIntent().getExtras().getString("userMail");
        //String img_url = getIntent().getExtras().getString("userImage");

        //display user details
        //T_name.setText(name);
        T_mail.setText(email);
        // Picasso.get().load(img_url).into(pic);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.userPix:
                userPopUp();
                break;
            case R.id.fab:
                startActivity(new Intent(this,EntryActivity.class));
                break;
        }
    }

    public void userPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, pic);
        popupMenu.getMenuInflater().inflate(R.menu.menu_user_options, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.change_pic:
                        selectImage();
                        break;
                    case R.id.log_out:
                        userLogOut();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Profile Picture");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camIntent, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();

                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                pic.setImageBitmap(bmp);

            } else if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                pic.setImageURI(uri);
            }
        }
    }

    public void setUserPic() {
        ActionBar actionBar = getSupportActionBar();
        LinearLayout linearLayout = findViewById(R.id.custom_layout);
        linearLayout.removeView(pic);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(70, 70, Gravity.END
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        assert actionBar != null;
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        pic.setLayoutParams(layoutParams);
        actionBar.setCustomView(pic);
    }

    public void userLogOut() {
        auth.signOut();
        finish();
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = getIntent().getStringExtra("Entry");
    if (text != null) {
        recyclerView = findViewById(R.id.entry_list);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        EntryData data = new EntryData(text, date, strDate);

        EntryAdapter entryAdapter = new EntryAdapter(this, data);
        recyclerView.setAdapter(entryAdapter);
    }

    }
}
