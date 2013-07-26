package me.karshan.memento;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

// Stores state of app in json private file
// {
//   "running": boolean   # a task is currently running (has been started but not stopped
// }
public class State {
	private static final String fname = "state.json";
	private static Context ctx;
	
	private static JSONObject getDefaultState() throws JSONException {
		JSONObject state = new JSONObject();
		state.put("running", false);
		return state;
	}

	private static FileInputStream openFileInput(String name) throws FileNotFoundException {
		return ctx.getApplicationContext().openFileInput(name);
	}
	
	private static FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
		return ctx.getApplicationContext().openFileOutput(name, mode);
	}
	
	private static String readAll(FileInputStream fis) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] buf = new byte[1024];
		int length;
		while ((length = fis.read(buf)) != -1) {
			out.append(new String(buf, 0, length));
		}
		return out.toString();
	}
	
	// Create file with default state if it doesn't exist
	private static void createFileNoTrunc() throws JSONException, IOException {
		try {
			openFileInput(fname);
		} catch (FileNotFoundException e) {
			writeState(getDefaultState());
			
		}
	}
	
	private static JSONObject getState() throws JSONException, IOException {
		createFileNoTrunc();
		FileInputStream fis = openFileInput(fname);
		return new JSONObject(readAll(fis));
	}
	
	private static void writeState(JSONObject state) throws IOException {
		FileOutputStream fos = openFileOutput(fname, Context.MODE_PRIVATE);
		fos.write(state.toString().getBytes());
	}

	public static void Init(Context ctx) {
		State.ctx = ctx;
	}
	
	public static boolean isRunning() throws JSONException, IOException {
		return getState().getBoolean("running");
	}
}
