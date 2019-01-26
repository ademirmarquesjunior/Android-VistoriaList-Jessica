package com.example.adeju.jessica;

public class ItemCheck { //Classe

    private String title = "";
    private boolean checked = false;

    public ItemCheck(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
