package Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteHelper extends SQLiteOpenHelper {
    // final String
    // CREATE_TABLE_SQL="create table myTable(_id integer primary key autoincrement,name,phone,mobile,email,post,addr,comp)";//创建数据表myTable
    // public static final String DB_NAME="personal_contacts";
    public static final String TABLE_NAME = "Staff";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SALARY = "salary";
    public static final String DEPART = "depart";
    private SQLiteDatabase sqLiteDatabase;

    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        sqLiteDatabase=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        // arg0.execSQL(CREATE_TABLE_SQL);
        arg0.execSQL("create table " + TABLE_NAME + " (" + ID
                + " integer primary key," + NAME + " varchar," + SALARY
                + " varchar," + DEPART + " varchar)");
        Log.d("sqlite","oncreate complete");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    /**
     * 增加数据。
     */
    public long add(String name, String salary, String depart) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("salary", salary);
        values.put("depart", depart);
        return sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    /**
     * 删除表里与id对应的数据。
     */
    public long delete(String id) {
        return sqLiteDatabase.delete(TABLE_NAME, "id = ? ", new String[]{id});
    }
    /**
     * 查询数据。
     */
    public Cursor searchall() {
        return getReadableDatabase().rawQuery("select * from Staff", null);
    }
    /**
     * 查询数据。
     */
    public Cursor search(String arg) {
        return getReadableDatabase().rawQuery("select * from Staff Where name like ? OR salary like ? OR depart like ?", new String[]{"%"+arg+"%","%"+arg+"%","%"+arg+"%"});
    }
}