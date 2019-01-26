package com.example.adeju.jessica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;

    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Método chamado na criação da classe
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) { //Cria e insere dados nas tabelas do banco de dados

        String CREATE_TABLE_ITEM = "CREATE TABLE " + Item.TABLE + "("
                + Item.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Item.KEY_TEXT + " TEXT, "
                + Item.KEY_TIPO + " INT )";

        db.execSQL(CREATE_TABLE_ITEM);

        db.execSQL("INSERT INTO " + Item.TABLE +
                "(" + Item.KEY_ID + "," + Item.KEY_TEXT + "," + Item.KEY_TIPO + ") VALUES " +
                "(NULL, 'Verificar visualmente a embarcação para assegurar se não foram feitas alterações no casco e/ou nas superestruturas que possam alterar a borda livre anteriormente atribuída - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação e a manutenção da proteção de aberturas das condições de estanqueidade dos(s) porão(ões) de carga - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação e a manutenção da proteção de aberturas das condições de estanqueidade dos escotilhões de acesso abaixo do convés de borda livre. O fechamento de um escotilhão deverá ser necessariamente efetuado por intermédio de tampas com atracadores permanentemente fixados - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação e manutenção da proteção de aberturas das condições de estanqueidade da(s) porta(s) de visita - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação e a manutenção da proteção de aberturas das condições de estanqueidade das vigias e olhos de boi existentes nos costados abaixo do convés de borda livre - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação e a manutenção da proteção de aberturas das condições de estanqueidade dos alboios para iluminação e/ou ventilação natural - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar o estado de conservação das balaustradas e das bordas falsas das partes expostas dos conveses de borda livre e de superestrutura - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar se as saídas de água se encontram permanentemente desobstruídas - NORMAM 02/DPC Capítulo 6 Item 0631 C)3)', 5)," +
                "(NULL, 'Verificar se a altura da(s) balaustrada(s) e/ou borda(s) falsa(s) são > ou = a 1,00 m e também verificar se a abertura inferior da(s) balaustradas(s) apresentam altura < ou = 230 mm e se os demais vãos da(s) balaustrada(s) apresentam altura < ou = 380 mm - NORMAM 02/DPC Capítulo 4 Item 0427 b)c)', 5)," +
                "(NULL, 'Verificar a existência de uma passagem permanentemente desobstruída de proa a popa da embarcação, a qual não poderá ser efetivada por cima de tampas e escotilhas - NORMAM 02/DPC Capítulo 6 Item 0611 g)1)', 5)," +
                "(NULL, 'Verificar se as marcas de borda livre estão permanentemente fixadas em ambos os bordos da embarcação sendo que para embarcação de aço, as marcas devem ser soldados ou buriladas de forma permanente - NORMAM 02/DPC Capítulo 6 Item 0625 a)', 5)," +
                "(NULL, 'Verificar se as marcas estão pintadas em branco ou amarelo quando fixadas em fundo escuro ou em preto com fundo claro - NORMAM 02/DPC Capítulo 6 Item 0625 b)', 5)," +
                "(NULL, 'Verificar se todas as marcas de borda livre estão facilmente visíveis ou se necessário arranjo especial podem ser feito com este propósito. - NORMAM 02/DPC Capítulo 6 Item 0625 b)', 5)," +
                "(NULL, 'Verificar se a posição das marcas de borda livre se encontra de acordo com o estabelecido no Certificado Nacional de Borda Livre em vigor - NORMAM 02/DPC Capítulo 6 Item 0631 c)3)IV', 5)," +
                "(NULL, 'Verificar se as tampas das aberturas de escotilha, dos escotilhões e seus receptivos dispositivos de fechamento, quando existentes, tem resistência suficiente que permita satisfazer as condições de estanqueidade previstas para o tipo de embarcação considerada e deverão apresentar todos os elementos necessários para assegurar a estanqueidade - NORMAM 02/DPC Capítulo 6 Item 0611 e 0612', 5)," +
                "(NULL, 'Verificar se o(s) suspiro(s) localizado(s) acima do convés de borda livre é em forma de U invertido e possuam, no mínimo, uma altura de 450 mm, medidos da aresta inferior do suspiro até o convés de borda livre - NORMAM 02/DPC Capítulo 6 Item 0611 d)1)a)b)', 5)," +
                "(NULL, 'Verificar se as soleiras de portas, braçolas de escotilhas, escotilhões ou qualquer elemento que de acesso ao interior do casco possuam altura mínima de 150 mm (quando em ÁREA 1) e 260 mm (quando em ÁREA 2) - NORMAM 02/DPC Capítulo 6 Item 0611 e 0612', 5)," +
                "(NULL, 'Verificar a condição de estanqueidade das anteparas estanques e se as portas de visita, nelas instaladas, estão devidamente aparafusadas e em condições estanques - NORMAM 02/DPC Capítulo 6', 5)," +
                "(NULL, 'Verificar a bordo o ponto de alagamento, caso a mesma o possua, analisar se o mesmo está de acordo com o informado no Estudo de Estabilidade e representado no Plano de Arranjo Geral - NORMAM 02/DPC Capítulo 6 Item 0603 x)', 5)"
        );

        String CREATE_TABLE_VISTORIA = "CREATE TABLE IF NOT EXISTS " + Vistoria.TABLE + "("
                + Vistoria.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Vistoria.KEY_EMBARCACAO + " TEXT, "
                + Vistoria.KEY_TIPO + " INT, "
                + Vistoria.KEY_DATA + " DATETIME DEFAULT CURRENT_TIMESTAMP )";
        db.execSQL(CREATE_TABLE_VISTORIA);


        String CREATE_TABLE_CHECK = "CREATE TABLE IF NOT EXISTS " + CheckedItem.TABLE + "("
                + CheckedItem.KEY_ID_ITEM + " INTEGER NOT NULL, "
                + CheckedItem.KEY_ID_TIPO + " INTEGER NOT NULL, "
                + CheckedItem.KEY_ID_VISTORIA + " INTEGER NOT NULL,"
                + "PRIMARY KEY " +
                "( " + CheckedItem.KEY_ID_ITEM + "," + CheckedItem.KEY_ID_TIPO + "," + CheckedItem.KEY_ID_VISTORIA + "))";
        db.execSQL(CREATE_TABLE_CHECK);


    }


    /**
     * Método chamado quando há alteração na versão do banco de dados
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Exclui as tabelas
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Vistoria.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CheckedItem.TABLE);

        //Cria novas tabelas
        onCreate(db);

    }

}
