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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import net.empoweringtechnology.volunteertracker.R;

public class Reports extends AppCompatActivity {

    private SimpleCursorAdapter dataAdapterReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PopulateVolunteeringReport();
        showTotalHours();
    }

    /* Set the total hours volunteered data on the report page */
    public void showTotalHours() {
        final net.empoweringtechnology.volunteertracker.MyDBHandler dbHandler = new net.empoweringtechnology.volunteertracker.MyDBHandler(this, null, null, 1);
        Cursor cursor = dbHandler.getTotalHours();
        String totalHours = cursor.getString(cursor.getColumnIndex(dbHandler.COLUMN_TOTAL_HOURS));
        if (totalHours == null) totalHours="0";
        TextView totHours = (TextView) findViewById(R.id.totalHours);
        totHours.setText(totalHours);
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

    /* method to populate report data */
    private void PopulateVolunteeringReport() {
        final net.empoweringtechnology.volunteertracker.MyDBHandler dbHandler = new net.empoweringtechnology.volunteertracker.MyDBHandler(this, null, null, 1);
        ListView lv = (ListView) findViewById(R.id.MyListView);

        Cursor cursor = dbHandler.FetchReportData();
        {

            // The desired columns to be bound
        }
        String[] reportcolumns = new String[]{
                //dbHandler.COLUMN_ROWID,
                dbHandler.COLUMN_ORG,
                dbHandler.COLUMN_ORG_HOURS
        };

        // the XML defined views which the data will be bound to
        int[] rep_to = new int[]{
                R.id.Reportorg,
                R.id.ReportHours,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapterReports = new SimpleCursorAdapter(
                this, R.layout.custom_report_row,
                cursor,
                reportcolumns,
                rep_to,
                0);

        final ListView listView = (ListView) findViewById(R.id.MyReportView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapterReports);
    }

    public void Gallery(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                "content://media/internal/images/media"));
        startActivity(intent);
    }


    public void FindVolunteering(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.VolunteeringEvents.class);
        startActivity(intent);
    }
}
