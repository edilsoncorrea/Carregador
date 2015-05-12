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
    }
}
