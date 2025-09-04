package listadetarefas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TaskDBHelper extends SQLiteOpenHelper
{

    public TaskDBHelper(Context context)
    {
        super(context, TaskContract.DB_Name, null, TaskContract.DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String sqlQuerry = String.format("CREATE TABLE %s ("+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT)", TaskContract.Table, TaskContract.Columns.tarefa);

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuerry);

        db.execSQL(sqlQuerry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.Table);
    }
}
