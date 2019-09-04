package z.houbin.xposed.lib;

import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import z.houbin.xposed.lib.log.Logs;

public class Config {
    private static File dir = new File(Environment.getExternalStorageDirectory(), "/.xposed.lib/");
    private static boolean isInit;

    public static void init(File configDir) {
        Config.dir = configDir;
        isInit = true;
    }

    public static void init(String packageName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/.xposed.lib/" + packageName);
            if (!file.exists()) {
                file.mkdirs();
            } else if (file.isFile()) {
                file.delete();
                file.mkdirs();
            }
            init(file);
        } catch (Exception e) {
            Logs.e(e);
        }
    }

    public static void save(String name, String data) {
        Files.writeFile(new File(dir, name), data);
    }

    public static String read(String name) {
        return Files.readFile(new File(dir, name));
    }

    public static void remove(String name) {
        Files.delete(new File(dir, name));
    }

    public static JSONObject readJSON(String name) {
        String data = read(name);
        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            try {
                return new JSONObject(data);
            } catch (JSONException e) {
                Logs.e(e);
            }
        }
        return null;
    }

    public static void saveJSON(String name, JSONObject json) {
        if (json != null) {
            save(name, json.toString());
        }
    }

    public static void saveConfig(JSONObject json) {
        saveJSON("config.json", json);
    }

    public static JSONObject readConfig() {
        return readJSON("config.json");
    }

    public static void writeLog(String log) {
        if (isInit) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ", Locale.CHINA);
            Files.appendFile(new File(dir, "temp.log"), sdf.format(d) + log);
        }
    }

    public static String readLog() {
        return Files.readFile(new File(dir, "temp.log"));
    }
}
