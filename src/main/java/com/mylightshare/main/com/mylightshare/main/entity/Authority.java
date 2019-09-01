package com.mylightshare.main.com.mylightshare.main.entity;

import javax.persistence.*;
import java.io.Serializable;

class CompositeKey implements Serializable {
    private String username;
    private String authority;
}

@Entity
@Table(name="authorities")
@IdClass(CompositeKey.class)
public class Authority {

    @Id
    @JoinColumn(nullable = false, name = "username")
    private String username;

    @Id
    @Column(name="authority")
    private String authority;

    public Authority() {}

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
