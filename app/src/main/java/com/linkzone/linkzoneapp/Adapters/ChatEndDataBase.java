package com.linkzone.linkzoneapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatEndDataBase extends SQLiteOpenHelper
{
    private static SQLiteDatabase databaseRead;
    private static SQLiteDatabase databaseWrite;
    private static String CHAT_TABLE="chatTable";
    private static String GROUP_CHAT_TABLE="groupChatTable";

    public ChatEndDataBase(Context context)
    {
        super(context, "chatIndDb.db", null, 1);
        this.databaseRead=getReadableDatabase();
        this.databaseWrite=getWritableDatabase();
        Log.d("Database","Created..");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE contactList(contactJson TEXT);");
        db.execSQL("CREATE TABLE groupList(groupJson TEXT);");

        db.execSQL("CREATE TABLE groupChatTable(" +
                "msgId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "groupId TEXT," +
                "senderId TEXT," +
                "senderName TEXT," +
                "senderImage TEXT," +
                "message TEXT," +
                "mediaPath TEXT," +
                "msgType TEXT," +
                "isSeen INTEGER," +
                "dateTime TEXT);");

        db.execSQL("CREATE TABLE chatTable(" +
                "msgId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senderId TEXT," +
                "receiverId TEXT," +
                "message TEXT," +
                "mediaPath TEXT," +
                "msgType TEXT," +
                "isSeen INTEGER," +
                "dateTime TEXT);");
        Log.d("Table","Created..");
    }

    public static void deleteChat(String senderId, String receiverId){
        String query = "delete from chatTable where (senderId='"+senderId+"' and receiverId='"+receiverId+"') OR (senderId='"+receiverId+"' and receiverId='"+senderId+"')";
        databaseRead.execSQL(query);
    }

    public static void insertChat(String senderId,String receiverId,String message,String mediaPath,String msgType,String isSeen,String dateTime)
    {
        ContentValues conv=new ContentValues();
        conv.put("senderId",senderId);
        conv.put("receiverId",receiverId);
        conv.put("message",message);
        conv.put("mediaPath",mediaPath);
        conv.put("msgType",msgType);
        conv.put("isSeen",isSeen);
        conv.put("dateTime",dateTime);
        databaseWrite.insert("chatTable",null,conv);
        Log.d("Chat","Inserted..");
    }

    public static Cursor getChats(String senderId,String receiverId)
    {
        Cursor c=null;
        try
        {
            String query="SELECT msgId,senderId,receiverId,message,mediaPath,msgType,dateTime FROM chatTable WHERE senderId='"+ senderId +"' AND receiverId='"+ receiverId +"' OR senderId='"+ receiverId +"' AND receiverId='"+ senderId +"' ORDER BY msgId ASC";
            c=databaseRead.rawQuery(query,null);
            Log.e("Query",query);
            Log.e("Chats",c.getCount()+"");
        }catch (Exception e)
        {
            Log.e("Chats",e.getMessage());
        }
        return c;
    }

    public static int getBadgesCount(String senderId,String receiverId)
    {
        Cursor c=null;
        try
        {
            String query="SELECT message FROM chatTable WHERE isSeen=0 AND senderId='"+ senderId +"' AND receiverId='"+ receiverId +"' OR isSeen=0 AND senderId='"+ receiverId +"' AND receiverId='"+ senderId +"'";
            c=databaseRead.rawQuery(query,null);
            Log.e("Query",query);
            Log.e("Chats",c.getCount()+"");
        }catch (Exception e)
        {
            Log.e("Chats",e.getMessage());
        }
        return c.getCount();
    }

    public static Cursor getlastMessage(String senderId,String receiverId)
    {
        Cursor c=null;
        try
        {
            String query="SELECT msgId,message,dateTime,msgType FROM chatTable WHERE senderId='"+ senderId +"' AND receiverId='"+ receiverId +"' OR senderId='"+ receiverId +"' AND receiverId='"+ senderId +"' ORDER BY msgId DESC LIMIT 1";
            c=databaseRead.rawQuery(query,null);
            Log.e("Query",query);
            Log.e("Chats",c.getCount()+"");
        }catch (Exception e)
        {
            Log.e("Chats",e.getMessage());
        }
        return c;
    }

    public static void setAllSeen(String senderId,String receiverId)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("isSeen",1);
        databaseWrite.update("chatTable",contentValues,"senderId='"+ senderId +"' AND receiverId='"+ receiverId +"' OR senderId='"+ receiverId +"' AND receiverId='"+ senderId +"'",null);
        Log.e("Chats","All Seen Updated");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public static void deleteContactData()
    {
        databaseWrite.execSQL("DELETE FROM contactList");
        Log.d("Contact","Deleted..");
    }

    public static void deleteGroupData()
    {
        databaseWrite.execSQL("DELETE FROM groupList");
        Log.d("Group","Deleted..");
    }

    public static void saveContacts(String json)
    {
        deleteContactData();
        ContentValues contentValues=new ContentValues();
        contentValues.put("contactJson",json);
        databaseWrite.insert("contactList",null,contentValues);
        Log.d("Contact","Inserted..");
    }

    public static void saveGroup(String json)
    {
        deleteGroupData();
        ContentValues contentValues=new ContentValues();
        contentValues.put("groupJson",json);
        databaseWrite.insert("groupList",null,contentValues);
        Log.d("Group","Inserted..");
    }

    public static String getGroups()
    {
        String result="null";
        Cursor c=databaseRead.rawQuery("SELECT * FROM groupList",null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            result=c.getString(0);
        }
        Log.d("Group Data",c.getCount() + "  "+result);
        return result;
    }

    public static String getContacts()
    {
        String result="null";
        Cursor c=databaseRead.rawQuery("SELECT * FROM contactList",null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            result=c.getString(0);
        }
        Log.d("Contact Data",c.getCount() + "  "+result);
        return result;
    }
}
