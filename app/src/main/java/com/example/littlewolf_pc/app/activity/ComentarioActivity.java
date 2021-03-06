package com.example.littlewolf_pc.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.littlewolf_pc.app.adapter.AdapterListerComentario;
import com.example.littlewolf_pc.app.model.ComentarioDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.littlewolf_pc.app.R;
import com.example.littlewolf_pc.app.model.HistoriaDTO;
import com.example.littlewolf_pc.app.model.UsuarioDTO;
import com.example.littlewolf_pc.app.resource.ApiComentario;
import com.example.littlewolf_pc.app.resource.ApiHistoria;
import com.example.littlewolf_pc.app.utils.UsuarioSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComentarioActivity extends AppCompatActivity {

    private Button btnComent;
    List<ComentarioDTO> comentarioDTOList;
    List<ComentarioDTO>lstComentarios = new ArrayList<ComentarioDTO>();
    private EditText etComentario;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://josiasveras.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        final Dialog loading = new Dialog(this, android.R.style.Theme_Black);
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawableResource(R.color.transparent);
        loading.setContentView(viewDialog);
        loading.setCancelable(false);

        loading.show();
        btnComent = findViewById(R.id.btnComent);
        etComentario = findViewById(R.id.txtComent);

        ApiComentario apiComentario =
                retrofit.create(ApiComentario.class);

        Intent intent = getIntent(); // gets the previously created intent
        Integer idHistoria = intent.getIntExtra("idHistoria",-1);
        Call<List<ComentarioDTO>> comentarioDTOCall = apiComentario.getAllComentariosByIdHistoria(String.valueOf(idHistoria));

        Callback<List<ComentarioDTO>> comentarioCallBack = new Callback<List<ComentarioDTO>>() {
            @Override
            public void onResponse(Call<List<ComentarioDTO>> call, Response<List<ComentarioDTO>> response) {
                comentarioDTOList = response.body();

                if (comentarioDTOList != null && response.code() == 200) {

                    loading.dismiss();

                    if(comentarioDTOList.size() > 0){

                        for (ComentarioDTO comentarioDTO : comentarioDTOList) {

                            lstComentarios.add(new ComentarioDTO(comentarioDTO.getId(), comentarioDTO.getTexto(), comentarioDTO.getUsuario(), comentarioDTO.getData()));
                        }
                        AdapterListerComentario AdapterListerComentario = new AdapterListerComentario(lstComentarios, ComentarioActivity.this);
                        ListView lista = findViewById(R.id.lista_comentarios);
                        lista.setAdapter(AdapterListerComentario);

                    }

                }

            }

            @Override
            public void onFailure(Call<List<ComentarioDTO>> call, Throwable t) {
                t.printStackTrace();
                loading.dismiss();

            }
        };
        comentarioDTOCall.enqueue(comentarioCallBack);
        enviarMensagem();



    }

    public void enviarMensagem(){

        ListView listView;
        listView = (ListView) LayoutInflater.from(this)
                .inflate(R.layout.model_comentario,
                        null, false);


        btnComent.setOnClickListener(new View.OnClickListener() {
                ListView lista = findViewById(R.id.lista_comentarios);
                AdapterListerComentario AdapterListerComentario = new AdapterListerComentario(lstComentarios, ComentarioActivity.this);
            @Override
            public void onClick(View v) {

                ComentarioDTO comentarioDTO = new ComentarioDTO();

                if(etComentario.getText().toString() != null) {
                    final SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);

                    Integer id = prefs.getInt("id", 0);

                    String nome = prefs.getString("nome", null);

                    Intent intent = getIntent(); // gets the previously created intent
                    Integer idHistoria = intent.getIntExtra("idHistoria",-1);

                    if(id > 0) {
                        comentarioDTO.setId(0);
                        comentarioDTO.setUsuario(new UsuarioDTO(id, nome));
                        comentarioDTO.setTexto(etComentario.getText().toString());
                        comentarioDTO.setHistoria(new HistoriaDTO(Integer.valueOf(idHistoria)));
                        comentarioDTO.setData(null);
                    }
                }

                 ApiComentario apiComentario =
                            retrofit.create(ApiComentario.class);
                    Call<ComentarioDTO> comentarioDTOCall = apiComentario.saveComentario(comentarioDTO);

                    Callback<ComentarioDTO> comentarioCallBack = new Callback<ComentarioDTO>() {
                        @Override
                        public void onResponse(Call<ComentarioDTO> call, Response<ComentarioDTO> response) {
                            ComentarioDTO comentario = response.body();

                            if (comentarioDTOList != null && response.code() == 200) {

                                if(comentario != null){

                                    lstComentarios.add(comentario);

                                    AdapterListerComentario AdapterListerComentario = new AdapterListerComentario(lstComentarios, ComentarioActivity.this);
                                    ListView lista = findViewById(R.id.lista_comentarios);
                                    lista.setAdapter(AdapterListerComentario);
                                    etComentario.setText("");

                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<ComentarioDTO> call, Throwable t) {
                            t.printStackTrace();

                        }
                    };
                    comentarioDTOCall.enqueue(comentarioCallBack);

            }
        });
    }
}

