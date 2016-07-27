package com.example.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.webservice.MyApplication;

import android.content.Context;
import android.content.res.AssetManager;

public class FileSp {

	@SuppressWarnings("finally")
	public static byte[] read(String fileName) {
		if (fileName == null || fileName.length() <= 0) {
			return null;
		}

		byte[] buffer = null;
		
		try {

			InputStream fin = MyApplication.getInstance().getAssets().open("uploader"+fileName);
			int length = fin.available();

			buffer = new byte[length];

			fin.read(buffer);

			fin.close();

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		finally
		{
			return buffer;
		}
	}
	
	public static boolean isExist(String filePath)
	{
		AssetManager asset = MyApplication.getInstance().getAssets();
		try {
			asset.open("uploader"+filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
