package br.com.edilsoncorrea.carregador.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Edilson on 02/05/2015.
 */
public class CarregarBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Battery", "Carregado no boot");
        Toast.makeText(context, "Carregado no boot", Toast.LENGTH_LONG).show();

    }
}
