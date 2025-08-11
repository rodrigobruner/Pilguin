package app.bruner.pillguin.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.ui.medication.detail.MedicationDetailInfoFragment;
import app.bruner.pillguin.ui.medication.detail.MedicationDetailSideEffectsFragment;
import app.bruner.pillguin.ui.medication.detail.MedicationDetailTakenLogFragment;
/**
 * Adapter to deal with the tabs in the Medication Detail screen.
 */
public class MedicationDetailTabAdapter extends FragmentStateAdapter {
    private Medication medication;

    public MedicationDetailTabAdapter(@NonNull Fragment fragment, Medication medicationParam) {
        super(fragment);
        this.medication = medicationParam;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) { // returns the fragment for each tab
        switch (position) {
            case 0: return MedicationDetailInfoFragment.newInstance(medication);
            case 1: return MedicationDetailTakenLogFragment.newInstance(medication);
            case 2: return MedicationDetailSideEffectsFragment.newInstance(medication);
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

