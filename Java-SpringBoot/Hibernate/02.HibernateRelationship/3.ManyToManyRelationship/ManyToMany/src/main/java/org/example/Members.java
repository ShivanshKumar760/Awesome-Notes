package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Members {
    @Id
    private int memid;
    private String memberName;
    @ManyToMany(mappedBy = "members")
    private List<Societies> societies;

    public int getMemid() {
        return memid;
    }

    public void setMemid(int memid) {
        this.memid = memid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<Societies> getSocieties() {
        return societies;
    }

    public void setSocieties(List<Societies> societies) {
        this.societies = societies;
    }


    @Override
    public String toString() {
        return "Members{" +
                "memid=" + memid +
                ", memberName='" + memberName + '\'' +
                ", societies=" + societies +
                '}';
    }
}
