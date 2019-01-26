package com.example.adeju.jessica;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.System.exit;

public class Checklist extends AppCompatActivity {

    private ListView lv;
    private int _Tipo;
    private int _Vistoria_Id;
    private Button toolBarBtn;
    private CustomAdapter adapter;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    File myFile;
    Document document;
    ArrayList<String> observacoes;
    ArrayList<ItemCheck> list;

    /**
     * Método chamado na criação da Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist); //Configura o arquivo de layout para esta Activity

        //Inicializa o menu superior e o botão voltar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Referenciar widget por seu id
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBarBtn = (Button)findViewById(R.id.rep_button);
        toolBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile ();
            }
        });

        //Configurar o Intent filter para receber os valores passados
        Intent intent = getIntent();
        _Vistoria_Id = intent.getIntExtra("vistoria_Id", 0);
        _Tipo = intent.getIntExtra("tipo", 0);


        updateList(this); //Chama o método para atualizar e instanciar a lista
    }


    /**
     * Instancia e atualiza um ListView para o checklist
     * @param view
     */
    public void updateList(Checklist view) {
        ItemRepo repo = new ItemRepo(this); //Instancia um objeto ItemRepo para interação com o banco de dados
        ArrayList<ItemCheck> itemList = repo.getItemList(_Tipo, _Vistoria_Id); //Obtem a lista com os itens do checklist

        if (itemList.size() != 0) { //Se houver itens na lista, configura o ListView
            lv = (ListView) findViewById(R.id.checklist); //Referenciar widget por seu id
            adapter = new CustomAdapter(itemList, Checklist.this, _Vistoria_Id, _Tipo); //Configura o adapter customizado
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //Configura o ListView como uma lista de multipla escolha
            lv.setAdapter(adapter); //Atribui o adapter ao ListView
        } else { //Se não houver itens, mostrar uma mensagem indicando que o checklist ainda não foi implementado
            Toast.makeText(this, "Checklist ainda não disponível!", Toast.LENGTH_SHORT).show();
        }
    }

    public void reportList() {
        list = adapter.getMylist();
            observacoes = new ArrayList<>(list.size());

            int k = 0;

        for (int i = 0; i < list.size(); i++) {
            ItemCheck item = list.get(i);
            if (item.isChecked() == false) {
                k = i;
                break;
            }
        }

            for (int i = 0; i < list.size(); i++) {
                observacoes.add("");
                ItemCheck item = list.get(i);
                dialog(item, i, k);
            }
    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException{
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("É necessário acesso ao sistema de arquivos")
                            .setMessage("Autorizar?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .create();
                    dialog.show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Permissão de escrita negada!", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void openFile () {
        File pdfFolder = new File(Environment.getExternalStorageDirectory(), "Vistorias");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }

        VistoriaRepo repo = new VistoriaRepo(this);
        Vistoria vistoria = repo.getVistoriaById(_Vistoria_Id);

        myFile = new File(pdfFolder.getAbsolutePath() + "/" + vistoria.embarcacao + String.valueOf(_Vistoria_Id) + ".pdf");

        if (myFile.exists()) {
            dialogFile();
        } else {
            reportList();
        }
    }

    public void createPdf() throws DocumentException, FileNotFoundException {
        VistoriaRepo repo = new VistoriaRepo(this);
        Vistoria vistoria = repo.getVistoriaById(_Vistoria_Id);
        String[] tipo_vistoria = getResources().getStringArray(R.array.tipos_vistoria);
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String nome = preferences.getString("nome","");
        String empresa = preferences.getString("empresa","");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //Configura o formato de data e hora
        String formattedDateString = formatter.format(vistoria.data); //Formata a data e hora do objeto

        if (myFile.exists()) {
            dialogFile();
        }

        OutputStream output = new FileOutputStream(myFile);

        document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA, 20.0f, Font.BOLD, BaseColor.BLACK);
        Paragraph titulo = new Paragraph("Checklist de Vistorias - Relatório", font);
        document.add(new Paragraph(titulo));

        document.add(new Paragraph("Vistoriador: " + nome));
        document.add(new Paragraph("Empresa: " + empresa));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Nome da embarcação vistoriada: " + vistoria.embarcacao));
        document.add(new Paragraph("Tipo de vistoria: " + tipo_vistoria[_Tipo]));
        document.add(new Paragraph("Data da vistoria: " + formattedDateString));

        document.add(new Paragraph("\n\n"));
        document.add(new LineSeparator());

        for (int i = 0; i < list.size(); i++) {
            ItemCheck item = list.get(i);
            if (!item.isChecked()) {
                document.add(new Paragraph("(" + String.valueOf(i) + ") " + item.getTitle() + " - NÃO CONFORME."));
                document.add(new Paragraph("Observações: " + observacoes.get(i)));
                document.add(new Paragraph("\n"));

            } else {
                document.add(new Paragraph("(" + String.valueOf(i) + ") " + item.getTitle() + " - Em conformidade."));
                document.add(new Paragraph("\n"));
            }
            document.add(new Paragraph(""));

        }


        document.close(); //Fecha o documento
        promptForNextAction();


    }


    private void viewPdf(){
        Uri pdfUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), myFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void emailNote() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,"Relatório de vistoria");
        email.putExtra(Intent.EXTRA_TEXT, "");
        Uri pdfUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), myFile);
        email.putExtra(Intent.EXTRA_STREAM, pdfUri);
        email.setType("message/rfc822");
        startActivity(email);


    }

    private void promptForNextAction() {
        final String[] options = { "Enviar por email", "Visualizar",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Relatório salvo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Enviar por email")){
                    emailNote();
                }else if (options[which].equals("Visualizar")){
                    viewPdf();
                }else if (options[which].equals("Cancelar")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void dialog (final ItemCheck item, final int i, final int k) {
        if (!item.isChecked()) {
            final EditText taskEditText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Insira as suas observações para o item")
                    .setMessage(item.getTitle())
                    .setView(taskEditText)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            observacoes.set(i, String.valueOf(taskEditText.getText()));
                            if (i == k) {
                                try {
                                    createPdfWrapper();
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                    Log.e("documento", "erro ao criar documento pdf");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Log.e("arquivo", "não encontrou arquivo");
                                }
                            }
                        }
                    })
                    .create();
            dialog.show();
        } else {
            observacoes.set(i, "");
        }
    }

    private void dialogFile () {
        Toast.makeText(this, "nuuyyuy", Toast.LENGTH_SHORT);
        AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Relatório já esxistente")
                    .setMessage("Deseja abrir o existente?")
                    .setPositiveButton("Abrir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            promptForNextAction();
                        }
                    })
                    .setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    })
                    .setNeutralButton("Gerar novo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reportList();
                        }
                    })
                    .create();
            dialog.show();
    }
}
