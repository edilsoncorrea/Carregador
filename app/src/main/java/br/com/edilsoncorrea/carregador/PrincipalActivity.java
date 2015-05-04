package br.com.edilsoncorrea.carregador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.com.edilsoncorrea.carregador.tarefas.EnviaComando;


public class PrincipalActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Button btnLigar = (Button) findViewById(R.id.btLigar);
        btnLigar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EnviaComando ligar = new EnviaComando(PrincipalActivity.this, true);
                ligar.execute();
            }
        });

        Button btnDesligar = (Button) findViewById(R.id.btDesligar);
        btnDesligar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EnviaComando ligar =  new EnviaComando(PrincipalActivity.this, false);
                ligar.execute();
            }
        });

        Button btnBateria = (Button) findViewById(R.id.btBateria);
        btnBateria.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                batteryLevel();
            }
        });

    }

    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
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

                EnviaComando ligar1 =  new EnviaComando(PrincipalActivity.this, true);
                ligar1.execute();

                if (level < 30) {
                    EnviaComando ligar =  new EnviaComando(PrincipalActivity.this, true);
                    ligar.execute();
                }

                if (level > 35) {
                    EnviaComando ligar =  new EnviaComando(PrincipalActivity.this, false);
                    ligar.execute();
                }



            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
