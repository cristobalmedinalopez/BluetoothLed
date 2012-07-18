package cml.bluetoothled;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class BluetoothLedActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;

    private String mac = "00:11:22:33:44:55"; //Mac
    private String uuid = "00001101-0000-1000-8000-00805F9B34FB";
    private ImageView led1;
    private ImageView led2;
    private ImageView led3;
    private boolean ld1;
    private boolean ld2;
    private boolean ld3;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        led1=(ImageView)findViewById(R.id.led1);
        led2=(ImageView)findViewById(R.id.led2);
        led3=(ImageView)findViewById(R.id.led3);
        ld1=false;
        ld2=false;
        ld3=false;
  
            try{
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // El dispositivo no soporta bluetooth
                        finish();
                    return;
                }
                else{
                        // El dispositivo soporta bluetooth
                        if (!mBluetoothAdapter.isEnabled()) {
                                //El dispositivo no tiene habilitado el bluetooth
                                //habilitamos el bluetooth
                            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
                            startActivity(discoverableIntent);
                        }
                        else{
                                //Dispositivo bluetooth habilitado
                        }
                        //Consultar dispositivos vinculados
                        Set<BluetoothDevice> pairedDevices =mBluetoothAdapter.getBondedDevices();
                }
            }catch(Exception e){}
            
    }
    public void Desconectar(View v){
    	try {
			mmSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void Conectar(View v){
    	//Conexion
        mmDevice = mBluetoothAdapter.getRemoteDevice(mac);
        try {
			mmSocket =mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
			mmSocket.connect();
			new Loggin().execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    public void Enviar (char led){
    	OutputStream tmpOut = null;
    	try {
			tmpOut = mmSocket.getOutputStream();
			tmpOut.write(led);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void pulsarLed1(View v){
    	if (ld1==false){
    		Enviar('7');	
    		led1.setImageResource(R.drawable.ledon);
    		ld1=true;
    	}else{
    		Enviar('4');	
    		led1.setImageResource(R.drawable.ledoff);
    		ld1=false;
    	}
    }
    
    public void pulsarLed2(View v){
    	if (ld2==false){
    		Enviar('6');	
    		led2.setImageResource(R.drawable.ledon);
    		ld2=true;
    	}else{
    		Enviar('3');	
    		led2.setImageResource(R.drawable.ledoff);
    		ld2=false;
    	}
    }
    
    public void pulsarLed3(View v){
    	if (ld3==false){
    		Enviar('5');	
    		led3.setImageResource(R.drawable.ledon);
    		ld3=true;
    	}else{
    		Enviar('2');	
    		led3.setImageResource(R.drawable.ledoff);
    		ld3=false;
    	}
    }
    public void recibido(){
    	Toast.makeText(this, "Recibido", Toast.LENGTH_SHORT).show();
    }
    
    private class Loggin extends AsyncTask<Void, Void, Integer> {
    	byte[] buffer = new byte[1024];
        int bytes;
        InputStream tmpIn =null;
         
		protected Integer doInBackground(Void... params) {			
			 while (true) {
	                try {
	                	tmpIn= mmSocket.getInputStream();
	                    // Read from the InputStream
	                	bytes=tmpIn.read(buffer);
	                    if (bytes==0){
	                    	recibido();
	                    	 
	                    }
	                    Log.e("AQUI: ", bytes+"");
	                 } catch (IOException e) {
	                    Log.e("AQUI: ", "disconnected", e);
	                    break;
	                }
	            }
			 return 1;
		}
		protected void onPostExecute(Integer resultado) {
			Log.e("AQUI: ", "Fin de conexion");
		}
		
	}	
 }