package com.example.foodproject.Activity.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.foodproject.Activity.Domain.Foods;

import java.util.ArrayList;

public class ManagementListFavorite {
    private Context context;
    private TinyDB tinyDB;

    public ManagementListFavorite(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }
    public ArrayList<Foods> getListFavorite() {
        return tinyDB.getListObject("FavoriteList");
    }
    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListFavorite();

        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        tinyDB.putListObject("FavoriteList",listpop);
        Toast.makeText(context, "Added to your Favorite List", Toast.LENGTH_SHORT).show();
    }
    public  void removeItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.remove(position);
        tinyDB.putListObject("FavoriteList",listItem);
        changeNumberItemsListener.change();
    }



}
