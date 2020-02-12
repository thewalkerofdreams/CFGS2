package es.iesnervion.yeray.fragments_jugadores.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.iesnervion.yeray.fragments_jugadores.Fragments.AddPlayerFragment;
import es.iesnervion.yeray.fragments_jugadores.Fragments.PlayerListFragment;
import es.iesnervion.yeray.fragments_jugadores.POJO.ClsPlayerWithPositions;
import es.iesnervion.yeray.fragments_jugadores.R;
import es.iesnervion.yeray.fragments_jugadores.ViewModels.ClsMainActivityVM;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button listButton, addButton;
    Fragment listFragment, addFragment;
    ClsMainActivityVM viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(ClsMainActivityVM.class);
        listButton = findViewById(R.id.btnPlayerListMain);
        listButton.setOnClickListener(this);
        addButton = findViewById(R.id.btnNewPlayerMain);
        addButton.setOnClickListener(this);

        listFragment = new PlayerListFragment();
        addFragment = new AddPlayerFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.FrameLayoutMain, listFragment);//Por defecto dejamos la lista de jugadores

            /*El observer*/
            final Observer<ClsPlayerWithPositions> personObserver = new Observer<ClsPlayerWithPositions>() {
                @Override
                public void onChanged(ClsPlayerWithPositions playerWithPositions) {
                    //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //ft.replace(R.id.fragment, details).addToBackStack(null).commit();
                }
            };

            //Observo el LiveData con ese observer que acabo de crear
            viewModel.get_selectedPlayer().observe(this, personObserver);

            /*El observer para el fragment de añadir*/
            final Observer<Integer> isBtnAddPressedObserver = new Observer<Integer>() {
                @Override
                public void onChanged(Integer fragmentoACargar) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    switch (viewModel.get_fragmentoACargar().getValue()){
                        case 1:
                            ft.replace(R.id.FrameLayoutMain, listFragment).addToBackStack(null).commit();
                            break;
                        case 2:
                            ft.replace(R.id.FrameLayoutMain, addFragment).addToBackStack(null).commit();
                            break;
                    }
                }
            };

            //Observo el LiveData con ese observer que acabo de crear
            viewModel.get_fragmentoACargar().observe(this, isBtnAddPressedObserver);

        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlayerListMain:
                viewModel.set_fragmentoACargar(1);
                break;
            case R.id.btnNewPlayerMain:
                viewModel.set_fragmentoACargar(2);
                break;
        }
    }

}
