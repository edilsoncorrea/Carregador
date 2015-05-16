package br.com.edilsoncorrea.carregador.tarefas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Edilson on 11/05/2015.
 */
public class EnviaComandoBT  extends AsyncTask<Void, Void, Integer> {
    private final Boolean ligar;
    private final Context context;

    public EnviaComandoBT(Context context, Boolean ligar)
    {
        this.context = context;
        this.ligar = ligar;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        BluetoothAdapter btfAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btfAdapter == null) {
            Toast.makeText(context, "Bluetooth nao disponivel neste aparelho", Toast.LENGTH_LONG).show();
        }
        else {
            BluetoothDevice device = btfAdapter.getRemoteDevice("00:12:03:23:70:80");
            UUID uuidSerial = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuidSerial);
                socket.connect();

                int contador = 0;

                SystemClock.sleep(500);

                while ((! socket.isConnected()) && (contador < 10)) {
                    contador++;
                    SystemClock.sleep(500);
                }

                OutputStream out = socket.getOutputStream();

                String msg;

                if (ligar) {
                    msg = "L";
                } else {
                    msg = "D";
                }

                if (out != null) {
                    out.write(msg.getBytes());
                }

                SystemClock.sleep(500);


                out.close();

                socket.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected void onPostExecute(Integer result) {
        Log.i("CATEGORIA", "Envio BT concluido.");
    }
}
