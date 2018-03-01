package de.biomia.general.reportsystem;

import de.biomia.BiomiaPlayer;

public class PlayerReport {

    private final int reporterBiomiaID;
    private final int reporteterBiomiaID;
    private String grund;

    private String reporterName;
    private String reporteterName;

    public PlayerReport(int reporterBiomiaID, int reporteterBiomiaID) {
        this.reporterBiomiaID = reporterBiomiaID;
        this.reporteterBiomiaID = reporteterBiomiaID;
        this.reporterName = BiomiaPlayer.getName(reporterBiomiaID);
        this.reporteterName = BiomiaPlayer.getName(reporteterBiomiaID);
        ReportManager.unfinishedReports.add(this);
    }

    public PlayerReport(int reporterBiomiaID, int reporteterBiomiaID, String grund) {
        this.reporterBiomiaID = reporterBiomiaID;
        this.reporteterBiomiaID = reporteterBiomiaID;
        this.grund = grund;
        ReportManager.plReports.add(this);
    }

    public void finish(String grund) {
        this.grund = grund;
        ReportManager.unfinishedReports.remove(this);
        ReportManager.plReports.add(this);
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReporteterName() {
        return reporteterName;
    }

    public String getGrund() {
        return grund;
    }

    public int getReporteterBiomiaID() {
        return reporteterBiomiaID;
    }

    public int getReporterBiomiaID() {
        return reporterBiomiaID;
    }

    public int getLevel() {
        return ReportSQL.getLevel(getReporteterBiomiaID());
    }
}
