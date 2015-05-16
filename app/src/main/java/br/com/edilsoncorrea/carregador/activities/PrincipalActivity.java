package br.com.edilsoncorrea.carregador.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.edilsoncorrea.carregador.R;
import br.com.edilsoncorrea.carregador.tarefas.EnviaComando;
import br.com.edilsoncorrea.carregador.tarefas.EnviaComandoBT;

/**
 * Created by Edilson on 05/05/2015.
 */
public class PrincipalActivity extends Activity {
    private BluetoothAdapter btfAdapter;
    private BluetoothDevice device;
    private UUID uuidSerial;
    private BluetoothSocket socket;
    private OutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);

//        btfAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (btfAdapter == null) {
//            Toast.makeText(this, "Bluetooth nao disponivel neste aparelho", Toast.LENGTH_LONG).show();
//            finish();
//        }

        Button btLigar = (Button) findViewById(R.id.btLigar);
        btLigar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EnviaComandoBT ligar = new EnviaComandoBT(PrincipalActivity.this, true);
                ligar.execute();

//                try {
//                    out = socket.getOutputStream();
//
//                    String msg = "L";
//
//                    if (out != null) {
//                        out.write(msg.getBytes());
//                    }
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }
        });

        Button btDesligar = (Button) findViewById(R.id.btDesligar);
        btDesligar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EnviaComandoBT ligar =  new EnviaComandoBT(PrincipalActivity.this, false);
                ligar.execute();

//                try {
//                    out = socket.getOutputStream();
//
//                    String msg = "D";
//
//                    if (out != null) {
//                        out.write(msg.getBytes());
//                    }
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }
        });

        Button btBluetooth = (Button) findViewById(R.id.btBluetooth);
        btBluetooth.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
//               List<BluetoothDevice> lista;
//
//                lista = new ArrayList<BluetoothDevice>(btfAdapter.getBondedDevices());
//
//                for (BluetoothDevice  device : lista) {
//
//                    //device = btfAdapter.getRemoteDevice("00:12:03:23:70:80");
//                    uuidSerial = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//                    try {
//                        socket = device.createRfcommSocketToServiceRecord(uuidSerial);
//                        socket.connect();
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });

        Button btDesligaBT = (Button) findViewById(R.id.btDesligaBT);
        btDesligaBT.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
//                try {
//                    out.close();
//
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (btfAdapter.isEnabled()) {
//            Toast.makeText(this, "Bluetooth esta ligado", Toast.LENGTH_LONG).show();
//        }
//        else {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, 0);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "O bluetooth nao foi ativado.", Toast.LENGTH_LONG).show();
        }
    }
}
