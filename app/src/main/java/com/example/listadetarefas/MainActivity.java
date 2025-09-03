package com.example.listadetarefas;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.listadetarefas.db.TaskContract;
import com.example.listadetarefas.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity {


    private TaskDBHelper helper;

    private Button button;

    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        updateUI();

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarItem(v);
            }
        });



    }

    private void updateUI() {

        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.Table,
                new String[]{TaskContract.Columns._id, TaskContract.Columns.tarefa},
                null, null, null, null, null);


        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.celula,
                cursor,
                new String[] {TaskContract.Columns.tarefa, TaskContract.Columns._id},
                new int[] {R.id.textoCelula, R.id.buttonExcluir}, 0
        );

        listAdapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.buttonExcluir) {
                    final long id = cursor.getLong(cursor.getColumnIndex(TaskContract.Columns._id));

                    view.setOnClickListener(v -> apagarItem(id));
                    return true;
                }
                return false;
            }
        });
        ListView listView = findViewById(R.id.listaDeTarefas);
        listView.setAdapter(listAdapter);
    }

    public void adicionarItem(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicione uma tarefa");
        builder.setMessage("O que você precisa fazer?");
        final EditText inputfield = new EditText(this);
        builder.setView(inputfield);


        builder.setPositiveButton("Adicionar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String tarefa = inputfield.getText().toString();
                        Log.d("MainActivity", tarefa);

                        helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.tarefa, tarefa);

                        db.insertWithOnConflict(TaskContract.Table, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

        builder.setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    public void apagarItem(long id) {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();

        sqlDB.delete(
                TaskContract.Table,
                TaskContract.Columns._id + "=?",
                new String[]{String.valueOf(id)}
        );

        updateUI();
    }
}