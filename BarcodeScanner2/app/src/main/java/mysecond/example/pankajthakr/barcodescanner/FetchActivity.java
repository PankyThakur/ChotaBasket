package mysecond.example.pankajthakr.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//public static final String PRODUCT_NAME = "mysecond.example.pankajthakr.barcodescanner.Product";
//public static final String PRICE = "mysecond.example.pankajthakr.barcodescanner.Price";

public class FetchActivity extends AppCompatActivity {
    EditText editTextBarcode;
    Button scanbar, btnSubmit, btnnex, btnpay;
    FirebaseDatabase database;
    private DatabaseReference reference;
    FirebaseAuth fauth;
    final Activity activity = this;
    private static final String TAG = "View Ads Fragment";
    private RecyclerView adsRecyclerView;
    ListView listView;
    List<Product> ProductList;
    private String databaseError;
    Cursor cursor;
    ArrayAdapter arrayAdapter;
    ArrayList<String> details = new ArrayList<>();
    private TextView viewData, totPrice;
    MqttAndroidClient client;

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        btnnex = findViewById(R.id.buttonnext);
        editTextBarcode = findViewById(R.id.barcodeNumber);
        scanbar = findViewById(R.id.buttonScan);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("All Barcodes");
        fauth = FirebaseAuth.getInstance();

        btnSubmit = findViewById(R.id.submit);
        listView = (ListView) findViewById(R.id.listView);
        totPrice = (TextView) findViewById(R.id.totPrice);
        btnpay = findViewById(R.id.buttonpay);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, details);
        listView.setAdapter(arrayAdapter);


        scanbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator in = new IntentIntegrator(activity);
                in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                in.setPrompt("scan");
                in.setCameraId(0);
                in.setBeepEnabled(false);
                //in.setBarcodeImageEnabled(false);
                in.initiateScan();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, editTextBarcode.getText().toString(), Toast.LENGTH_SHORT).show();
                getProductFromDb(editTextBarcode.getText().toString());
                String topic = "paho/chotabasket";
                String payload = "Green";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }


            }

            public void getProductFromDb(final String Barcode) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Product p = dataSnapshot.child("All Barcodes").child(Barcode).getValue(Product.class);
                        Toast.makeText(activity, p.toString(), Toast.LENGTH_SHORT).show();
                        //To add price
                        int price = Integer.parseInt(totPrice.getText().toString()) + Integer.parseInt(p.getPrice());
                        totPrice.setText("" + price);
                        //To Display the details
                        details.add(p.toString());
                        arrayAdapter.notifyDataSetChanged();

                              /*for (DataSnapshot prodSnapshot : dataSnapshot.getChildren()){

                                 Product product = prodSnapshot.child("All Barcodes").child(Barcode).getValue(Product.class);
                                 Toast.makeText(FetchActivity.this, "Barcode is : "+product, Toast.LENGTH_SHORT).show();

                             }*/

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "Error trying to get classified ads for " + Barcode +
                                " " + databaseError);
                        Toast.makeText(getActivity(),
                                "Error trying to get classified ads for " + Barcode, Toast.LENGTH_SHORT).show();

                    }

                });


            }

        });
        editTextBarcode.setText(null);
      /*btnnex.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(),Main2Activity.class));
              Toast.makeText(FetchActivity.this, "Main2going", Toast.LENGTH_SHORT).show();
              
          }
         
      });*/
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), "tcp://iot.eclipse.org:1883",
                clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("MQTT", "onSuccess");
                    Toast.makeText(FetchActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("MQTT", "onFailure");

                    Toast.makeText(FetchActivity.this, "Not Successfull", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                topic = "foo/bar1";

                //tvRecvMsg.setText(new String(message.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        btnnex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FetchActivity.this,"You Logged Out",Toast.LENGTH_LONG).show();
                startActivity(new Intent(FetchActivity.this,LoginActivity.class));
            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Log.d("MainActivity", "Scanned");
                editTextBarcode.setText(result.getContents());
                Toast.makeText(this, "Scanned " + result.getContents(), Toast.LENGTH_LONG).show();


            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Activity getActivity() {
        return activity;
    }

    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
    // Bundle savedInstanceState) {
    //View view = inflater.inflate(R.layout.ad_view_)*/
    public void browser1(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paytm.com"));
        //Log.d(TAG,"Web invoked");
        startActivity(browserIntent);
    }
}


  /* ValueEventListener valueEventListener = new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           ProductList.clear();
           if(dataSnapshot.exists()){
               for(DataSnapshot Snapshot: dataSnapshot.getChildren()){
                   Product product = Snapshot.getValue(Product.class);
                   ProductList.add(product);
               }
            arrayAdapter.notifyDataSetChanged();
               Log.d(TAG,"Retrieved");
           }
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {
           Log.d(TAG,"Cannot Retrieve");

       }
   };*/


