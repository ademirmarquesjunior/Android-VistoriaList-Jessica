package com.example.adeju.jessica;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Iniciando as variáveis globais da classe
    TextView vistoria_Id;
    ListView lv;
    ListAdapter adapter;
    String nome, empresa;



    /**
     * Método para gerenciar os toques na tela
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.fab)) {
            Intent intent = new Intent(this, VistoriaDetalhe.class);
            intent.putExtra("vistoria_Id", 0);
            startActivity(intent);
        } else {
            updateList(this);
        }
    }


    /**
     * Método que carrega e atualiza a lista (ListView)
     * @param view
     */
    public void updateList(MainActivity view) {
        VistoriaRepo repo = new VistoriaRepo(this); //Instancia um objeto VistoriaRepo para acessar o banco de dados
        ArrayList<HashMap<String, String>> vistoriaList = repo.getVistoriaList(); //Armazena as vistorias em uma lista do tipo Hashmap

        lv = (ListView) findViewById(R.id.vistorialist); //Referenciar widget por seu id
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Configura a resposta ao clique em um dos itens da lista
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vistoria_Id = (TextView) view.findViewById(R.id.vistoria_Id); //Referenciar widget por seu id
                String vistoriaId = vistoria_Id.getText().toString(); //Armazena o Id do item clicado em uma String
                Intent objIndent = new Intent(getApplicationContext(), VistoriaDetalhe.class); //Cria um Intent para a Activity VistoriaDetalhe
                objIndent.putExtra("vistoria_Id", Integer.parseInt(vistoriaId)); //Insere no Intent a Id da vistoria
                startActivity(objIndent); //Inicia a Activity a partir do Intent

            }
        });
        adapter = new SimpleAdapter(MainActivity.this, vistoriaList, R.layout.view_vistoria_entrada, new String[]{"id", "embarcacao", "data"}, new int[]{R.id.vistoria_Id, R.id.vistoria_embarcacao, R.id.vistoria_data}); //Instancia o Adapter a ser utilizado no ListView
        lv.setAdapter(adapter); //Configura o Adapter no ListView


        if (vistoriaList.size() == 0) { //Se a lista estiver vazia mostra uma mensagem
            Toast.makeText(this, "Lista vazia!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método chamado quando a Activity é criada
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Configura o arquivo de layout para esta Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Referenciar widget por seu id
        setSupportActionBar(toolbar); //Inicializa a barra superior do App

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //Referenciar widget por seu id
        fab.setOnClickListener(this); //Configura o Listener para o botão flutuante

        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        nome = preferences.getString("nome","");
        empresa = preferences.getString("empresa","");

        if (nome == "") {
            profileEditor();
        }

        updateList(this); //Atualiza a lista ao iniciar
    }


    /**
     * Inicializa o menu na action bar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu); //Inicializa o menu da barra superior (Action bar)
        return true;
    }

    /**
     * Método que gerencia os clicks no menu da barra superior (action bar)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); //Obtem o Id do item clickado

        if (id == R.id.action_about) { //Se o usuário clicou no item "Sobre" mostrar uma mensagem
            new AlertDialog.Builder(this).
                    setMessage("Aplicativo para checklist de vistorias baseado nas Normas da Autoridade Maritima (NORMAM 02, 2005)\n\n" +
                            "\nDesenvolvimento: Ademir Marques Junior e Jéssica Garcia." +
                    "\nVersão: 1.0").
                    setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Nada a fazer
                        }
                    }).
                    show();
            return true;
        }

        if (id == R.id.action_profile) {
            profileEditor();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método chamado quando a Activity volta a ser exibida
     */
    @Override
    public void onResume() {
        super.onResume();
        updateList(this); //Atualizar a lista quando voltar a Activity
    }

    /**
     * Método chamado quando a Activity reinicia
     */
    @Override
    public void onRestart() {
        super.onRestart();
        updateList(this); //Atualizar a lista quando voltar a Activity
    }

    public void profileEditor () {
        final SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        LayoutInflater inflater = getLayoutInflater();
        View alertlayout = inflater.inflate(R.layout.perfil, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edite o seu perfil");
        builder.setMessage("Insira o seus dados abaixo:");
        builder.setView(alertlayout);
        final EditText nomeEditText = (EditText) alertlayout.findViewById(R.id.nomeText);
        final EditText empresaEditText = (EditText) alertlayout.findViewById(R.id.empresaText);

        empresaEditText.setText(preferences.getString("empresa", ""));
        nomeEditText.setText(preferences.getString("nome", ""));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putString("nome", String.valueOf(nomeEditText.getText()));
                editor.putString("empresa", String.valueOf(empresaEditText.getText()));
                editor.apply();
            }
        });
        builder.setNegativeButton("Cancela", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
