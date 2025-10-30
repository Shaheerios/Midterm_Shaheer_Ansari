package com.example.midterm_shaheer_ansari;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Set<Integer>> historyNumbers = new MutableLiveData<>(new HashSet<>());
    public LiveData<Set<Integer>> getHistoryNumbers() {
        return historyNumbers;
    }

    private final MutableLiveData<List<String>> currentTable = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<String>> getCurrentTable() {
        return currentTable;
    }

    public void addHistoryNumber(int number) {
        Set<Integer> currentSet = historyNumbers.getValue();
        if (currentSet != null) {
            currentSet.add(number);
            historyNumbers.setValue(currentSet);
        }
    }

    public void setCurrentTable(List<String> list) {
        currentTable.setValue(list);
    }

    public void clearCurrentTable() {
        currentTable.setValue(new ArrayList<>());
    }
}