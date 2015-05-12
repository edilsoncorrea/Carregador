package br.com.edilsoncorrea.carregador.servico;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import br.com.edilsoncorrea.carregador.tarefas.EnviaComando;

/**
 * Created by Edilson on 04/05/2015.
 */
public class ServicoEnviarComando extends Service implements Runnable{
    private final String USER_AGENT = "Mozilla/5.0";
    private boolean ativo;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("Battery", "ExemploServico.onCreate()");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ativo = true;

        Log.i("Battery", "Servico Iniciado");

        new Thread(this, "ExemploServico-" + startId).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        batteryLevel();
        batteryLow();
        batteryCharged();
    }

    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                //context.unregisterReceiver(this);
                Log.i("Bateria", ":: Receiver");

                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }

                if (level < 20) {
                    EnviaComando ligar =  new EnviaComando(context, true);
                    ligar.execute();
                }

                Log.i("Battery", "Nivel restante da bateria: " + level + "%");
                Toast.makeText(context, "Nivel restante da bateria: " + level + "%", Toast.LENGTH_LONG).show();
                //btnConsumer.setText("Battery Level Remaining: " + level + "%");
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private void batteryLow() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                    EnviaComando ligar =  new EnviaComando(context, true);
                    ligar.execute();
            }
        };

        IntentFilter batteryLowFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryLevelReceiver, batteryLowFilter);
    }

    private void batteryCharged() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                EnviaComando ligar =  new EnviaComando(context, true);
                ligar.execute();
            }
        };

        IntentFilter batteryChargedFilter = new IntentFilter(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryLevelReceiver, batteryChargedFilter);
    }

    @Override
    public void onDestroy() {
        //Ao encerrar o servico, altera o flag para a Thread parar (isto eh importante para encerrar
        //a thread caso alguem tenha chamado o stopService(intent)
        ativo = false;
        Log.i("Battery", "Servico.onDestroy()");
    }
}
