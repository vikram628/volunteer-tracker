package net.empoweringtechnology.volunteertracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import net.empoweringtechnology.volunteertracker.R;

import static android.support.v4.app.ActivityCompat.startActivity;

public class VolunteeringEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.volunteermatch.org/");
        myWebView.setWebViewClient(new WebViewClient());


    }



    /* method to navigate to home page */
    public void GoHome(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.HomePage.class);
        startActivity(intent);
    }

    /* method to navigate to save activity page */
    public void addHours(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.MainActivity.class);
        startActivity(intent);
    }

}
