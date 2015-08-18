package zibilal.com.newlocationapptest;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mLatEditView;
    private EditText mLonEditView;
    private Button mGetLocationButton;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatEditView = (EditText) findViewById(R.id.lat_edit_view);
        mLonEditView = (EditText) findViewById(R.id.lon_edit_view);
        mGetLocationButton = (Button) findViewById(R.id.get_location_btn);

        buildGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Getting the latest Location ==> ");
        Log.d(TAG, "Google api client = " + "--> client = " + mGoogleApiClient);

        if (mGoogleApiClient != null) Log.d(TAG, "Google api client is connected ? " + mGoogleApiClient.isConnected());


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            getLatestLocation();
        }
    }

    private void getLatestLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, "Latest location = " + mLastLocation);
        if (mLastLocation != null) {
            mLatEditView.setText(String.valueOf(mLastLocation.getLatitude()));
            mLonEditView.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    private synchronized void buildGoogleApiClient() {
        Log.d(TAG, "Building google api client....");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "On Connected!!");
                        getLatestLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "Connection is suspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "Connection is failed " + connectionResult.getErrorCode());
                    }
                })
                .addApi(LocationServices.API).build();
    }
}
