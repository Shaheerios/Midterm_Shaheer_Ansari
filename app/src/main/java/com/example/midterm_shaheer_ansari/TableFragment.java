package com.example.midterm_shaheer_ansari;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.midterm_shaheer_ansari.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {

    private SharedViewModel viewModel;
    private ArrayAdapter<String> tableAdapter;
    private List<String> currentTableList;
    private EditText editTextNumber;
    private ListView listViewTable;
    private static final int ACTION_CLEAR_ALL = 1001;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize UI components
        editTextNumber = view.findViewById(R.id.editTextNumber);
        Button buttonGenerate = view.findViewById(R.id.buttonGenerate);
        Button buttonHistory = view.findViewById(R.id.buttonHistory);
        listViewTable = view.findViewById(R.id.listViewTable);

        // Load saved state from ViewModel
        currentTableList = new ArrayList<>(viewModel.getCurrentTable().getValue());

        // Setup ListView Adapter
        tableAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                currentTableList
        );
        listViewTable.setAdapter(tableAdapter);

        // Event Listeners
        buttonGenerate.setOnClickListener(v -> generateTable(editTextNumber.getText().toString()));
        listViewTable.setOnItemClickListener((parent, v, position, id) -> showDeleteConfirmationDialog(position));

        buttonHistory.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void generateTable(String input) {
        try {
            int number = Integer.parseInt(input);
            if (number <= 0) {
                Toast.makeText(getContext(), "Please enter a positive number.", Toast.LENGTH_SHORT).show();
                return;
            }

            currentTableList.clear();
            for (int i = 1; i <= 10; i++) {
                String result = number + " × " + i + " = " + (number * i);
                currentTableList.add(result);
            }

            tableAdapter.notifyDataSetChanged();

            // Update ViewModel
            viewModel.setCurrentTable(new ArrayList<>(currentTableList));
            viewModel.addHistoryNumber(number);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid number entered.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        if (position < 0 || position >= currentTableList.size()) return;

        final String selectedItem = currentTableList.get(position);

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Item")
                .setMessage("Do you want to delete this row?\n" + selectedItem)
                .setPositiveButton("Delete", (dialog, which) -> {
                    currentTableList.remove(position);
                    tableAdapter.notifyDataSetChanged();
                    viewModel.setCurrentTable(new ArrayList<>(currentTableList));

                    Toast.makeText(getContext(), "Deleted: " + selectedItem, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Bonus Feature: Clear All
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.add(Menu.NONE, ACTION_CLEAR_ALL, Menu.NONE, "Clear All");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == ACTION_CLEAR_ALL) {
            showClearAllConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showClearAllConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear All")
                .setMessage("Are you sure you want to clear all items from the table?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    currentTableList.clear();
                    tableAdapter.notifyDataSetChanged();
                    viewModel.clearCurrentTable();

                    Toast.makeText(getContext(), "All rows cleared.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}