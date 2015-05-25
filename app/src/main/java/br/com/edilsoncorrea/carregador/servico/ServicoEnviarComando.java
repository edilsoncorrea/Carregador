package br.com.edilsoncorrea.carregador.servico;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import br.com.edilsoncorrea.carregador.tarefas.EnviaComando;
import br.com.edilsoncorrea.carregador.tarefas.EnviaComandoBT;

/**
 * Created by Edilson on 04/05/2015.
 */
public class ServicoEnviarComando extends Service implements Runnable{
    private final String USER_AGENT = "Mozilla/5.0";
    private boolean ativo;
    private static SharedPreferences configuracoes = null;
    private static Long inicioCarga = Long.valueOf(0);
    private static int nivelInicial = 0;
    private static int nivelCorrenteBateria = 0;
    private static boolean carregando = false;

    private static BroadcastReceiver batteryLevelReceiver = null;
    private static BroadcastReceiver batteryLowReceiver = null;
    private static BroadcastReceiver batteryChargedReceiver = null;
    private static BroadcastReceiver carregaddorConectadoReceiver = null;
    private static BroadcastReceiver carregaddorDesconectadoReceiver = null;

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

        configuracoes = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        Log.i("Battery", "Servico Iniciado");

        new Thread(this, "ExemploServico-" + startId).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        batteryLow();
        batteryCharged();
        carregadorConectado();
        carregadorDesconectado();
    }

    private void batteryLevel() {
        batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                //context.unregisterReceiver(this);
                Log.i("Bateria", ":: Receiver");

                nivelCorrenteBateria = getLevel(intent);

                if (nivelCorrenteBateria < 20) {
                    EnviaComandoBT ligar =  new EnviaComandoBT(context, true);
                    ligar.execute();
                }

                if (nivelCorrenteBateria == 100) {
                    EnviaComandoBT ligar =  new EnviaComandoBT(context, false);
                    ligar.execute();
                    Toast.makeText(context, "Carregado!", Toast.LENGTH_LONG).show();
                }


                Log.i("Battery", "Nivel restante da bateria: " + nivelCorrenteBateria + "%");
                //Toast.makeText(context, "Mudanca. Nivel restante da bateria: " + level + "%", Toast.LENGTH_LONG).show();
                //btnConsumer.setText("Battery Level Remaining: " + level + "%");
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    private int getLevel(Intent intent) {
        int rawlevel = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        int level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }
        return level;
    }

    private void batteryLow() {
        batteryLowReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                    EnviaComandoBT ligar =  new EnviaComandoBT(context, true);
                    ligar.execute();
            }
        };

        IntentFilter batteryLowFilter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryLowReceiver, batteryLowFilter);
    }

    private void batteryCharged() {
        batteryChargedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (carregando) {
                   AtualizaTempoCarga(context);

                    EnviaComandoBT ligar = new EnviaComandoBT(context, false);
                    ligar.execute();
                }

                carregando = false;
            }
        };

        IntentFilter batteryChargedFilter = new IntentFilter(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryChargedReceiver, batteryChargedFilter);
    }

    private void carregadorConectado() {
        carregaddorConectadoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryLevel();

                carregando = true;
                inicioCarga = System.currentTimeMillis();
                nivelInicial = nivelCorrenteBateria;

                Log.i("Battery", "Nivel restante da bateria: " + nivelInicial + "%");
                Toast.makeText(context, "Conectado. Nivel restante da bateria: " + nivelInicial + "%", Toast.LENGTH_LONG).show();

            }
        };

        IntentFilter filtroCarregadorConectado = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(carregaddorConectadoReceiver, filtroCarregadorConectado);
    }

    private void carregadorDesconectado() {
        carregaddorDesconectadoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (carregando) {
                    AtualizaTempoCarga(context);
                }

                carregando = false;

                desfazRegistroBatteryLevel();
            }
        };

        IntentFilter filtroCarregadorDesconectado = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(carregaddorDesconectadoReceiver, filtroCarregadorDesconectado);
    }

    private void AtualizaTempoCarga(Context context) {
        Long tempoTotalCarga = configuracoes.getLong("TempoTotalCarga", 0);
        int quantidadeCargas = configuracoes.getInt("QuantidadeCargas", 0);
        int nivelFinal = nivelCorrenteBateria;
        int tempoCargaCorrente = (int) ((System.currentTimeMillis() - inicioCarga) / 60000);
        int percentualCarregado = (nivelFinal - nivelInicial);

        if ((percentualCarregado > 0) && (tempoCargaCorrente >= 10 )) {
            //Ajusta a carga proporcionalmente
            double coeficienteCarga = ((double)percentualCarregado / 100.0);
            tempoCargaCorrente = (int) (tempoCargaCorrente / coeficienteCarga);

            tempoTotalCarga = tempoTotalCarga + (tempoCargaCorrente * percentualCarregado);
            quantidadeCargas = quantidadeCargas + percentualCarregado;

            SharedPreferences.Editor editor = configuracoes.edit();

            editor.clear();
            editor.putLong("TempoTotalCarga", tempoTotalCarga);
            editor.putInt("QuantidadeCargas", quantidadeCargas);
            editor.commit();

            Log.i("Battery", "Nivel restante da bateria: " + nivelFinal + "%");
            Toast.makeText(context, "Desconectado. Nivel restante da bateria: " + nivelFinal + "%", Toast.LENGTH_LONG).show();
            Toast.makeText(context, "Desconectado. Tempo projetado de carga: " + tempoCargaCorrente, Toast.LENGTH_LONG).show();

        } else {
            Log.i("Battery", "Nao carregou");
            Toast.makeText(context, "Nao carregou", Toast.LENGTH_LONG).show();
        }
    }

    private void desfazRegistroBatteryLevel() {
        if (batteryLevelReceiver != null) {
            unregisterReceiver(batteryLevelReceiver);
        };

    }

    private void RetiraRegistros() {
        desfazRegistroBatteryLevel();

        unregisterReceiver(batteryLowReceiver);
        unregisterReceiver(batteryChargedReceiver);
        unregisterReceiver(carregaddorConectadoReceiver);
        unregisterReceiver(carregaddorDesconectadoReceiver);
    }

    @Override
    public void onDestroy() {
        RetiraRegistros();
        //Ao encerrar o servico, altera o flag para a Thread parar (isto eh importante para encerrar
        //a thread caso alguem tenha chamado o stopService(intent)
        ativo = false;
        Log.i("Battery", "Servico.onDestroy()");
    }
}
