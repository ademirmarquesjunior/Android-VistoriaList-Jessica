package com.example.adeju.jessica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CheckedItemRepo {

    private DBHelper dbHelper;

    /**
     * Construtor da classe
     * @param context
     */
    public CheckedItemRepo(Context context) {
        dbHelper = new DBHelper(context);
    }


    /**
     * Salva no banco de dados um item do checklist (quando o usuário marca checado em uma linha)
     * @param checkedItem
     * @return
     */
    public int Check(CheckedItem checkedItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Instancia o banco de dados em modo de escrita
        ContentValues values = new ContentValues(); //Instancia um objeto ContentValues a ser inserido no banco de dados
        values.put(CheckedItem.KEY_ID_ITEM, checkedItem.id_item); //Insere no objeto values um atributo recebido
        values.put(CheckedItem.KEY_ID_TIPO, checkedItem.id_tipo); //Insere no objeto values um atributo recebido
        values.put(CheckedItem.KEY_ID_VISTORIA, checkedItem.id_vistoria); //Insere no objeto values um atributo recebido

        long checked_id = db.insert(CheckedItem.TABLE, null, values); //Salva no banco de dados usando o método INSERT
        db.close(); //Fecha o banco de dados
        return (int) checked_id; //Retorna o Id do objeto inserido
    }


    /**
     * Verifica se um item do checklist já foi inserido no banco de dados
     * @param checkedItem
     * @return
     */
    public boolean isChecked(CheckedItem checkedItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); //Instancia o banco de dados em modo de leitura

        //A string com a consulta ao banco de dados é montada
        String selectQuery = "SELECT  " +
                CheckedItem.KEY_ID_ITEM + "," +
                CheckedItem.KEY_ID_TIPO + "," +
                CheckedItem.KEY_ID_VISTORIA +
                " FROM " + CheckedItem.TABLE +
                " WHERE " + CheckedItem.KEY_ID_ITEM + "=" + checkedItem.id_item + " AND " +
                CheckedItem.KEY_ID_TIPO + "=" + checkedItem.id_tipo + " AND " +
                CheckedItem.KEY_ID_VISTORIA + "=" + checkedItem.id_vistoria;

        Cursor cursor = db.rawQuery(selectQuery, null); //O resultado da consulta é retornado em um objeto Cursor

        if (cursor.getCount() > 0) {
            return true; //Se foi encontrado o item retorna verdadeiro
        } else {
            return false; //Se nada encontrado retorna falso
        }
    }


    /**
     * Exclui do banco de dados um item do checklist (quando o usuário desmarca um item em uma linha)
     * @param checkedItem
     */
    public void unCheck(CheckedItem checkedItem) { //Exclui um item checkado do banco de dados - acontece quando o usuário desmarca um item
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Instancia o banco de dados em modo de escrita

        //Deleta o item checkado do banco de dados
        db.delete(CheckedItem.TABLE, CheckedItem.KEY_ID_ITEM + " =? AND " +
                CheckedItem.KEY_ID_TIPO + " =? AND " + CheckedItem.KEY_ID_VISTORIA + " =?", new String[]{String.valueOf(checkedItem.id_item), String.valueOf(checkedItem.id_tipo), String.valueOf(checkedItem.id_vistoria)});
        db.close(); //Fecha o banco de dados
    }


}
