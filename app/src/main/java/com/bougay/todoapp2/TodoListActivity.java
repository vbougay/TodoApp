package com.bougay.todoapp2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    private static final String TAG = "TodoListActivity";

    private TodoDbHelper dbHelper;

    private ListView itemsList;
    private ArrayAdapter<String> itemAdapter;

    private void onAddTask()
    {
        final EditText edit = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(edit)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(edit.getText());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TodoDbContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TodoDbContract.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();

                        populateItemsList();

                        Log.d(TAG, "Task to add: " + task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void populateItemsList()
    {
        ArrayList<String> itemList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TodoDbContract.TaskEntry.TABLE,
                new String[]{TodoDbContract.TaskEntry._ID, TodoDbContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TodoDbContract.TaskEntry.COL_TASK_TITLE);
            itemList.add(cursor.getString(idx));
        }
        cursor.close();
        db.close();

        if (itemAdapter == null)
        {
            itemAdapter = new ArrayAdapter<String>(this, R.layout.content_todo_item,
                    R.id.task_title, itemList);
            itemsList.setAdapter(itemAdapter);
        }
        else
        {
            itemAdapter.clear();
            itemAdapter.addAll(itemList);
            itemAdapter.notifyDataSetChanged();
        }
    }

    public void deleteItem(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TodoDbContract.TaskEntry.TABLE,
                TodoDbContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();

        populateItemsList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddTask();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dbHelper = new TodoDbHelper(this);

        itemsList = (ListView) findViewById(R.id.list_todo);
        populateItemsList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_task:
                onAddTask();
                Log.d(TAG, "Add a new task");

                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
