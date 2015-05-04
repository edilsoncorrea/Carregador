package br.com.edilsoncorrea.carregador.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import br.com.edilsoncorrea.carregador.tarefas.EnviaComando;

/**
 * Created by Edilson on 02/05/2015.
 */
public class NivelBateria extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //context.unregisterReceiver(this);
        Log.i("Battery", ":: IN RECEIVER");

        int rawlevel = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        int level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }

        Log.i("Battery", "Battery Level Remaining: " + level + "%");
        Toast.makeText(context, "Battery Level Remaining: " + level + "%", Toast.LENGTH_LONG).show();
        //btnConsumer.setText("Battery Level Remaining: " + level + "%");


        if (level < 30) {
            EnviaComando ligar =  new EnviaComando(context, true);
            ligar.execute();
        }

        if (level > 35) {
            EnviaComando ligar =  new EnviaComando(context, false);
            ligar.execute();
        }

    }
}
