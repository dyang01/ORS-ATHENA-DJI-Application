package com.dji.sdk.sample.demo.ors;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.dji.sdk.sample.R;
import com.dji.sdk.sample.internal.controller.DJISampleApplication;
import com.dji.sdk.sample.internal.utils.ModuleVerificationUtil;
import com.dji.sdk.sample.internal.utils.ToastUtils;
import com.dji.sdk.sample.internal.view.BasePushDataView;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.flightcontroller.virtualstick.FlightControlData;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;

public class TestClass_backup extends BasePushDataView implements View.OnClickListener {

    private float pitch;
    private float roll;
    private float yaw;
    private float throttle;
    private double lat;
    private double setLat;
    private double setLng;
    private String latS;
    private String lngS;
    private String altS;
    private String fullStr;
    private double homeLat;
    private double homeLng;
    private double lng;
    private double alt;

    private double[] arr;

    private FlightController flightController;
    public TestClass_backup(Context context) {
        super(context);
        init(context);
    }

    private Button testButton;
    private Button setButton;
    private Button getButton;
    private Button goHomeButton;
    private Button startMotor;
    private Button stopMotor;

    private void initUI() {

        testButton = (Button) findViewById(R.id.test_btn);
        setButton = (Button) findViewById(R.id.set_home_btn);
        getButton = (Button) findViewById(R.id.get_home_btn);
        goHomeButton = (Button) findViewById(R.id.go_home_btn);
        startMotor = (Button) findViewById(R.id.start_motor);
        stopMotor = (Button) findViewById(R.id.stop_motor);

        testButton.setOnClickListener(this);
        setButton.setOnClickListener(this);
        getButton.setOnClickListener(this);
        goHomeButton.setOnClickListener(this);
        startMotor.setOnClickListener(this);
        stopMotor.setOnClickListener(this);

    }

    @Override

    public void onClick(View v) {

        //FlightController flightController = ModuleVerificationUtil.getFlightController();

        if (flightController == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.test_btn:
                pitch = 0.0f;
                roll = 0.0f;
                yaw = 0.0f;
                throttle = 5.0f;
                flightController.setStateCallback(new FlightControllerState.Callback() {
                    @Override
                    public void onUpdate(@NonNull FlightControllerState flightControllerState) {
                        lat = flightControllerState.getAircraftLocation().getLatitude();
                        lng = flightControllerState.getAircraftLocation().getLongitude();
                        alt = flightControllerState.getAircraftLocation().getAltitude();
                        homeLat = flightControllerState.getHomeLocation().getLatitude();
                        homeLng = flightControllerState.getHomeLocation().getLongitude();
                    }
                });
                latS = String.valueOf(lat);
                lngS = String.valueOf(lng);
                altS = String.valueOf(alt);
                fullStr = "Latitude: " + latS + " " + "Longitude: " + lngS + " Altitude: " + altS;
                ToastUtils.setResultToToast(fullStr);
                DJISampleApplication.getAircraftInstance()
                        .getFlightController()
                        .sendVirtualStickFlightControlData(new FlightControlData(pitch,
                                        roll,
                                        yaw,
                                        throttle),
                                new CommonCallbacks.CompletionCallback() {
                                    @Override
                                    public void onResult(DJIError djiError) {

                                    }
                                });
                break;
            case R.id.set_home_btn:
                setLat = 35;
                setLng = -90;
                flightController.setHomeLocationUsingAircraftCurrentLocation(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {

                    }
                });
                ToastUtils.setResultToToast("New Home Location Set");
                break;
            case R.id.get_home_btn:
                latS = String.valueOf(homeLat);
                lngS = String.valueOf(homeLng);
                fullStr = "Home Lat: " + latS + "Home Long: " + lngS;
                ToastUtils.setResultToToast(fullStr);
                break;
            case R.id.go_home_btn:
                flightController.startGoHome(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {

                    }
                });
                ToastUtils.setResultToToast("Going Home");
                break;
            case R.id.start_motor:
                flightController.turnOnMotors(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {

                    }
                });
                break;
            case R.id.stop_motor:
                flightController.turnOffMotors(new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {

                    }
                });
                break;
            default:
                break;
        }
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_test_class, this, true);
        flightController = ModuleVerificationUtil.getFlightController();
        BaseProduct product = DJISampleApplication.getProductInstance();
        flightController = ((Aircraft) product).getFlightController();
        flightController.setVirtualStickModeEnabled(true, new CommonCallbacks.CompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {

                    }
                });
        //initAllKeys();
        initUI();
    }



    /*@Override

    protected void onAttachedToWindow() {

        super.onAttachedToWindow();



        try {

            DJISampleApplication.getProductInstance().getBattery().setStateCallback(new BatteryState.Callback() {

                @Override

                public void onUpdate(BatteryState djiBatteryState) {

                    stringBuffer.delete(0, stringBuffer.length());



                    stringBuffer.append("BatteryEnergyRemainingPercent: ").

                            append(djiBatteryState.getChargeRemainingInPercent()).

                            append("%\n");

                    stringBuffer.append("CurrentVoltage: ").

                            append(djiBatteryState.getVoltage()).append("mV\n");

                    stringBuffer.append("CurrentCurrent: ").

                            append(djiBatteryState.getCurrent()).append("mA\n");



                    showStringBufferResult();

                }

            });

        } catch (Exception ignored) {



        }

    }



    @Override

    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();



        try {

            DJISampleApplication.getProductInstance().getBattery().setStateCallback(null);

        } catch (Exception ignored) {



        }

    }*/



    @Override

    public int getDescription() {

        return R.string.battery_listview_push_info;

    }

}