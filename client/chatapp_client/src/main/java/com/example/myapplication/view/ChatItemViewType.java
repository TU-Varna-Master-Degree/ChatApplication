package com.example.myapplication.view;

public enum ChatItemViewType
{
    VIEW_TYPE_POV_TEXT( 0x0 ),
    VIEW_TYPE_POV_FILE( 0x1 ),
    VIEW_TYPE_POV_IMAGE( 0x2 ),
    VIEW_TYPE_OTHER_TEXT( 0x3 ),
    VIEW_TYPE_OTHER_FILE( 0x4 ),
    VIEW_TYPE_OTHER_IMAGE( 0x5 );
    
    
    private final int value;
    ChatItemViewType(int value)
    {
        this.value = value;
    }
    
    public int getValue() { return value; }
    
    public static boolean isOwnerPov(ChatItemViewType flag)
    {
        return (flag.getValue() & 0x1) == 0;
    }
    
}
