package com.jobnotes.imageloader1.imageloader;

import android.content.Context;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "JobNotes");
        else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();


      /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      	Date date = new Date();
      	if(!sharedPreferences.getString("TODAY_DATE", "").equals(dateFormat.format(date))) 
      		//toady date aur save date
          {
  	      	File[] f = cacheDir.listFiles();
          	for(File file:f)
          	{
          		file.delete();
          	}
//          today date save in shared preference
          	
          	System.out.println(dateFormat.format(date));
          	Editor editor =sharedPreferences.edit();
          	editor.putString("TODAY_DATE", dateFormat.format(date));
          	editor.commit();
          }*/
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}