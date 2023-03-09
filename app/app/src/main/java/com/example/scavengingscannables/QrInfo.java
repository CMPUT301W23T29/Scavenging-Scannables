package com.example.scavengingscannables;

public class QrInfo {
    // Will generate a visual based on the qrID
    public String generateVisual(Integer qrID) {
        String visualization = "";
        int hashOfQRID = qrID.hashCode();

        // Checks first 6 bits of HashCode to determine visualization, currently using placeholders
        // as visualizations

        String[] VisualOptionA = {"1stOptionB\n", "2ndOptionB\n","3rdOptionB\n", "4thOptionB\n","5thOptionB\n", "6thOptionB\n",};
        String[] VisualOptionB = {"1stOptionA\n","2ndOptionA\n","3rdOptionA\n", "4thOptionA\n","5thOptionA\n", "6thOptionA\n",};

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
