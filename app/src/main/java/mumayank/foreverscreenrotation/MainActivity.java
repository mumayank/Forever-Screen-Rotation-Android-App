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

    private static final String textViewString1 = "No. of total rotations: ";
    private static final String textViewString2 = "Duration between each rotation (in s): ";
    private int currentRotation = Surface.ROTATION_0;
    private int rotationsRemaining = 0;
    private int duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // disable auto-rotation
        Settings.System.putInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION,
                Surface.ROTATION_0
        );

        final SeekBar seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        final SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar2);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) findViewById(R.id.textView1)).setText(textViewString1 + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) findViewById(R.id.textView2)).setText(textViewString2 + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar1.setMax(48);
        seekBar1.setProgress(24);

        seekBar2.setMax(20);
        seekBar2.setProgress(2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotationsRemaining = seekBar1.getProgress();
                duration = seekBar2.getProgress();

                if (rotationsRemaining == 0 || duration == 0) {
                    Toast.makeText(MainActivity.this, "Please select value > 0", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Begin", Toast.LENGTH_SHORT).show();
                    nextRotation();
                }
            }
        });
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
                duration * 1000
        );

    }
}
