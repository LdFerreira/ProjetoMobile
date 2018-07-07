package leandro.br.com.projetomobile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import leandro.br.com.projetomobile.Entity.User;

public class LoginActivity  extends Activity {

    EditText editNome;
    EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editNome = findViewById(R.id.login);
        editSenha = findViewById(R.id.senha);
    }

    public void TelaDeCadastro(View view) {
        Intent it = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
        startActivity(it);

    }

    public void entrar(View view) {
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        try {
            User user = new User();
            user.setLogin(editNome.getText().toString());
            user.setSenha(editSenha.getText().toString());

            Gson gson = new Gson();
            entity = new StringEntity(gson.toJson(user));
            //url
            String url = "http://192.168.0.107:8080/mobileapi/v1/api/user";
            AsyncHttpClient client = new AsyncHttpClient();

            if (editNome.getText().toString().isEmpty() || editSenha.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Login ou senha não podem ser vazios", Toast.LENGTH_SHORT).show();
                return;
            }
            client.post(this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "Nenhum usuário encontrado", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
