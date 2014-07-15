package th.co.thinknet.atit.tnfingerscanviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by atit on 7/15/14 AD.
 */
public class UserData {
    private static UserData ourInstance = new UserData();

    private final String KEY_SHAREDPREFS = "th.co.thinknet.tnfingerscanviewer";
    private final String KEY_DATA = "FSDataModel";

    private SharedPreferences mPrefs;
    private Gson gson;

    private FSDataModel dataModel;

    private boolean isInitialize;

    public static UserData getInstance() {
        return ourInstance;
    }


    private UserData() {
        isInitialize = false;
        gson = new Gson();
    }

    public void initialize(Context m_context){
        mPrefs = m_context.getSharedPreferences(KEY_SHAREDPREFS,Context.MODE_PRIVATE);

        String jsonData = mPrefs.getString(KEY_DATA,null);

        if(jsonData == null){
            Log.d("initData", "hasn't data");
            dataModel = null;
        }
        else{
            Log.d("initData","has data");
            dataModel = gson.fromJson(jsonData, FSDataModel.class);
        }

        isInitialize = true;
    }

    public void saveData(){
        if(isInitialize){
            String jsonData = gson.toJson(dataModel);
            //Log.d("saveData",jsonData);
            mPrefs.edit().putString(KEY_DATA,jsonData).commit();
        }
    }

    public FSDataModel getDataModel(){
        if(isInitialize)
            return dataModel;
        else
            return null;
    }

    public void setDataModel(FSDataModel dataModel){
        this.dataModel = dataModel;
    }
}
