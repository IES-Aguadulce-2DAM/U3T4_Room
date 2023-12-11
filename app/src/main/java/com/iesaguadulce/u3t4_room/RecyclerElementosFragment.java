package com.iesaguadulce.u3t4_room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.iesaguadulce.u3t4_room.databinding.FragmentRecyclerElementosBinding;
import com.iesaguadulce.u3t4_room.databinding.ViewholderElementoBinding;

import java.util.List;


public class RecyclerElementosFragment extends Fragment {

    private FragmentRecyclerElementosBinding binding;
    private ElementosViewModel elementosViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRecyclerElementosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elementosViewModel = new ViewModelProvider(requireActivity()).get(ElementosViewModel.class);
        navController = Navigation.findNavController(view);

        binding.fbtnIrANuevoElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_recyclerElementosFragment_to_nuevoElementoFragment);
            }
        });

        ElementosAdapter elementosAdapter = new ElementosAdapter();
        binding.rvElementos.setAdapter(elementosAdapter);
        elementosViewModel.obtener()
                .observe(getViewLifecycleOwner(), elementos -> elementosAdapter.establecerLista(elementos));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // No permitimos mover elementos
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Elemento elemento = elementosAdapter.elementos.get(viewHolder.getAdapterPosition());
                elementosViewModel.eliminar(elemento);
            }
        }).attachToRecyclerView(binding.rvElementos);
    }

    class ElementosAdapter extends RecyclerView.Adapter<ElementoViewHolder> {

        List<Elemento> elementos;

        @NonNull
        @Override
        public ElementoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ElementoViewHolder(ViewholderElementoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ElementoViewHolder holder, int position) {
            Elemento elemento = elementos.get(position);

            holder.binding.tvNombre.setText(elemento.nombre);
            holder.binding.rbValoracion.setRating(elemento.valoracion);
            holder.binding.rbValoracion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if (fromUser) {
                    elementosViewModel.actualizar(elemento, rating);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                elementosViewModel.setSeleccionado(elemento);
                navController.navigate(R.id.action_recyclerElementosFragment_to_mostrarElementoFragment);
            });
        }

        @Override
        public int getItemCount() {
            return elementos != null ? elementos.size() : 0;
        }

        public void establecerLista(List<Elemento> elementos) {
            this.elementos = elementos;
            notifyDataSetChanged();
        }

        public Elemento obtenerElemento(int posicion) {
            return elementos.get(posicion);
        }
    }

    // Clase para inicializar el ViewBinding en los ViewHolder
    class ElementoViewHolder extends RecyclerView.ViewHolder {

        private final ViewholderElementoBinding binding;

        public ElementoViewHolder(ViewholderElementoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
