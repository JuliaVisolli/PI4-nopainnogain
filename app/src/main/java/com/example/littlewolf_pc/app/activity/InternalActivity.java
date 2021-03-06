package com.example.littlewolf_pc.app.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.littlewolf_pc.app.fragment.AmigoFragment;
import com.example.littlewolf_pc.app.fragment.CardFragment;
import com.example.littlewolf_pc.app.fragment.LoginFragment;
import com.example.littlewolf_pc.app.fragment.NotificacaoFragment;
import com.example.littlewolf_pc.app.fragment.ProfileFragment;
import com.example.littlewolf_pc.app.R;
import com.example.littlewolf_pc.app.adapter.RecyclerViewAdapter;
import com.example.littlewolf_pc.app.fragment.SobreFragment;
import com.example.littlewolf_pc.app.fragment.TermoFragment;
import com.example.littlewolf_pc.app.model.UsuarioDTO;


import java.util.ArrayList;
import java.util.List;

public class InternalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProfileFragment perfilFragment;
    private CardFragment feedFragment;
    private AmigoFragment amigoFragment;
    private RecyclerView recyclerView;
    private List<UsuarioDTO> lstUsuarios;
    RecyclerViewAdapter adapter;
    FloatingActionButton fabGoToHistoria;
    TextView txtNomeUsuarioMenuLateral;
    View mHeaderView;
    int previousScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstUsuarios = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.navigation_view);
        // NavigationView Header
        mHeaderView =  navigationView.getHeaderView(0);

        // View
        txtNomeUsuarioMenuLateral =  mHeaderView.findViewById(R.id.nome_usuario_menu_lateral);
//        textViewEmail= (TextView) mHeaderView.findViewById(R.id.textViewEmailNav);

        // Set username & email
        final SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);

        String nome = prefs.getString("nome", null);
        txtNomeUsuarioMenuLateral.setText(nome);
//        textViewEmail.setText(SharedPrefManager.getInstance(this).getEmail());


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {
                            menuItem.setChecked(true);
                        }
                        drawerLayout.closeDrawers();
                        if (menuItem.getItemId() == R.id.main_frame) {
                            LoginFragment fragment = new LoginFragment();
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.main_frame, fragment).commit();
                            return true;
                        }

                        if (menuItem.getItemId() == R.id.action_friends) {
                            AmigoFragment amigoFragment = new AmigoFragment();
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.main_frame, amigoFragment).commit();
                            return true;
                        }
                        if (menuItem.getItemId() == R.id.action_terms) {
                            TermoFragment termoFragment = new TermoFragment();
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.main_frame, termoFragment).commit();
                            return true;
                        }
                        if (menuItem.getItemId() == R.id.action_about) {
                            SobreFragment SobreFragment = new SobreFragment();
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.main_frame, SobreFragment).commit();
                            return true;
                        }
                        if (menuItem.getItemId() == R.id.action_logout) {
                            final SharedPreferences preferences = getSharedPreferences("usuario", MODE_PRIVATE);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent i = new Intent(InternalActivity.this, MainActivity.class);
                            startActivity(i);
                            return true;
                        }
                        return false;
                    }
                });

        drawerLayout = findViewById(R.id.drawer);
        //abrir fechar menu
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer) {
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        feedFragment = new CardFragment();
        amigoFragment = new AmigoFragment();
        perfilFragment = new ProfileFragment();

        setFragment(feedFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    setFragment(feedFragment);
                    return true;
                case R.id.navigation_perfil:
                    setFragment(perfilFragment);
                    return true;
                case R.id.navigation_friend:
                    setFragment(amigoFragment);
                    return true;
            }
            return false;
            }
        });

        fabGoToHistoria = findViewById(R.id.fabEdit);
        View.OnClickListener listenerHistoria = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HistoriaActivity.class);
                startActivity(intent);
            }
        };
        fabGoToHistoria.setOnClickListener(listenerHistoria);

        final ScrollView scroll = findViewById(R.id.scroll_internal);

        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                // previousScrollY this variable is define in your Activity or Fragment
                if (scroll.getScrollY() > previousScrollY && fabGoToHistoria.getVisibility() == View.VISIBLE) {
                    fabGoToHistoria.hide();
                } else if (scroll.getScrollY() < previousScrollY && fabGoToHistoria.getVisibility() != View.VISIBLE) {
                    fabGoToHistoria.show();
                }
                previousScrollY = scroll.getScrollY();
            }
        });


    }

    private void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.getSupportFragmentManager().popBackStack();

    }
}
