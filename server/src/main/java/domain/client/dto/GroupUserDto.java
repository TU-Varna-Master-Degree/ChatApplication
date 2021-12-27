package domain.client.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GroupUserDto implements Serializable {

    private Long id;
    private String username;
    private LocalDateTime joinDate;

    public GroupUserDto(Long id, String username, LocalDateTime joinDate) {
        this.id = id;
        this.username = username;
        this.joinDate = joinDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
}
