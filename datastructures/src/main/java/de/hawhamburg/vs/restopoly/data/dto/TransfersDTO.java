package de.hawhamburg.vs.restopoly.data.dto;

import de.hawhamburg.vs.restopoly.data.model.Transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransfersDTO {
    private Collection<String> transfers;

    public TransfersDTO(int gameId, List<Transfer> transfers) {
        this.transfers = new ArrayList<>();
        for(int i = 0; i < transfers.size(); i++) {
            this.transfers.add("/banks/" + gameId + "/transfers/" + i);
        }
    }

    public TransfersDTO(List<String> transfers) {
        this.transfers = transfers;
    }

    public TransfersDTO() {
    }

    public Collection<String> getTransfers() {
        return transfers;
    }

    public void setTransfers(Collection<String> transfers) {
        this.transfers = transfers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransfersDTO that = (TransfersDTO) o;

        return transfers != null ? transfers.equals(that.transfers) : that.transfers == null;

    }

    @Override
    public int hashCode() {
        return transfers != null ? transfers.hashCode() : 0;
    }
}