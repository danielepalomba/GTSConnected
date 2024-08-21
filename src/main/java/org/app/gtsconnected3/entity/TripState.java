package org.app.gtsconnected3.entity;

public enum TripState {
    PLANNED,
    STARTED,
    DELETED,
    FINISHED;

    public String getLabel() {
        return name().toUpperCase();
    }
    public String getLabelToShow() {
        String label = name().toLowerCase();
        return switch (label) {
            case "planned" -> "Pianificato";
            case "started" -> "Iniziato";
            case "deleted" -> "Cancellato";
            case "finished" -> "Terminato";
            default -> label;
        };
    }

}
