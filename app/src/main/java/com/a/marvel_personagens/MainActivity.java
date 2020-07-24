package com.a.marvel_personagens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.a.marvel_personagens.adapter.Adapter;
import com.a.marvel_personagens.model.modelo;

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



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_ID.setLayoutManager(layoutManager);
       // recyclerView_ID.setHasFixedSize(true);
        final Adapter adapter = new Adapter(listaPersonagens);
        recyclerView_ID.setAdapter(adapter);

        //listaPersonagens.clear();
        List();


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;

            }

            final String[] textoDigitado = {""};
            @Override
            public boolean onQueryTextChange(String newText) {

                textoDigitado[0] = newText.toUpperCase();
/*
                listaFilmes.clear();
                pesquisa(textoDigitado[0]);
                adapter.notifyDataSetChanged();

 */
                return true;
            }
        });

    }



    public void List() {


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

                                try {
                                    //recebe o json em string
                                    String data = response.body().string();

                                    JSONObject json = new JSONObject(data);
                                    //entra an chave data
                                    JSONObject datajson = json.getJSONObject("data");
                                    //entra na chave results
                                    JSONArray results = datajson.getJSONArray("results");
                                    //Uma vez já dentro da chave certa devolvo ao formato string para usar.
                                    String StringData = String.valueOf(results);

                                    JSONArray jsonArray = new JSONArray(StringData);
                                    JSONObject jsonObject;

                                    // campos nome,description, tumbnail  dentro de path


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        JSONObject dataTumb= json.getJSONObject("thumbnail");
                                        String caminhoImg=dataTumb.getString("path")+".jpg";

                                                modelo modelo = new modelo (jsonObject.getString("name"),
                                                        jsonObject.getString("description"),
                                                        caminhoImg);
                                        listaPersonagens.add(modelo);

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } catch (IOException e) {
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

    }// Fim da função lista



}