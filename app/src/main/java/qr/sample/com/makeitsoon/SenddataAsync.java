package qr.sample.com.makeitsoon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by dhiraj.kumar on 2/28/2017.
 */

/*public class SenddataAsync extends AsyncTask<String, Integer, Void> {

    private ProgressDialog progressDialog;
    private Context context;
    private String url = "";
    String list;
    String value;

    public SenddataAsync(Context context, String url, String value) {
        this.context = context;
        this.url = url;
        this.value = value;

    }

    @Override
    protected void onPreExecute() {
        try {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait....");
            progressDialog.show();
            super.onPreExecute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... arg0) {
        rating(url, value);
        return null;
    }

    @SuppressWarnings("static-access")
    private void rating(String url, String value) {
        try {
            Sendbackparser parser = new Sendbackparser();
            list = parser.parseVector(url, value);
        }catch (Exception E){
            E.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try{
            progressDialog.dismiss();
        }catch (Exception E){
            E.printStackTrace();
        }

        // "Status":"Success","StatusCode":"0","Message":"Successful Submit Feedback.
        if ((list != null)&&(list.equals("")))
        {
            try {


            JSONObject object = new JSONObject(list.toString());
            if(object.getString("Status").equalsIgnoreCase("Success")) {
                //wrrite Your Code here
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        }

    }


}*/

