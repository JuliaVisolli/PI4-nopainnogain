package com.example.littlewolf_pc.app.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.littlewolf_pc.app.R;
import com.example.littlewolf_pc.app.model.HistoriaDTO;
import com.example.littlewolf_pc.app.model.UsuarioDTO;
import com.example.littlewolf_pc.app.resource.ApiHistoria;
import com.example.littlewolf_pc.app.resource.ApiUsuario;
import com.example.littlewolf_pc.app.utils.UsuarioSingleton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {

    LinearLayout moldura;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        moldura = view.findViewById(R.id.linear);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://josiasveras.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Integer idUsuario = UsuarioSingleton.getInstance().getUsuario().getId();
        if(idUsuario != null){
            ApiUsuario apiUsuario = retrofit.create(ApiUsuario.class);
            Call<List<UsuarioDTO>> usuarioDTOCall = apiUsuario.perfilUsuario(String.valueOf(idUsuario));

            Callback<List<UsuarioDTO>> hiListCallback = new Callback<List<UsuarioDTO>>() {
                @Override
                public void onResponse(Call<List<UsuarioDTO>> call, Response<List<UsuarioDTO>> response) {
                    List<UsuarioDTO> perfilUsuario = response.body();

                    if(perfilUsuario != null && response.code() == 200){
                        for (UsuarioDTO usuarioDTO: perfilUsuario) {

                            TextView nomeUsuario = view.findViewById(R.id.nome);
                            nomeUsuario.setText(usuarioDTO.getNome());


                            if(usuarioDTO.getHistoria() != null){

                                if(usuarioDTO.getFoto() != null){
    //                            addItem(usuarioDTO.getFoto().toString(), usuarioDTO.getNome(), usuarioDTO.getEmail());
                                }
                                addItem("https://cdn4.iconfinder.com/data/icons/web-app-flat-circular-icons-set/64/Iconos_Redondos_Flat_Usuario_Icn-512.png", usuarioDTO.getNome(), usuarioDTO.getHistoria().getData(),  usuarioDTO.getHistoria().getTexto(),
                                        "https://st3.depositphotos.com/12985790/18246/i/450/depositphotos_182461084-stock-photo-anonymous.jpg", usuarioDTO.getHistoria().getTotalCurtidas().toString(), usuarioDTO.getHistoria().getTotalComentarios().toString());
                            }

                        }

                    }

                }
                @Override
                public void onFailure(Call<List<UsuarioDTO>> call, Throwable t) {
                    t.printStackTrace();

                }
            };
            usuarioDTOCall.enqueue(hiListCallback);
        }




        return view;
    }





    private void addItem(String url, String textoDoTitulo, Date textoDaHora, String textoDaMensagem, String imageURL, String quantidadeCurtida, String quantidadeComentario){
        CardView cardView;

        cardView = (CardView) LayoutInflater.from(this.getActivity())
                .inflate(R.layout.card,
                        moldura, false);


        TextView titulo = cardView.findViewById(R.id.titulo);
        titulo.setText(textoDoTitulo);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = format.format(textoDaHora);
        TextView hora = cardView.findViewById(R.id.hora);
        hora.setText(dateString);
        TextView mensagem = cardView.findViewById(R.id.mensagem);
        mensagem.setText(textoDaMensagem);
        TextView quantCurtida = cardView.findViewById(R.id.contcurtida);
        quantCurtida.setText(quantidadeCurtida);
        TextView quantComentario = cardView.findViewById(R.id.contcomentario);
        quantComentario.setText(quantidadeComentario + " comentarios");
//        btnComentario = cardView.findViewById(R.id.btn_comentario);
//        btnCurtida = cardView.findViewById(R.id.btn_curtida);


        moldura.addView(cardView);

        carregarImagemPerfil(imageURL, cardView);
        carregarImagemHistoria(url, cardView);
    }

    private void carregarImagemHistoria(String url, CardView cardView){
        ImageView imagem = cardView.findViewById(R.id.image);
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getActivity()));
        imageLoader.displayImage(url, imagem);

    }

    private void carregarImagemPerfil(String url, CardView cardView){
        ImageView imagemPerfil = cardView.findViewById(R.id.imagem);
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.init(ImageLoaderConfiguration.createDefault(this.getActivity()));
        imageLoader.displayImage(url, imagemPerfil);

    }




}
