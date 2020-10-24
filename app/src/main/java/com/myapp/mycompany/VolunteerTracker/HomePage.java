package net.empoweringtechnology.volunteertracker;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import net.empoweringtechnology.volunteertracker.MyDBHandler;

public class HomePage extends AppCompatActivity {

    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /* Populate the volunteering data on the home page */
        PopulateVolunteeringData();
    }

    /* method to navigate to Save Activity page */
    public void addHours(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.MainActivity.class);
        startActivity(intent);
    }

    /* method to navigate to Reports page */
    public void showReports(View view) {
        Intent intent = new Intent(this, net.empoweringtechnology.volunteertracker.Reports.class);
        startActivity(intent);
    }

    /* method to navigate to Reports page */
    private void PopulateVolunteeringData() {
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        ListView lv = (ListView) findViewById(R.id.MyListView);

        Cursor cursor = dbHandler.FetchVoluteerInfo();
        {

            // The desired columns to be bound
        }
        String[] columns = new String[]{
                //dbHandler.COLUMN_ROWID,
                dbHandler.COLUMN_ORG,
                dbHandler.COLUMN_ACTIVITY,
                dbHandler.COLUMN_DATE,
                dbHandler.COLUMN_HOURS
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.org,
                R.id.act,
                R.id.actdate,
                R.id.acthours,
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.custom_row,
                cursor,
                columns,
                to,
                0);

        final ListView listView = (ListView) findViewById(R.id.MyListView);
        final Cursor mycursor = cursor;
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        /* listener for clicks on the avaivity data to offer a dialog to edit or delete */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> listView, View view,
                                    int position, long id) {
                final int RoWIDEdit = (int) dataAdapter.getItemId(position);
                final String OrgValue = mycursor.getString(mycursor.getColumnIndex(dbHandler.COLUMN_ORG));
                final String actValue = mycursor.getString(mycursor.getColumnIndex(dbHandler.COLUMN_ACTIVITY));
                final String dateValue = mycursor.getString(mycursor.getColumnIndex(dbHandler.COLUMN_DATE));
                final String hoursValue = mycursor.getString(mycursor.getColumnIndex(dbHandler.COLUMN_HOURS));
                //final int positionToRemove = position;

                final ListView lv = (ListView) listView;

                AlertDialog.Builder dialog = new AlertDialog.Builder(HomePage.this);
                dialog.setTitle("Update");
                dialog.setMessage("Do you want to Edit or Delete?");
                dialog.setNegativeButton("Cancel", null);
                dialog.setNeutralButton("Edit", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(HomePage.this, net.empoweringtechnology.volunteertracker.MainActivity.class);
                        intent.putExtra("idValue", RoWIDEdit);
                        intent.putExtra("orgValue", OrgValue);
                        intent.putExtra("actValue", actValue);
                        intent.putExtra("dateValue", dateValue);
                        intent.putExtra("hoursValue", hoursValue);
                        startActivity(intent);
                    }
                });
                dialog.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.DeleteItem(RoWIDEdit);
                        dataAdapter.notifyDataSetChanged();
                        PopulateVolunteeringData();
                    }
                });
                dialog.show();

            }
        });
    }
}
