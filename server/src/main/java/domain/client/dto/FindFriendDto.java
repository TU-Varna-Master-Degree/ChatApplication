package domain.client.dto;

public class FindFriendDto {
    private Long id;
    private String username;

    public FindFriendDto() {
    }

    public FindFriendDto(Long id, String username) {
        this.id = id;
        this.username = username;
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
}
