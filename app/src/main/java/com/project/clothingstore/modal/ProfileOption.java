package com.project.clothingstore.modal;

public class ProfileOption {
    private String title;
    private int iconResId;

    public ProfileOption(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}

