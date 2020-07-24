package com.a.marvel_personagens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.a.marvel_personagens.adapter.Adapter;
import com.a.marvel_personagens.model.modelo;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public static final String Reference_File = "ReferenceFile";


    private TextView textView;
    private RecyclerView recyclerView_ID;
    private SearchView search;





    private List<modelo> listaPersonagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final SharedPreferences sharedPreferences = getSharedPreferences(Reference_File, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();



        recyclerView_ID = (RecyclerView) findViewById(R.id.recyclerView_ID);
        textView        = (TextView) findViewById(R.id.textView);
        search          =  findViewById(R.id.search);

        consomeAPI();


        recyclerView_ID.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView_ID,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                final int posicao = position;
                                alertDialogDadosPersonagem(posicao);



                            }


                            @Override
                            public void onLongItemClick(View view, int position) {



                            }


                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );//Fim do evento de lick


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;

            }

            final String[] textoDigitado = {""};
            @Override
            public boolean onQueryTextChange(String newText) {

                textoDigitado[0] = newText.toUpperCase();

                    listaPersonagens.clear();
                try {
                    buscaLista(textoDigitado[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return true;
            }
        });

    }




    public void alertDialogDadosPersonagem(final int posicao){

        //Criando o AlertDialog
        final AlertDialog.Builder alertDialogEmissao = new AlertDialog.Builder(MainActivity.this);


        //---------------------------------
        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.alertdialog_personagem, null, false);
        //Aqui declarar os componentes da view

        final TextView name_ID                         = v.findViewById(R.id.name_ID);
        final TextView description_ID                  = v.findViewById(R.id.description_ID);
        final ImageView path_ID                        = v.findViewById(R.id.path_ID);
        final ListView  listView_ID                    = v.findViewById(R.id.listView_ID);

         ArrayAdapter<String> itensAdapter;
         ArrayList<String> itens;

        alertDialogEmissao.setView(v);
        //Faz não poder sair
        alertDialogEmissao.setCancelable(false);

        final modelo modelo = listaPersonagens.get(posicao);


        name_ID.setText(modelo.getName());
        description_ID.setText(modelo.getDescription());
        String caminhoImagemPoster = modelo.getPath();
        Glide.with(path_ID).load(caminhoImagemPoster).into(path_ID);
        String Json_itens= modelo.getDados_adicionais();


        try {
        JSONArray jsonArray = new JSONArray(Json_itens);
        JSONObject jsonObject;

        itens = new ArrayList<String>();

        itensAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2, itens);
        listView_ID.setAdapter(itensAdapter);

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            itens.add(jsonObject.getString("name")
            );
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        alertDialogEmissao.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }



        });//fim do positive


        //deixa visivel
        alertDialogEmissao.create();
        alertDialogEmissao.show();

    }



    public void buscaLista(final String busca) throws JSONException {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://gateway.marvel.com/v1/public/characters?ts=120220201955&apikey=2c3146623833e4c70f647fa910e4540c&hash=c8fe6f71fbb410984ffaa6ab297816db").newBuilder();

            String url = urlBuilder.build().toString();


            Request request = new Request.Builder()
                    .url(url)
                    .get()//Metodo get
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {


                                String data = response.body().string();

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_ID.setLayoutManager(layoutManager);
                                final Adapter adapter = new Adapter(listaPersonagens);
                                recyclerView_ID.setAdapter(adapter);


                                JSONObject json = new JSONObject(data);
                                JSONObject datajson = json.getJSONObject("data");
                                JSONArray resultjson = datajson.getJSONArray("results");
                                String stringResultjson = String.valueOf(resultjson);



                                JSONArray jsonArray = new JSONArray(stringResultjson );
                                JSONObject jsonObject;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);

                                    JSONObject comicjson =  jsonObject.getJSONObject("comics");
                                    JSONArray resultitems  = comicjson.getJSONArray("items");


                                    JSONObject dataTumb       = jsonObject.getJSONObject("thumbnail");
                                    String   caminhoImg       = dataTumb.getString("path")+".jpg";
                                    String   name             = jsonObject.getString("name");
                                    String   description      = jsonObject.getString("description");
                                    String   dados_adicionais = String.valueOf(resultitems);
                                    String testaBusca = name;

                                    if(testaBusca.contains(busca)){
                                        modelo modelo = new modelo (
                                                name,
                                                caminhoImg,
                                                description,
                                                dados_adicionais);
                                        listaPersonagens.add(modelo);
                                        adapter.notifyDataSetChanged();
                                    }

                                    }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                ;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//fim da função





    public void consomeAPI() {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://gateway.marvel.com/v1/public/characters?ts=120220201955&apikey=2c3146623833e4c70f647fa910e4540c&hash=c8fe6f71fbb410984ffaa6ab297816db").newBuilder();

            String url = urlBuilder.build().toString();


            Request request = new Request.Builder()
                    .url(url)
                    .get()//Metodo get
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                String data = response.body().string();

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_ID.setLayoutManager(layoutManager);
                                final Adapter adapter = new Adapter(listaPersonagens);
                                recyclerView_ID.setAdapter(adapter);


                                JSONObject json = new JSONObject(data);
                                JSONObject datajson = json.getJSONObject("data");
                                JSONArray resultjson = datajson.getJSONArray("results");
                                String stringResultjson = String.valueOf(resultjson);



                                JSONArray jsonArray = new JSONArray(stringResultjson );
                                JSONObject jsonObject;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);

                                    JSONObject comicjson =  jsonObject.getJSONObject("comics");
                                    JSONArray resultitems  = comicjson.getJSONArray("items");


                                    JSONObject dataTumb       = jsonObject.getJSONObject("thumbnail");
                                    String   caminhoImg       = dataTumb.getString("path")+".jpg";
                                    String   name             = jsonObject.getString("name");
                                    String   description      = jsonObject.getString("description");
                                    String   dados_adicionais = String.valueOf(resultitems);
                                    modelo modelo = new modelo (
                                            name,
                                            caminhoImg,
                                            description,
                                            dados_adicionais);



                                    listaPersonagens.add(modelo);
                                    adapter.notifyDataSetChanged();

                                }

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                ;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }// Fim da função



}