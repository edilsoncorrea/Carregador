package br.com.edilsoncorrea.carregador.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.edilsoncorrea.carregador.servico.ServicoEnviarComando;

/**
 * Created by Edilson on 02/05/2015.
 */
public class CarregarBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Carregador", "Carregado no boot");

        context.startService(new Intent(context, ServicoEnviarComando.class));

//        Intent it = new Intent("MONITOR_CARREGAMENTO");
//        PendingIntent p = PendingIntent.getService(context, 0, it, 0);
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        c.add(Calendar.SECOND, 1);
//        AlarmManager alarme = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        long tempo = c.getTimeInMillis();
//        alarme.set(AlarmManager.RTC_WAKEUP, tempo, p);

    }
}
