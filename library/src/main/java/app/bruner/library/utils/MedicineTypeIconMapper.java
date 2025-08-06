package app.bruner.library.utils;

import android.content.Context;

import app.bruner.library.R;

public class MedicineTypeIconMapper {

    // base on id return a icon resource id
    public static int getIconByPosition(int position) {
        switch (position) {
            case 0: return R.drawable.ic_pill;
            case 1: return R.drawable.ic_type_liquid;
            case 2: return R.drawable.ic_type_injection;
            case 3: return R.drawable.ic_type_tablets;
            case 4: return R.drawable.ic_type_eyedropper;
            case 5: return R.drawable.ic_type_inhaler;
            case 6: return R.drawable.ic_type_topical;
            case 7: return R.drawable.ic_type_patch;
            case 8: return R.drawable.ic_type_spray;
            case 9: return R.drawable.ic_type_powder;
            case 10: return R.drawable.ic_type_lozenge;
            default: return R.drawable.ic_type_other;
        }
    }

    public static int getIconByType(Context context, String type) {
        String[] medicationTypesArray = context.getResources().getStringArray(R.array.list_medication_types);
        for (int i = 0; i < medicationTypesArray.length; i++) {
            if (medicationTypesArray[i].equalsIgnoreCase(type)) {
                return getIconByPosition(i);
            }
        }
        return getIconByPosition(-1);
    }
}
