package com.example.scavengingscannables;

public class QrInfo {
    // Will generate a visual based on the qrID
    public String generateVisual(Integer qrID) {
        String visualization;
        int hashOfQRID = qrID.hashCode();
        // Checks first 6 bits of HashCode to determine visualization, currently using placeholders
        // as visualizations, Also open to changing implementation
        if (hashOfQRID << 6 == 0) {
            visualization = "1stOptionA\n";
            }
        else {
                visualization = "1stOptionB\n";
        }
        if (hashOfQRID << 5 == 0) {
            visualization.concat("2ndOptionA\n");
        }
        else {
            visualization.concat("2ndOptionB\n");
        }
        if (hashOfQRID << 4 == 0) {
            visualization.concat("3rdOptionA\n");
        }
        else {
            visualization.concat("3rdOptionB\n");
        }
        if (hashOfQRID << 3 == 0) {
            visualization.concat("4ndOptionA\n");
        }
        else {
            visualization.concat("4ndOptionB\n");
        }
        if (hashOfQRID << 2 == 0) {
            visualization.concat("5thOptionA\n");
        }
        else {
            visualization.concat("5thOptionB\n");
        }
        if (hashOfQRID << 1 == 0) {
            visualization.concat("6thOptionA\n");
        }
        else {
            visualization.concat("6thOptionB\n");
        }
        return visualization;
    }
}
