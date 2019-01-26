package com.example.adeju.jessica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class VistoriaRepo {

    private DBHelper dbHelper;

    public VistoriaRepo(Context context) {
        dbHelper = new DBHelper(context);
    }


    /**
     * Insere uma vistoria no banco de dados.
     * @param vistoria
     * @return O Id da vistoria
     */
    public int insert(Vistoria vistoria) {

        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Instancia o banco de dados em modo de escrita
        ContentValues values = new ContentValues(); //Instancia um objeto ContentValues a ser inserido no banco de dados
        values.put(Vistoria.KEY_EMBARCACAO, vistoria.embarcacao); //Insere no objeto values um atributo recebido
        values.put(Vistoria.KEY_TIPO, vistoria.tipo); //Insere no objeto values um atributo recebido
        values.put(Vistoria.KEY_DATA, vistoria.data); //Insere no objeto values um atributo recebido

        long vistoria_id = db.insert(Vistoria.TABLE, null, values); //Insere a vistoria no banco de dados
        db.close(); //Fecha o banco de dados
        return (int) vistoria_id; //Retorna o Id da vistoria inserida
    }

    /**
     * Deleta a vistoria do banco de dados
     * @param vistoria_id
     */
    public void delete(int vistoria_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Instancia o banco de dados em modo de escrita
        db.delete(Vistoria.TABLE, Vistoria.KEY_ID + " =?", new String[]{String.valueOf(vistoria_id)}); //Deleta a vistoria do banco de dados
        db.delete(CheckedItem.TABLE, CheckedItem.KEY_ID_VISTORIA + " =?", new String[]{String.valueOf(vistoria_id)}); //Deleta os itens de checklist checados para esta vistoria
        db.close(); //Fecha o banco dados
    }


    /**
     * Atualiza os dados da vistoria
     * @param vistoria
     */
    public void update(Vistoria vistoria) {

        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Instancia o banco de dados em modo de escrita
        ContentValues values = new ContentValues(); //Instancia um objeto ContentValues a ser inserido no banco de dados
        values.put(Vistoria.KEY_TIPO, vistoria.tipo); //Insere no objeto values um atributo recebido
        values.put(Vistoria.KEY_EMBARCACAO, vistoria.embarcacao); //Insere no objeto values um atributo recebido
        values.put(Vistoria.KEY_DATA, vistoria.data); //Insere no objeto values um atributo recebido

        db.update(Vistoria.TABLE, values, Vistoria.KEY_ID + "= ?", new String[]{String.valueOf(vistoria.id)}); //Atualiza a vistoria no banco dados com os valores recebidos
        db.close(); //Fecha o banco de dados
    }


    /**
     * Gera uma lista de vistorias
     * @return Lista HashMap com informações das vistorias obtidas no banco de dados
     */
    public ArrayList<HashMap<String, String>> getVistoriaList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase(); //Instancia o banco de dados em modo de leitura

        //A string com a consulta ao banco de dados é montada
        String selectQuery = "SELECT  " +
                Vistoria.KEY_ID + "," +
                Vistoria.KEY_TIPO + "," +
                Vistoria.KEY_EMBARCACAO + "," +
                Vistoria.KEY_DATA +
                " FROM " + Vistoria.TABLE + " ORDER BY " + Vistoria.KEY_DATA + " DESC";

        ArrayList<HashMap<String, String>> vistoriaList = new ArrayList<HashMap<String, String>>(); ////Instancia uma lista HashMap vazia para salvar as vistorias

        Cursor cursor = db.rawQuery(selectQuery, null); //Salvar o retorno da consulta ao banco de dados em um cursor


        if (cursor.moveToFirst()) { //Percorrer todas as entradas do cursor e adicionar a lista
            do {
                HashMap<String, String> vistoria = new HashMap<String, String>(); //Instancia um objeto HashMap de nome vistoria
                vistoria.put("id", cursor.getString(cursor.getColumnIndex(Vistoria.KEY_ID))); //Insere no objeto vistoria um atributo da entrada do cursor atual
                vistoria.put("embarcacao", cursor.getString(cursor.getColumnIndex(Vistoria.KEY_EMBARCACAO))); //Insere no objeto vistoria um atributo da entrada do cursor atual
                vistoria.put("tipo", cursor.getString(cursor.getColumnIndex(Vistoria.KEY_TIPO))); //Insere no objeto vistoria um atributo da entrada do cursor atual

                //Configura o formato de data e hora
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                String formattedDateString = formatter.format(Long.valueOf(cursor.getString(cursor.getColumnIndex(Vistoria.KEY_DATA))));
                vistoria.put("data", "Salvo: " + formattedDateString); //Insere no objeto vistoria a hora formatada

                vistoriaList.add(vistoria); //Insere o objeto vistoria na lista de vistoria

            } while (cursor.moveToNext()); //Avança dentro do cursor
        }

        cursor.close(); //Fecha o cursor
        db.close(); //Fecha o banco de dados
        return vistoriaList; //Retorna o objeto vistoriaList

    }

    /**
     * Retorna informações de uma vistoria pelo seu Id
     * @param Id
     * @return Um objeto vistoria
     */
    public Vistoria getVistoriaById(int Id) {
        if (Id != 0) { //Se o Id recebido não for zero, a consulta no banco de dados é feita
            SQLiteDatabase db = dbHelper.getReadableDatabase(); //Instancia o banco dados em modo leitura

            //A string com a consulta ao banco de dados é montada
            String selectQuery = "SELECT  " +
                    Vistoria.KEY_ID + " , " +
                    Vistoria.KEY_EMBARCACAO + " , " +
                    Vistoria.KEY_TIPO + " , " +
                    Vistoria.KEY_DATA +
                    " FROM " + Vistoria.TABLE
                    + " WHERE " +
                    Vistoria.KEY_ID + "=?";

            Vistoria vistoria = new Vistoria(); //Um objeto Vistoria é instanciado

            Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Id)}); //O resultado da consulta é retornado em um objeto Cursor

            if (cursor.moveToFirst()) {
                do {
                    vistoria.embarcacao = cursor.getString(cursor.getColumnIndex(Vistoria.KEY_EMBARCACAO)); //Insere no objeto vistoria um atributo da entrada do cursor atual
                    vistoria.tipo = cursor.getInt(cursor.getColumnIndex(Vistoria.KEY_TIPO)); //Insere no objeto vistoria um atributo da entrada do cursor atual
                    vistoria.data = cursor.getLong(cursor.getColumnIndex(Vistoria.KEY_DATA)); //Insere no objeto vistoria um atributo da entrada do cursor atual

                } while (cursor.moveToNext());
            }

            cursor.close(); //Fecha o cursor
            db.close(); //Fecha o banco de dados
            return vistoria; //Retorna um objeto Vistoria

        } else { //Se o Id recebido for zero, então valores padrão são retornados
            Vistoria vistoria = new Vistoria(); //Um objeto Vistoria é instanciado
            vistoria.embarcacao = ""; //O nome da vistoria recebe um texto vazio
            vistoria.data = System.currentTimeMillis(); //A vistoria recebe a hora atual
            return vistoria; //Retorna um objeto Vistoria
        }
    }
}
