package com.example.scavengingscannables;

public class QrInfo {
    // Will generate a visual based on the qrID
    public String generateVisual(Integer qrID) {
        String visualization;
        int hashOfQRID = qrID.hashCode();

        // Checks first 6 bits of HashCode to determine visualization, currently using placeholders
        // as visualizations

        ArrayList<String> VisualOptionA = new ArrayList<String>;
        ArrayList<String> VisualOptionB = new ArrayList<String>;
        VisualOptionA.addAll("1stOptionA", "2ndOptionA","3rdOptionA", "4thOptionA","5thOptionA", "6thOptionA",);
        VisualOptionA.addAll("1stOptionB", "2ndOptionB","3rdOptionB", "4thOptionB","5thOptionB", "6thOptionB",);

        for (int i = 0; i < 6; i++) {
            if (hashOfQRID << i == 0) {
                visualization = VisualOptionA[i];
            } else {
                visualization = VisualOptionB[i];
            }
        }
        return visualization;
    }
}
