package com.example.adeju.jessica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class VistoriaDetalhe extends AppCompatActivity implements View.OnClickListener {

    //Inicialização de variáveis globais
    EditText editTextEmbarcacao;
    EditText editTextData;
    Spinner spinner;
    private int _Vistoria_Id = 0;

    /**
     * Método chamado quando a classe é criada
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistoria_detalhe); //Configura o arquivo de layout para esta Activity

        //Inicialização do menu de ferramentas superior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Referenciar widget por seu id
        setSupportActionBar(toolbar);
        //Ativar o botão voltar no menu de ferramentas
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextEmbarcacao = (EditText) findViewById(R.id.editTextEmbarcacao);
        editTextData = (EditText) findViewById(R.id.editTextData);

        FloatingActionButton fab_del = (FloatingActionButton) findViewById(R.id.fab_del); //Referenciar widget por seu id
        fab_del.setOnClickListener(this); //Configurar listener para aguardar click do usuário

        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save); //Referenciar widget por seu id
        fab_save.setOnClickListener(this); //Configurar listener para aguardar click do usuário

        FloatingActionButton fab_open = (FloatingActionButton) findViewById(R.id.fab_open); //Referenciar widget por seu id
        fab_open.setOnClickListener(this); //Configurar listener para aguardar click do usuário


        spinner = (Spinner) findViewById(R.id.spinner); //Referenciar widget por seu id
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipos_vistoria, android.R.layout.simple_spinner_item); //Configurar adaptador e carregar lista de tipos de vistoria
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Especifica o tipo de layout do spinner
        spinner.setAdapter(adapter); //Configurar o adapter ao spinner

        //Configurar o Intent filter para receber os valores passados
        _Vistoria_Id = 0;
        Intent intent = getIntent();
        _Vistoria_Id = intent.getIntExtra("vistoria_Id", 0);

        //Inicializar um objeto VistoriaRepo com os valores passados
        VistoriaRepo repo = new VistoriaRepo(this);
        Vistoria vistoria = repo.getVistoriaById(_Vistoria_Id);

        if (_Vistoria_Id == 0){ //Se for uma nova vistoria, desabilita o botão abrir
            fab_open.setEnabled(false); //Desabilita o botão
            fab_open.setVisibility(View.INVISIBLE);  //Deixa o botão invisível
        }

        editTextEmbarcacao.setText(vistoria.embarcacao); //Altera o campo de texto com o valor correspondente no objeto

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //Configura o formato de data e hora
        String formattedDateString = formatter.format(vistoria.data); //Formata a data e hora do objeto
        editTextData.setText(String.valueOf(formattedDateString)); //Altera o campo de texto com o valor correspondente no objeto

        spinner.setSelection(vistoria.tipo); //Altera o item selecionado com o valor correspondente no objeto
    }

    /**
     * Método para gerenciar os toques na tela
     * @param view
     */
    public void onClick(View view) {
        if (view == findViewById(R.id.fab_save)) { //Se usuário clicar no botão salvar, as alterações são salvas e o checklist é aberto

            //Inicializar as variáveis e objetos a serem utilizados
            VistoriaRepo repo = new VistoriaRepo(this);
            Vistoria vistoria = new Vistoria();
            vistoria.embarcacao = editTextEmbarcacao.getText().toString();
            vistoria.data = System.currentTimeMillis();
            vistoria.tipo = spinner.getSelectedItemPosition();
            vistoria.id = _Vistoria_Id;

            if (vistoria.embarcacao.isEmpty()) { //Verifica se o campo com o nome da vistoria está vazio
                Toast.makeText(this, "Insira todos os campos", Toast.LENGTH_SHORT).show();
                return;
            } else {

                if (_Vistoria_Id == 0) {
                    _Vistoria_Id = repo.insert(vistoria);

                    Toast.makeText(this, "Nova vistoria salva", Toast.LENGTH_SHORT).show();
                } else {

                    repo.update(vistoria);
                    Toast.makeText(this, "Dados de vistoria atualizados", Toast.LENGTH_SHORT).show();
                }


                Intent intent = new Intent(this, Checklist.class); //Cria o Intent para chamar a Activity Checklist
                intent.putExtra("vistoria_Id", _Vistoria_Id); //Insere as variáveis e valores no Intent
                intent.putExtra("tipo", spinner.getSelectedItemPosition()); //Insere as variáveis e valores no Intent
                startActivity(intent); //Inicializa a Activity com o Intent criado
                finish(); //Encerra a Activity atual

            }
        } else if (view == findViewById(R.id.fab_open)) { //Se o usuário clicar no botão abrir, abre o checklist sem salvar as alterações

            Intent intent = new Intent(this, Checklist.class); //Cria o Intent para chamar a Activity Checklist
            intent.putExtra("vistoria_Id", _Vistoria_Id); //Insere as variáveis e valores no Intent
            intent.putExtra("tipo", spinner.getSelectedItemPosition()); //Insere as variáveis e valores no Intent
            startActivity(intent); //Inicializa a Activity com o Intent criado
            finish(); //Encerra a Activity atual

        } else if (view == findViewById(R.id.fab_del)) { //Se o usuário clica no botão deletar, uma confirmação é exibida

            //Inicializa o popup de confirmação
            new AlertDialog.Builder(this)
                    .setTitle("Atenção!")
                    .setMessage("Você deseja apagar esta vistoria?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) { //Se for confimado
                            VistoriaRepo repo = new VistoriaRepo(VistoriaDetalhe.this); //Instancia o objeto
                            repo.delete(_Vistoria_Id); //Apaga o objeto no banco de dados
                            Toast.makeText(VistoriaDetalhe.this, "Vistoria apagada", Toast.LENGTH_SHORT); //Exibe mensagem de sucesso

                            finish(); //Encerra a Activity atual
                        }})
                    .setNegativeButton(android.R.string.no, null).show(); //Se o usuário cancela, nada é feito
        }
    }


}
