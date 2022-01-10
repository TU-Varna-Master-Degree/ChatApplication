package com.example.myapplication.domain.enums;

public enum ChatItemViewType {

    POV_TEXT,
    POV_FILE,
    POV_IMAGE,
    OTHER_TEXT,
    OTHER_FILE,
    OTHER_IMAGE,
    USER_JOIN;

    public boolean isOwnerPov() {
        return this == POV_TEXT || this == POV_FILE || this == POV_IMAGE;
    }
}
