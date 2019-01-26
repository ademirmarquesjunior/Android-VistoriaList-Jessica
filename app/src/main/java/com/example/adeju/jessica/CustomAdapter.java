package com.example.adeju.jessica;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    //Incialização das variáveis globais
    ArrayList<ItemCheck> mylist = new ArrayList<>();
    private Context mContext;
    private int _Vistoria_Id;
    private int _Tipo_Id;

    /**
     * Construtor da classe
     * @param itemArray Item em uma lista
     * @param mContext Contexto atual
     * @param vistoria Id da vistoria
     * @param tipo tipo devistoria
     */
    public CustomAdapter(ArrayList<ItemCheck> itemArray, Context mContext, int vistoria, int tipo) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
        _Vistoria_Id = vistoria;
        _Tipo_Id = tipo;
    }

    /**
     * Método para obter o tamanho da lista
     * @return Tamanho da lista
     */
    @Override
    public int getCount() {
        return mylist.size();
    }

    /**
     * Método que retorna um item da lista de acordo com a sua posição
     * @param position
     * @return A posição de um item em uma string
     */
    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    /**
     * Metodo que retorna o Id de um item na lista
     * @param position
     * @return A posição de um item
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onItemSelected(int position) {
    }

    /**
     * Obtem uma view em um ponto específico de acordo com o conjunto de dados
     * @param position
     * @param convertView
     * @param parent
     * @return Uma view em cache
     */
    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view = null; //Instancia uma view nula
        LayoutInflater inflator = ((Activity) mContext).getLayoutInflater(); //Instancia um objeto LayoutInflater baseado no contexto
        if (view == null) {
            view = new ViewHolder(); //Atribui um objeto ViewHolder a view
            convertView = inflator.inflate(R.layout.view_checklist_item, null); //Expande a view (linha em uma lista) e atribui a convertView
            view.nametext = (TextView) convertView.findViewById(R.id.item_text); //Referenciar widget por seu id
            view.tick = (CheckBox) convertView.findViewById(R.id.item_check); //Referenciar widget por seu id
            //final ViewHolder finalView = view; //Atribui view a um objeto final

            final CheckedItemRepo checkedItemRepo = new CheckedItemRepo(this.mContext); //Instância um objeto CheckedItemRepo para referência a itens já checados no banco de dados
            final CheckedItem row = new CheckedItem(); //Instancia um objeto CheckedItem com o nome de row
            row.id_vistoria = _Vistoria_Id; //Atribui valores de atributos ao objeto row
            row.id_item = position; //Atribui valores de atributos ao objeto row
            row.id_tipo = _Tipo_Id; //Atribui valores de atributos ao objeto row
            view.tick.setChecked(checkedItemRepo.isChecked(row)); //Verifica se o checkbox deve estar marcado ou desmarcado
            //finalView.tick.setChecked(true);

            view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //Configura a resposta aos toques no checkbox - marcado/desmarcado
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag(); //Obtem a posição do checkbox
                    mylist.get(getPosition).setChecked(buttonView.isChecked()); //Atribui a isChecked a interação do usuário
                    if (isChecked) {
                        checkedItemRepo.Check(row); //Salva o item checado no banco de dados
                    } else {
                        checkedItemRepo.unCheck(row); //Exclui o item checado do banco de dados
                    }
                }
            });
            convertView.setTag(view); //Atribui a convertTag a view atual
        } else {
            view = (ViewHolder) convertView.getTag(); //Atribui a view a tag de convertView
        }
        view.tick.setTag(position); //Obtem a posição para o checkbox
        view.nametext.setText("" + mylist.get(position).getTitle()); //Obtem o texto para a linha da lista
        view.tick.setChecked(mylist.get(position).isChecked()); //Marca ou desmarca o checkbox
        return convertView; //Retorna a view convertView
    }

    /**
     * Configura a classe a ser exibida na view
     */
    public class ViewHolder {
        public TextView nametext;
        public CheckBox tick;
    }

    public ArrayList<ItemCheck> getMylist() {
        return mylist;
    }
}
