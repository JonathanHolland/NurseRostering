package holland.jonathan.nurserostering.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import holland.jonathan.nurserostering.db.entity.DecoratedDate;

public class DecoratedDatesViewModel extends ViewModel {
    private MutableLiveData<List<DecoratedDate>> currentDecoratedDates;

    public MutableLiveData<List<DecoratedDate>> getCurrentDecoratedDates() {
        if (currentDecoratedDates == null) {
            currentDecoratedDates = new MutableLiveData<List<DecoratedDate>>();
        }
        return currentDecoratedDates;
    }
}
