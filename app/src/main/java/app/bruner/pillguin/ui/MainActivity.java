package app.bruner.pillguin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        init();
    }

    private void init(){
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_continue){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}