package com.project.clothingstore.modal;

public class ProductCollections {
    private int imv;
    private String txtTitle, messenge;

    public ProductCollections(int imv, String txtTitle) {
        this.imv = imv;
        this.txtTitle = txtTitle;
    }

    public ProductCollections(int imv, String messenge, String txtTitle) {
        this.imv = imv;
        this.messenge = messenge;
        this.txtTitle = txtTitle;
    }

    public String getMessenge() {
        return messenge;
    }

    public void setMessenge(String messenge) {
        this.messenge = messenge;
    }

    public int getImv() {
        return imv;
    }

    public void setImv(int imv) {
        this.imv = imv;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }
}
