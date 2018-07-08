package leandro.br.com.projetomobile.Entity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import leandro.br.com.projetomobile.DAO.MyORMLiteHelper;
import leandro.br.com.projetomobile.R;

/**
 * Created by Aluno on 27/06/2018.
 */

public class AdapterAutocomplete extends ArrayAdapter<Shopping> {

    ArrayList<Shopping> listaShopping;
    LayoutInflater inflate;
    Shopping s;
    Context ctx;
    ArrayList<Shopping> shoppingFavoritos;
    MyORMLiteHelper banco;


    public AdapterAutocomplete(Context ctx, ArrayList<Shopping> lista){
        super(ctx, R.layout.layout_autocomplete, lista);
        listaShopping = lista;
        shoppingFavoritos = new ArrayList<>();
        inflate = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return listaShopping.size();
    }

    @Override
    public Shopping getItem(int position) {
        return listaShopping.get(position);
    }


    public void remove(Shopping c){
        listaShopping.remove(c);
        notifyDataSetChanged();
    }

    public void add(Shopping c){
        listaShopping.add(c);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        banco = MyORMLiteHelper.getInstance(ctx);
        s = listaShopping.get(position);

        convertView = inflate.inflate(R.layout.layout_autocomplete, null);
        TextView tv = convertView.findViewById(R.id.editNomeShopping);
        tv.setText(s.getNome());
        tv.setTag(s.getNome());
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ctx);
                alerta.setTitle("Visualizando Dados");
                alerta.setIcon(android.R.drawable.ic_menu_view);
                alerta.setMessage(s.getNome());
                alerta.setNegativeButton("Fechar",null);

                alerta.setPositiveButton("Favoritar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingFavoritos.add(s);
                        try {
                            banco.getShoppingDAO().create(s);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    }
                });
                alerta.show();

                return false;
            }
        });

        return convertView;
    }

    public ArrayList<Shopping> getShoppingFavoritos() {
        return shoppingFavoritos;
    }

    public void setShoppingFavoritos(ArrayList<Shopping> shoppingFavoritos) {
        this.shoppingFavoritos = shoppingFavoritos;
    }
}
