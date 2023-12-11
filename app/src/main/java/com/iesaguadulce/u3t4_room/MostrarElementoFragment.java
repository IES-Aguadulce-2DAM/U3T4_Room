package com.iesaguadulce.u3t4_room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iesaguadulce.u3t4_room.databinding.FragmentMostrarElementoBinding;

public class MostrarElementoFragment extends Fragment {
    private FragmentMostrarElementoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMostrarElementoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ElementosViewModel elementosViewModel = new ViewModelProvider(requireActivity()).get(ElementosViewModel.class);
        elementosViewModel.getSeleccionado().observe(getViewLifecycleOwner(), elemento -> {
            binding.tvNombre.setText(elemento.nombre);
            binding.tvDescripcion.setText(elemento.descripcion);
            binding.rbValoracion.setRating(elemento.valoracion);

            binding.rbValoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                elementosViewModel.actualizar(elemento, rating);
            });
        });

    }
}
