package com.kexue.common.enums;


public enum PlayStatusEnum {

    END(0, "完结"),
    SERIAL(1, "连载"),
    
    NOTPLAY(2, "未播放"),
    
    WITHOUT(-1, " "),
    
    ;
    
    
    private final int key;
    private final String status;


    PlayStatusEnum(int key, String status) {
        this.key = key;
        this.status = status;
    }

    public int getKey() {
        return key;
    }

    public String getStatus() {
        return status;
    }

    public static String getStatus(int key) {
        for (PlayStatusEnum enumValue : PlayStatusEnum.values()) {
            if (enumValue.getKey() == key) {
                return enumValue.getStatus();
            }
        }
        return WITHOUT.getStatus();
    }

    public static int getKey(String status) {
        for (PlayStatusEnum enumValue : PlayStatusEnum.values()) {
            if (enumValue.getStatus().equals(status)) {
                return enumValue.getKey();
            }
        }
        return WITHOUT.getKey();
    }

}
