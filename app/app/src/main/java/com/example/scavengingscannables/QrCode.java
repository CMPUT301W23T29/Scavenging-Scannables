package com.example.scavengingscannables;

public class QrCode {

    private int qrId;
    private String nameText;
    private String comment;
    private String others;

    // create constructor to set the values for all the parameters of the each single view
    public QrCode(int QrId, String NameText, String Comment, String Others) {
        qrId = QrId;
        nameText = NameText;
        comment = Comment;
        others = Others;
    }

    public int getQrId() {
        return qrId;
    }
    public String getQrName() {
        return nameText;
    }

}