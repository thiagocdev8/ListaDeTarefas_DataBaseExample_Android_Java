package listadetarefas.db;

import android.provider.BaseColumns;

public class TaskContract
{
    public static final String DB_Name = "com.example.listadetarefas.db";
    public static final int DB_Version = 1;
    public static final String Table = "tarefas";

    public class Columns
    {
        public static final String tarefa = "tarefa";
        public static final String _id = BaseColumns._ID;

    }
}
