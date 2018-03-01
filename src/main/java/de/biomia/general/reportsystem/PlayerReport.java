package de.biomia.general.reportsystem;

import de.biomia.OfflineBiomiaPlayer;

public class PlayerReport {

    private final OfflineBiomiaPlayer reporterBiomiaPlayer;
    private final OfflineBiomiaPlayer reporteterBiomiaPlayer;
    private String grund;

    public PlayerReport(OfflineBiomiaPlayer reporterBiomiaPlayer, OfflineBiomiaPlayer reporteterBiomiaPlayer) {
        this.reporterBiomiaPlayer = reporterBiomiaPlayer;
        this.reporteterBiomiaPlayer = reporteterBiomiaPlayer;
        ReportManager.unfinishedReports.add(this);
    }

    public PlayerReport(OfflineBiomiaPlayer reporterBiomiaPlayer, OfflineBiomiaPlayer reporteterBiomiaPlayer, String grund) {
        this.reporterBiomiaPlayer = reporterBiomiaPlayer;
        this.reporteterBiomiaPlayer = reporteterBiomiaPlayer;
        this.grund = grund;
        ReportManager.plReports.add(this);
    }

    public void finish(String grund) {
        this.grund = grund;
        ReportManager.unfinishedReports.remove(this);
        ReportManager.plReports.add(this);
    }

    public OfflineBiomiaPlayer getReporterBiomiaPlayer() {
        return reporterBiomiaPlayer;
    }

    public OfflineBiomiaPlayer getReporteterBiomiaPlayer() {
        return reporteterBiomiaPlayer;
    }

    public String getGrund() {
        return grund;
    }

    public int getLevel() {
        return ReportSQL.getLevel(getReporteterBiomiaPlayer().getBiomiaPlayerID());
    }
}
