package com.example.midterm_shaheer_ansari;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HistoryFragment extends Fragment {

    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ListView listViewHistory = view.findViewById(R.id.listViewHistory);

        // Get unique numbers from the ViewModel
        Set<Integer> historySet = viewModel.getHistoryNumbers().getValue();
        List<Integer> historyList = new ArrayList<>(historySet);

        // Optional: Sort the list for clean display
        Collections.sort(historyList);

        ArrayAdapter<Integer> historyAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                historyList
        );
        listViewHistory.setAdapter(historyAdapter);
    }
}