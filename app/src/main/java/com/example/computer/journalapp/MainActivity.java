package com.example.computer.journalapp;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DbHelper dbHelper;
    ArrayAdapter arrayAdapter;
    ListView lstTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        lstTasks = (ListView)findViewById(R.id.listTasks);

        loadTasks();
    }

    private void loadTasks() {

        ArrayList<String> taskList = dbHelper.getTaskList();
        if(arrayAdapter==null)
        {
            arrayAdapter = new ArrayAdapter<String>(this,R.layout.tasks,R.id.taskTitle,taskList);
            lstTasks.setAdapter(arrayAdapter);
        }
        else
        {
            arrayAdapter.clear();
            arrayAdapter.addAll(taskList);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

//        change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
//                final EditText bodyEditText = new EditText(this);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What is in your mind?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = String.valueOf(taskEditText.getText());
//                                String body = String.valueOf(bodyEditText.getText());
                                dbHelper.insertNewTask(title);
                                loadTasks();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view)
    {
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)findViewById(R.id.taskTitle);
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTasks();
    }
}
