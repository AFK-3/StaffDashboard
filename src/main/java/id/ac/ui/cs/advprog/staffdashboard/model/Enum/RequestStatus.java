package id.ac.ui.cs.advprog.staffdashboard.model.Enum;


import lombok.Getter;

@Getter
public enum RequestStatus {
    WAITING_RESPONSE("WAITING_RESPONSE"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    private final String status;

    private RequestStatus(String status) {
        this.status = status;
    }

    public static boolean contains(String status) {
        for (RequestStatus requestStatus : RequestStatus.values()) {
            if (requestStatus.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
