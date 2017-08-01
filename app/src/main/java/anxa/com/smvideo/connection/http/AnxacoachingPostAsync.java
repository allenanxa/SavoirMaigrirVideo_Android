package anxa.com.smvideo.connection.http;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by allenanxa on 6/16/2017.
 */

public class AnxacoachingPostAsync extends AsyncTask<String,String, String> {
    AnxacoachingHttpRequest jsonParser = new AnxacoachingHttpRequest();
    public AsyncResponse Delegate;
    private ProgressDialog pDialog;
    private String ApiUrl = "";
    Gson gson = new Gson();

    private static final String TAG_MESSAGE = "message";
    Class<?> classType;

    public AnxacoachingPostAsync( String url)
    {
        ApiUrl = url;

    }


    public AnxacoachingPostAsync( AsyncResponse delegate, String url, Class<?> ct)
    {
        ApiUrl = url;
        classType = ct;
        Delegate = delegate;
    }




    @Override
    protected void onPreExecute() {

    }



    @Override
    protected  String doInBackground(String... params) {

        try {
            Log.d("request", "starting");
            return jsonParser.makeHttpRequest( ApiUrl , "POST", params[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {

        Object obt = gson.fromJson(response, classType);
        System.out.println("onPostExecute: " + response);
        System.out.println("onPostExecute1 : " + obt);
        Delegate.processFinish(obt);

    }
}