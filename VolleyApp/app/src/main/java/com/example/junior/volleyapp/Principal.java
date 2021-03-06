package com.example.junior.volleyapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.junior.volleyapp.conexao.CustomJsonArraytRequest;
import com.example.junior.volleyapp.conexao.CustomJsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Principal extends ActionBarActivity {

    private EditText edtNl, edtSenha;
    private ProgressBar progressBar;
    private  String url = "http://webserverandroid.esy.es/inicio/post/";
    //private  String url = "http://192.168.43.213/nef/noivosemfesta/index.php/inicio/post/";
    private RequestQueue rq ;
    private Map<String, String> params;
    private Gson json;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        edtNl = (EditText) findViewById(R.id.edtNL);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        progressBar = new ProgressBar(Principal.this);




        rq = Volley.newRequestQueue(Principal.this);





   }

    public void callByJsonObjectRequest(View view){

       /* JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });*/

        progressDialog = ProgressDialog.show(Principal.this, "Title","CAREGANDO...",true);

        params = new HashMap<String, String>();

        params.put("nome",edtNl.getText().toString());
        params.put("senha",edtSenha.getText().toString());



        CustomJsonObjectRequest com = new CustomJsonObjectRequest(Request.Method.POST,
                url+"jor",
                params,
                new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    progressDialog.dismiss();

                    try {

                        String verifica = response.getString("erro");

                        if(verifica.equals("404")){
                            AlertDialog.Builder alert = new AlertDialog.Builder(Principal.this);
                            alert.setTitle("ERRO :'(");
                            alert.setMessage("Usuario não encontrado\n ou não cadastrado.\n Tente denovo");
                            alert.show();
                        }else{
                            trocaTela("salaUsuario",response);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("Erro Json", e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Erro no metodo de troca", e.getMessage());
                    }

                }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Toast.makeText(Principal.this, "Erro volley=> "+volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        com.setTag("tag");
        rq.add(com);

    }
    public void callByJsonArraytRequest(View view){

        params = new HashMap<String, String>();

        CustomJsonArraytRequest jar = new CustomJsonArraytRequest(Request.Method.GET,
                url+"jar/"+edtNl.getText().toString()+"/"+edtSenha.getText().toString(),
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Toast.makeText(Principal.this, "RECEBIDO VIA GET: "+response,Toast.LENGTH_LONG).show();

                        Log.i("Script","Sucess=>" + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(Principal.this, "Erro=> "+volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        jar.setTag("tag");
        rq.add(jar);


    }
    public void callByStringRequest(View view){

    }

    @Override
    public  void onStop(){

        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void trocaTela(String tela, JSONObject response) throws Exception{
        if(tela.equals("salaUsuario")){

            String login = response.getString("login");
            String id = response.getString("id");

            Intent intent = new Intent(this, SalaUsuario.class);

            intent.putExtra("login", login);
            intent.putExtra("id",id);

            this.setContentView(R.layout.activity_principal);

            startActivity(intent);
        }
    }
}
