package com.example.projekPam;

public class ProgressItem {
    private String label;
    private int iconResId;
    private int progress;

    public ProgressItem(String label, int iconResId, int progress) {
        this.label = label;
        this.iconResId = iconResId;
        this.progress = progress;
    }

    public String getLabel() { return label; }

    public void setLabel(String label) { this.label = label; }

    public int getIconResId() { return iconResId; }

    public void setIconResId(int iconResId) { this.iconResId = iconResId; }

    public int getProgress() { return progress; }

    public void setProgress(int progress) { this.progress = progress; }
}