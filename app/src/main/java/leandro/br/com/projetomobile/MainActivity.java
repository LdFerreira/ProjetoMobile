package leandro.br.com.projetomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import leandro.br.com.projetomobile.DAO.MyORMLiteHelper;

import leandro.br.com.projetomobile.Entity.AdapterAutocomplete;
import leandro.br.com.projetomobile.Entity.Shopping;
import leandro.br.com.projetomobile.model.AdapterlistFavoritos;

public class MainActivity extends Activity {

    MyORMLiteHelper banco;
    ArrayList<Shopping> listaShopping;
    /*AdapterAutocomplete adapterShoppings;*/
    ArrayAdapter<Shopping> adapterShoppings;
    AutoCompleteTextView textView;
    ArrayList<Shopping> shoppingFavoritos;
    ListView listShopping;
    ListView listShoppingsFavoritos;
    AdapterlistFavoritos adapterShoppingFavoritos;
    Shopping shops = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banco = MyORMLiteHelper.getInstance(this);
        shoppingFavoritos = new ArrayList<>();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.0.107:8080/mobileapi/v1/api/shopping/list", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONArray json = new JSONArray(new String(response));
                    Type listType =
                            new TypeToken<ArrayList<Shopping>>() {
                            }.getType();
                    listaShopping = new Gson().fromJson(String.valueOf(json), listType);

                    adapterShoppings = new ArrayAdapter<Shopping>(MainActivity.this, android.R.layout.simple_list_item_1,
                            listaShopping);
                    listShopping = findViewById(R.id.listShopping);
                    listShopping.setOnItemLongClickListener(cliqueLongo());
                    listShopping.setOnItemClickListener(cliqueCurto());
                    listShopping.setAdapter(adapterShoppings);
                    Toast.makeText(MainActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

        });


        try {
            shoppingFavoritos = (ArrayList<Shopping>) banco.getShoppingDAO().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        listShoppingsFavoritos = findViewById(R.id.listFavoritos);

        adapterShoppingFavoritos = new AdapterlistFavoritos(this, shoppingFavoritos);
        listShoppingsFavoritos.setAdapter(adapterShoppingFavoritos);

        listShoppingsFavoritos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                shops = adapterShoppingFavoritos.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Visualizando Shopping");
                alerta.setMessage(shops.toString());
                alerta.setNeutralButton("Cancelar", null);
                alerta.setPositiveButton("Visitar Shopping", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, NavegacaoActivity.class);
                        startActivity(i);
                    }
                });
                alerta.setNeutralButton("Remover favorito", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            banco.getShoppingDAO().delete(shops);
                            adapterShoppingFavoritos.remove(shops);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alerta.show();
                return false;
            }
        });

    }


    public void teste(View v) {
        textView.setText(String.valueOf(v.getTag()));
        textView.dismissDropDown();
    }

//    public void popularShopping() throws SQLException {
//        Shopping shop1 = new Shopping("Shopping itaguaçu");
//        Shopping shop2 = new Shopping("Shopping Beiramar");
//        banco.getShoppingDAO().create(shop1);
//        banco.getShoppingDAO().create(shop2);
//    }




    private AdapterView.OnItemLongClickListener cliqueLongo() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Resgaatar a produto escolhido
                shops = adapterShoppings.getItem(position);

                //Alerta
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Adicionando a Favorito");
                alerta.setIcon(android.R.drawable.ic_menu_delete);
                alerta.setMessage("Deseja adicionar o shopping " + shops.getNome() + " a favorito ?");
                alerta.setNeutralButton("Não", null);
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingFavoritos.add(shops);
                        try {
                            banco.getShoppingDAO().create(shops);
                            adapterShoppingFavoritos.notifyDataSetChanged();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    }
                });
                alerta.show();

                return true;
            }
        };
    }
    private AdapterView.OnItemClickListener cliqueCurto() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                shops = adapterShoppings.getItem(i); // i = position

                //Criar um Dialog perguntando se quer editar
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Visualizado dados");
                alerta.setIcon(android.R.drawable.ic_menu_view);
                alerta.setMessage(shops.toString());
                alerta.setNeutralButton("Fechar", null);
                alerta.setPositiveButton("Visitar Shopping", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, NavegacaoActivity.class);
                        startActivity(i);
                    }
                });
                alerta.show();
            }
        };
    }

}




