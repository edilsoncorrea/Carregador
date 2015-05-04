package br.com.edilsoncorrea.carregador.tarefas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Edilson on 02/05/2015.
 */
public class EnviaComando  extends AsyncTask<Void, Void, Integer> {
    private final String USER_AGENT = "Mozilla/5.0";
    private final Boolean ligar;

    private final Context context;

    public EnviaComando(Context context, Boolean ligar)
    {
        this.context = context;
        this.ligar = ligar;
    }

    // HTTP GET request
    private void sendGet(Context context, Boolean ligar) throws Exception {
        String url = "http://192.168.2.110/?OFF";

        if (ligar) {
            url = "http://192.168.2.110/?ON";
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            sendGet(context, ligar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Integer result) {
        Log.i("CATEGORIA", "Concluido.");
    }

}

