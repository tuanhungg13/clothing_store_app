package com.project.clothingstore.modal;

public class ProductCollections {
    private int imv;
    private String txtTitle, messenge;

    private String collectionId, collectionName, collectionImg, content, collectionType;

    public ProductCollections() {
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionImg() {
        return collectionImg;
    }

    public void setCollectionImg(String collectionImg) {
        this.collectionImg = collectionImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }


    // Xóa  các constructor không cần thiết

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
