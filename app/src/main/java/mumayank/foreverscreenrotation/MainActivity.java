package mumayank.foreverscreenrotation;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String STRING_ROTATIONS = "No. of total rotations: ";
    private static final String STRING_DURATION = "Duration between each rotation (in s): ";
    private static final int ROTATIONS_MAX = 48;
    private static final int ROTATIONS_DEFAULT = 24;
    private static final int DURATION_MAX = 20;
    private static final int DURATION_DEFAULT = 2;
    private static final int WAITING_TIME = 5;
    private static final int MILLISECONDS_IN_ONE_SECOND = 1000;

    private int currentRotation = Surface.ROTATION_0;
    private int rotationsRemaining = 0;
    private int duration = 0;
    private SeekBar seekBarRotations;
    private SeekBar seekBarDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disableAutoRotation();
        setupSeekBars();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationsRemaining = seekBarRotations.getProgress();
                duration = seekBarDuration.getProgress();

                if (rotationsRemaining == 0 || duration == 0) {
                    Toast.makeText(MainActivity.this, "Please select value > 0", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Beginning in " + WAITING_TIME +" seconds\n\nSwitch to your app.", Toast.LENGTH_SHORT).show();

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    nextRotation();
                                }
                            },
                            WAITING_TIME * MILLISECONDS_IN_ONE_SECOND
                    );

                }
            }
        });
    }

    private void disableAutoRotation() {
        Settings.System.putInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION,
                Surface.ROTATION_0
        );
    }

    private void setupSeekBars() {
        seekBarRotations = (SeekBar) findViewById(R.id.seekBar1);
        seekBarDuration = (SeekBar) findViewById(R.id.seekBar2);

        seekBarRotations.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) findViewById(R.id.textView1)).setText(STRING_ROTATIONS + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) findViewById(R.id.textView2)).setText(STRING_DURATION + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarRotations.setMax(ROTATIONS_MAX);
        seekBarRotations.setProgress(ROTATIONS_DEFAULT);

        seekBarDuration.setMax(DURATION_MAX);
        seekBarDuration.setProgress(DURATION_DEFAULT);
    }

    private void nextRotation() {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        rotationsRemaining--;

                        switch (currentRotation) {
                            case Surface.ROTATION_0:
                                currentRotation = Surface.ROTATION_90;
                                break;
                            case Surface.ROTATION_90:
                                currentRotation = Surface.ROTATION_180;
                                break;
                            case Surface.ROTATION_180:
                                currentRotation = Surface.ROTATION_270;
                                break;
                            case Surface.ROTATION_270:
                                currentRotation = Surface.ROTATION_0;
                                break;
                        }

                        Settings.System.putInt(
                                getContentResolver(),
                                Settings.System.USER_ROTATION,
                                currentRotation
                        );

                        if (rotationsRemaining > 0) {
                            nextRotation();
                        }
                    }
                },
                duration * MILLISECONDS_IN_ONE_SECOND
        );

    }
}
