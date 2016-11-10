package com.jobnotes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jobnotes.Utility.AlertActivity;

public class DBHolder {
    Context context;
    Activity activity;
    AlertActivity alertActivity = new AlertActivity();
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_REPORT_DATA, MySQLiteHelper.COLUMN_REPORT_ID, MySQLiteHelper.COLUMN_LOCATION_ID, MySQLiteHelper.COLUMN_PROPERTY_ID,MySQLiteHelper.COLUMN_DATE_TIME};
    private String[] allColumnsPhotos = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PROPERTY_ID, MySQLiteHelper.COLUMN_PATH};
    private String[] allColumnsNotes = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PROPERTY_ID, MySQLiteHelper.COLUMN_USER_ID, MySQLiteHelper.COLUMN_NOTES};
    private String[] allColumnsWorkStatus = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_PROPERTY_ID, MySQLiteHelper.COLUMN_USER_ID, MySQLiteHelper.COLUMN_STATUS};
    //private String[] productIds={MySQLiteHelper.COLUMN_PRODUCT_ID};
    private String[] reportColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_REPORT_DATA};

    public DBHolder(Context context, Activity activity) {
        dbHelper = new MySQLiteHelper(context);
        this.context = context;
        this.activity = activity;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public void addToTable(String reportdata,String report_id,String location_id,String property_id,String date_time) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_REPORT_DATA, reportdata);
        values.put(MySQLiteHelper.COLUMN_REPORT_ID, report_id);
        values.put(MySQLiteHelper.COLUMN_LOCATION_ID, location_id);
        values.put(MySQLiteHelper.COLUMN_PROPERTY_ID, property_id);
        values.put(MySQLiteHelper.COLUMN_DATE_TIME, date_time);

        database = dbHelper.getWritableDatabase();
        long insertId = database.insert(MySQLiteHelper.TABLE_REPORT, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPORT,
                allColumns, null, null,
                null, null, null);


        Log.v("insert cursor_count", cursor.getColumnCount() + "");
        while (cursor.moveToNext()) {
            Log.v("id", cursor.getString(0));
            Log.v(MySQLiteHelper.COLUMN_REPORT_DATA, cursor.getString(1));
            Log.v(MySQLiteHelper.COLUMN_REPORT_ID, cursor.getString(2));
            Log.v(MySQLiteHelper.COLUMN_LOCATION_ID, cursor.getString(3));
            Log.v(MySQLiteHelper.COLUMN_PROPERTY_ID, cursor.getString(4));
            Log.v(MySQLiteHelper.COLUMN_DATE_TIME, cursor.getString(5));

         }
        cursor.close();
        //  alertActivity.showAlert(activity, "Data has been saved sucessfully", "Alert");
 }


    public void addNotes(String property_id, String user_id, String notes) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PROPERTY_ID, property_id);
        values.put(MySQLiteHelper.COLUMN_USER_ID, user_id);
        values.put(MySQLiteHelper.COLUMN_NOTES, notes);

        database = dbHelper.getWritableDatabase();
        long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumnsNotes, null, null,
                null, null, null);


        Log.v("insert cursor_count", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            Log.v("id", cursor.getString(0));
            Log.v(MySQLiteHelper.COLUMN_NOTES, cursor.getString(3));

        }
        cursor.close();

        //  alertActivity.showAlert(activity, "Data has been saved sucessfully", "Alert");

    }

    public void addPhotos(String property_id, String image_path) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PROPERTY_ID, property_id);
        values.put(MySQLiteHelper.COLUMN_PATH, image_path);

        database = dbHelper.getWritableDatabase();
        long insertId = database.insert(MySQLiteHelper.TABLE_PHOTOS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHOTOS,
                allColumnsPhotos, null, null,
                null, null, null);


        Log.v("insert cursor_count", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            Log.v("id", cursor.getString(0));
            Log.v(MySQLiteHelper.COLUMN_PATH, cursor.getString(2));

        }
        cursor.close();

        //  alertActivity.showAlert(activity, "Data has been saved sucessfully", "Alert");

    }

    public void addWorkStatus(String property_id, String user_id, String work_status) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PROPERTY_ID, property_id);
        values.put(MySQLiteHelper.COLUMN_USER_ID, user_id);
        values.put(MySQLiteHelper.COLUMN_STATUS, work_status);

        database = dbHelper.getWritableDatabase();
        long insertId = database.insert(MySQLiteHelper.TABLE_WORK_STATUS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORK_STATUS,
                allColumnsWorkStatus, null, null,
                null, null, null);


        Log.v("insert cursor_count", cursor.getCount() + "");
        while (cursor.moveToNext()) {
            Log.v("id", cursor.getString(0));
            Log.v(MySQLiteHelper.COLUMN_STATUS, cursor.getString(3));

        }
        cursor.close();

        //  alertActivity.showAlert(activity, "Data has been saved sucessfully", "Alert");

    }

    public Cursor GetAllRecords() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPORT,
                allColumns, null, null,
                null, null, null);

        return cursor;
    }


    public Cursor GetPropertyRecord(String report_id,String location_id,String property_id) {
        database = dbHelper.getWritableDatabase();
      /*  Cursor cursor = database.query(MySQLiteHelper.TABLE_REPORT,
                allColumns, MySQLiteHelper.COLUMN_REPORT_ID + "='"+report_id+"' AND "+MySQLiteHelper.COLUMN_LOCATION_ID + " = '"+location_id+"' AND "+MySQLiteHelper.COLUMN_PROPERTY_ID + " = '"+property_id+"'", null,
                null, null, null);*/



        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPORT,
                allColumns,"report_id = ? AND location_id = ?AND property_id = ?", new String[] {report_id, location_id,property_id } ,
                null, null, null);
   //   "KEY_WORKOUT = ? AND KEY_EXERCISE = ?"
        return cursor;
    }

    public  Cursor GetAllNotes() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumnsNotes, null, null,
                null, null, null);

        return cursor;
    }
    public Cursor GetPropertyNotes(long property_id) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumnsNotes, MySQLiteHelper.COLUMN_PROPERTY_ID + " = "+property_id, null,
                null, null, null);

        return cursor;
    }
    public Cursor GetAllPhotos() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHOTOS,
                allColumnsPhotos, null, null,
                null, null, null);

        return cursor;
    }

    public Cursor GetAllWorkStatus() {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORK_STATUS,
                allColumnsWorkStatus, null, null,
                null, null, null);

        return cursor;
    }


    public void deletePhotos(String id) {
        database = dbHelper.getWritableDatabase();
        database.delete(MySQLiteHelper.TABLE_PHOTOS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHOTOS,
                allColumnsPhotos, null, null,
                null, null, null);


        Log.v("delete cursor_count", cursor.getCount() + "");
        cursor.close();
    }


    public void deleteWorkStatus(String id) {
        database = dbHelper.getWritableDatabase();
        database.delete(MySQLiteHelper.TABLE_WORK_STATUS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORK_STATUS,
                allColumnsWorkStatus, null, null,
                null, null, null);


        Log.v("delete cursor_count", cursor.getCount() + "");
        cursor.close();
    }

    public void deleteReport(String id) {
        database = dbHelper.getWritableDatabase();
        database.delete(MySQLiteHelper.TABLE_REPORT, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_REPORT,
                allColumns, null, null,
                null, null, null);


        Log.v("delete cursor_count", cursor.getCount() + "");
        cursor.close();
    }

    public void deleteNote(String id) {
        database = dbHelper.getWritableDatabase();
        database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID + " = " + id, null);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES,
                allColumnsNotes, null, null,
                null, null, null);

        Log.v("delete cursor_count", cursor.getCount() + "");
        cursor.close();
    }

    public void deleteAll() {
        database = dbHelper.getWritableDatabase();
        database.delete(MySQLiteHelper.TABLE_NOTES, null, null);
        database.delete(MySQLiteHelper.TABLE_REPORT, null, null);
        database.delete(MySQLiteHelper.TABLE_PHOTOS, null, null);
        database.delete(MySQLiteHelper.TABLE_WORK_STATUS, null, null);


    }
 /*public double getSumOfTip(){
     double TotalTip = 0;
	  database = dbHelper.getWritableDatabase();
	  Cursor c=database.rawQuery("SELECT sum("+MySQLiteHelper.COLUMN_TIP_AMOUNT+") FROM "+MySQLiteHelper.TABLE_TipGiven+"",null);
	  System.out.println(c.getCount());
	 if (c.getCount()>0) {
		 while (c.moveToNext()) {
				//Log.v("tip total", c.getString(0));
				
				TotalTip=c.getDouble(0);
			}
	} 
	  
		    c.close();
	 return TotalTip;
  }
 public double getSumOfBill(){
	 double TotalBill = 0;
	  database = dbHelper.getWritableDatabase();
	  Cursor c=database.rawQuery("SELECT sum("+MySQLiteHelper.COLUMN_GRAND_TOTAL+") FROM "+MySQLiteHelper.TABLE_TipGiven+"",null);
	 if (c.getCount()>0) {
		 while (c.moveToNext()) {
			//	Log.v("bill total", c.getString(0));
				
				TotalBill=c.getDouble(0);
			}
	}
	  
		    c.close();
	  return TotalBill;
 }
 
 public ArrayList<Float>  getMonthTotal(String type) {
	 ArrayList<Float> alAmount=new ArrayList<Float>();
	 
	  database = dbHelper.getWritableDatabase();
	  //Cursor c=database.rawQuery("SELECT sum("+MySQLiteHelper.COLUMN_TIP_AMOUNT+","+MySQLiteHelper.COLUMN_MONTH+")FROM "+MySQLiteHelper.TABLE_TipGiven,null);
	  Cursor c=database.rawQuery("SELECT sum("+type+"),"+MySQLiteHelper.COLUMN_MONTH+" FROM "+MySQLiteHelper.TABLE_TipGiven +" GROUP BY "+MySQLiteHelper.COLUMN_MONTH,null);
	  //+" GROUP BY "+MySQLiteHelper.COLUMN_MONTH
	  if (c.getCount()>0) {
		
	
	  while (c.moveToNext()) {
			//Log.v("Tip", c.getString(0));
			
			
			switch (c.getInt(1)) {
			case 0:
				//jan
				jan=c.getFloat(0);
			//	alAmount.add(c.getDouble(0));
				break;

			case 1:
				//feb
				feb=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 2:
				//mar
				mar=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 3:
				//apr
				apr=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 4:
				//may
				may=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 5:
				//jun
				june=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 6:
				//july
				july=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 7:
				//augest
				aug=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 8:
				//sept
				sept=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 9:
				//oct
				oct=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			case 10:
				//nov
				nov=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;
			case 11:
				//dec
				dec=c.getFloat(0);
				//alAmount.add(c.getDouble(0));
				break;

			default:
				break;
			}
		}
	  }
		    c.close();
		    
		    alAmount.add(jan);
		    alAmount.add(feb);
		    alAmount.add(mar);
		    alAmount.add(apr);
		    alAmount.add(may);
		    alAmount.add(june);
		    alAmount.add(july);
		    alAmount.add(aug);
		    alAmount.add(sept);
		    alAmount.add(oct);
		    alAmount.add(nov);
		    alAmount.add(dec);
		    
			return alAmount;
}*/
 
/*      public boolean SearchProductItems(String productId){
    	  boolean isPresentItem=false;
    	  database = dbHelper.getWritableDatabase();
    	  Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
    	          allColumns, MySQLiteHelper.COLUMN_PRODUCT_ID + " = " + productId, null,
    	          null, null, null);
    	   while (cursor.moveToNext()) {
    			Log.v("id", cursor.getString(0));
    			Log.v("rest_id", cursor.getString(1));
    			Log.v("branch_id", cursor.getString(2));
    			Log.v("product_id", cursor.getString(3));
    			Log.v("quantity", cursor.getString(4));
    			
    		}
    	     if (cursor.getCount()>0) {
    	    	 isPresentItem= true;  
			}else if (cursor.getCount()<=0) {
				isPresentItem= false;  
			}
    	      
    	      cursor.close();
			return isPresentItem;  
      }*/
      /*   public void UpdateCartItems(String productId, String qunatity) {
    	    ContentValues values = new ContentValues();
    	   
    	    values.put(MySQLiteHelper.COLUMN_QUANTITY, qunatity);
    	    database = dbHelper.getWritableDatabase();
    	    database.update(MySQLiteHelper.TABLE_AddToCart, values, MySQLiteHelper.COLUMN_PRODUCT_ID + " = "+productId,null);
    	   // long insertId = database.insert(MySQLiteHelper.TABLE_AddToCart, null,values);
    	    Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
    	        allColumns,null, null,
    	        null, null, null);

    	   
    	    Log.v("update cursor_count",  cursor.getCount()+"");
    	   while (cursor.moveToNext()) {
    		Log.v("id", cursor.getString(0));
    		Log.v("rest_id", cursor.getString(1));
    		Log.v("branch_id", cursor.getString(2));
    		Log.v("product_id", cursor.getString(3));
    		Log.v("quantity", cursor.getString(4));
    		
    	}
    	    cursor.close();
    	    Toast.makeText(this.context,"Product is Updated to cart sucessfully", Toast.LENGTH_LONG).show();
    	  }

      public Cursor GetProductItems(){
    	  database = dbHelper.getWritableDatabase();
    	  Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
    			  productIds,null, null,
    	          null, null, null);
   
			return cursor;  
      }
      public String  GetProductQuantity(String productId){
    	  String quantity="";
    	  database = dbHelper.getWritableDatabase();
    	  Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
    			  productQuantity,MySQLiteHelper.COLUMN_PRODUCT_ID + " = "+productId, null,
    	          null, null, null);
    	  while (cursor.moveToNext()) {
      		
      		Log.v("quantity", cursor.getString(0));
      		quantity=cursor.getString(0);
      	}
			return quantity;  
      }
      public void deleteCartItem(String productId) {
    	    database = dbHelper.getWritableDatabase();
    	    database.delete(MySQLiteHelper.TABLE_AddToCart, MySQLiteHelper.COLUMN_PRODUCT_ID + " = "+productId, null);
    	    Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
    		        allColumns,null, null,
    		        null, null, null);

    		   
    		    Log.v("delete cursor_count",  cursor.getCount()+"");
    		    cursor.close();
    	  }
  public void deleteAllCartItem() {
    database = dbHelper.getWritableDatabase();
    database.delete(MySQLiteHelper.TABLE_AddToCart, null, null);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_AddToCart,
	        allColumns,null, null,
	        null, null, null);

	   
	    Log.v("delete cursor_count",  cursor.getCount()+"");
	    cursor.close();
  }*/
/*
  public List<Comment> getAllComments() {
    List<Comment> comments = new ArrayList<Comment>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Comment comment = cursorToComment(cursor);
      comments.add(comment);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return comments;
  }

  private Comment cursorToComment(Cursor cursor) {
    Comment comment = new Comment();
    comment.setId(cursor.getLong(0));
    comment.setComment(cursor.getString(1));
    return comment;
  }*/
}