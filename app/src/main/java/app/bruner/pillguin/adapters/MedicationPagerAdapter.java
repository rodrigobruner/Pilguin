package app.bruner.pillguin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.ui.medication.MedicationDetailsFragment;
import app.bruner.pillguin.ui.medication.SideEffectsFragment;
import app.bruner.pillguin.ui.medication.TakenLogFragment;

public class MedicationPagerAdapter extends FragmentStateAdapter {
    private Medication medication;

    public MedicationPagerAdapter(@NonNull Fragment fragment, Medication medicationParam) {
        super(fragment);
        this.medication = medicationParam;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return MedicationDetailsFragment.newInstance(medication);
            case 1: return TakenLogFragment.newInstance(medication);
            case 2: return SideEffectsFragment.newInstance(medication);
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

