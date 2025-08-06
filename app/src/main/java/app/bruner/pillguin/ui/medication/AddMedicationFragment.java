package app.bruner.pillguin.ui.medication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.FragmentAddMedicationBinding;
import app.bruner.pillguin.models.ScheduleProvider;
import app.bruner.pillguin.ui.medication.frequencies.CustomCard;
import app.bruner.pillguin.ui.medication.frequencies.EveryXHoursCard;

public class AddMedicationFragment extends Fragment {

    FragmentAddMedicationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddMedicationBinding.inflate(getLayoutInflater());
        init();
        return binding.getRoot();
    }

    private void init() {
        setUi();
        setUpFrequencyType();
    }


    private void setUi() {
        // medication type
        ArrayAdapter<CharSequence> medicationAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                app.bruner.library.R.array.list_medication_types,
                android.R.layout.simple_spinner_item
        );
        medicationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnMedicationType.setAdapter(medicationAdapter);

        // fraquency type
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.list_frequency_types,
                android.R.layout.simple_spinner_item
        );
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnFrequency.setAdapter(frequencyAdapter);

        // save button
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedication();
            }
        });
    }


    private void setUpFrequencyType(){
        binding.spnFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get type list from string
                String selectedFrequency = parent.getItemAtPosition(position).toString();
                String[] frequencyTypes = getResources().getStringArray(R.array.list_frequency_types);

                // decide which fragment to show
                Fragment fragment;
                if (selectedFrequency.equals(frequencyTypes[1])) { //Some days of the week
                    fragment = new CustomCard();
                } else { // Every X hours
                    fragment = new EveryXHoursCard();
                }

                // replace fragment on the frame layout
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_frequency, fragment)
                        .addToBackStack("details")
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void saveMedication() {

        // name
        String name = binding.edtMedicationName.getText().toString();
        if (name.isEmpty()) {
            binding.edtMedicationName.setError(getString(R.string.msg_error_name_required));
            return;
        }

        // dosage
        String dosage = binding.editTextText.getText().toString();
        if (dosage.isEmpty()) {
            binding.editTextText.setError(getString(R.string.msg_error_dosage_required));
            return;
        }

        // medication type
        String type = binding.spnMedicationType.getSelectedItem().toString();

        // get schedule from fragment
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.frame_layout_frequency);
        Schedule schedule = null;
        if (fragment instanceof ScheduleProvider) {
            schedule = ((ScheduleProvider) fragment).getSchedule();
        }

        if (schedule == null) { // if schedule is null, show error
            Toast.makeText(requireContext(), getString(R.string.msg_error_schedule), Toast.LENGTH_SHORT).show();
            return;
        }

        //create a ID
        long id = System.currentTimeMillis();

        // create medication object
        Medication medication = new Medication(id, name, dosage, type, schedule);

        // sava medication
        MedicationUtils.add(getContext(), medication);

        // show success message
        Toast.makeText(requireContext(), getString(R.string.msg_success_saved), Toast.LENGTH_SHORT).show();

        //redirect to MedicineFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new MedicationFragment())
                .commit();
    }

}
