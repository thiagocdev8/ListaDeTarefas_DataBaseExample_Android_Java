package listadetarefas.example.com;

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
import android.widget.SimpleCursorAdapter.ViewBinder;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listadetarefas.R;
import listadetarefas.db.TaskContract;
import listadetarefas.db.TaskDBHelper;

public class MainActivity extends AppCompatActivity
{
    private TaskDBHelper taskDBHelper;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        UpdateUI();

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adicionarItem(v);
            }
        });



    }

    private void UpdateUI()
    {
        taskDBHelper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = taskDBHelper.getReadableDatabase();
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

        listAdapter.setViewBinder(new ViewBinder()
        {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex)
            {
                if (view.getId() == R.id.buttonExcluir)
                {
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

    public void adicionarItem(View view)
    {

        AlertDialog.Builder alertAdicionarItem = new AlertDialog.Builder(this);
        alertAdicionarItem.setTitle("Adicione uma tarefa");
        alertAdicionarItem.setMessage("O que você precisa fazer?");
        final EditText inputfield = new EditText(this);
        alertAdicionarItem.setView(inputfield);


        alertAdicionarItem.setPositiveButton("Adicionar",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        String tarefa = inputfield.getText().toString();
                        Log.d("MainActivity", tarefa);

                        taskDBHelper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = taskDBHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.tarefa, tarefa);

                        db.insertWithOnConflict(TaskContract.Table, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        UpdateUI();
                    }
                });

        alertAdicionarItem.setNegativeButton("Cancelar", null);

        alertAdicionarItem.create().show();
    }

    public void apagarItem(long id)
    {
        taskDBHelper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = taskDBHelper.getWritableDatabase();

        sqlDB.delete(
                TaskContract.Table,
                TaskContract.Columns._id + "=?",
                new String[]{String.valueOf(id)}
        );
        UpdateUI();
    }
}