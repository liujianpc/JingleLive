package com.tekinarslan.material.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        android.widget.Button button = (Button) findViewById(R.id.color);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View colorPickerView = getLayoutInflater().inflate(R.layout.color_picker_dialog,null);
                if (colorPickerView == null) return;

                final ColorPicker colorPicker = (ColorPicker) colorPickerView.findViewById(R.id.picker);
                SVBar svBar = (SVBar) colorPickerView.findViewById(R.id.svbar);
                OpacityBar opacityBar = (OpacityBar) colorPickerView.findViewById(R.id.opacitybar);
                final TextView hexCode = (TextView) colorPickerView.findViewById(R.id.hex_code);
                colorPicker.addSVBar(svBar);
                colorPicker.addOpacityBar(opacityBar);
                colorPicker.setOldCenterColor(R.color.material_deep_teal_500);
                colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int color) {
                        String hexColor = Integer.toHexString(color).toUpperCase();
                        hexCode.setText("#" + hexColor);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setView(colorPickerView);
                builder.setTitle("选择皮肤颜色");
                builder.setCancelable(true);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int color = colorPicker.getColor();
                        Toast.makeText(getApplicationContext(), String.valueOf(color), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();

                        /*mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        mDrawerLayout.closeDrawer(Gravity.START);*/
            }
        });

    }
}
