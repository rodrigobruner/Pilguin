package app.bruner.pillguin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import app.bruner.pillguin.databinding.FragmentReportBinding;

public class ReportFragment extends Fragment {
    FragmentReportBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentReportBinding.inflate(getLayoutInflater());
        init();
        return binding.getRoot();
    }

    private void init() {

    }
}
