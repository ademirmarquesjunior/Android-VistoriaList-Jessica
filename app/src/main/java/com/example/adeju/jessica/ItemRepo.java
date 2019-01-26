package com.example.adeju.jessica;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class ItemRepo {

    private Context mContext;
    private DBHelper dbHelper;

    public ItemRepo(Context context) {
        mContext = context;
        dbHelper = new DBHelper(context);
    } //Construtor da classe

    /**
     * Obtem os item de um checklist de acordo com a categoria
     * @param cat
     * @param vistoria
     * @return
     */
    public ArrayList<ItemCheck> getItemList(int cat, int vistoria) {

        SQLiteDatabase db = dbHelper.getReadableDatabase(); //Instancia o banco de dados em modo de leitura

        //A string com a consulta ao banco de dados é montada
        String selectQuery = "SELECT  " +
                Item.KEY_ID + "," +
                Item.KEY_TEXT + "," +
                Item.KEY_TIPO +
                " FROM " + Item.TABLE +
                " WHERE " + Item.KEY_TIPO + " = " + cat;

        ArrayList<ItemCheck> itemList = new ArrayList<ItemCheck>(); //Instancia uma lista de itens vazia

        Cursor cursor = db.rawQuery(selectQuery, null); //Salvar o retorno da consulta ao banco de dados em um cursor

        if (cursor.moveToFirst()) { //Percorrer todas as entradas do cursor e adicionar a lista
            do {
                final CheckedItemRepo checkedItemRepo = new CheckedItemRepo(mContext);
                final CheckedItem row = new CheckedItem();
                row.id_vistoria = vistoria;
                row.id_item = cursor.getPosition();
                row.id_tipo = cat;

                //Verificar se o item do checklist já foi marcado como checado
                ItemCheck item = new ItemCheck(cursor.getString(cursor.getColumnIndex(Item.KEY_TEXT)), checkedItemRepo.isChecked(row));
                itemList.add(item); //Adiciona o item a lista

            } while (cursor.moveToNext()); //Avança dentro do cursor
        }

        cursor.close(); //fecha o cursor
        db.close(); //fecha o banco de dados
        return itemList; //retorna a lista com os itens do checklist
    }
}
