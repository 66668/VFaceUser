package com.vfaceuser.commen;

/**
 * ͬ��״̬
 * @author Don Fang
 *
 */
public enum SyncStatus {
	UnSync(0),	Synced(1)  ;
	
	private int value = 0;

    private SyncStatus(int value) {  
        this.value = value;
    }

    public static SyncStatus valueOf(int value) {  
        switch (value) {
        case 0:
            return UnSync;
        case 1:
            return Synced;
        default:
            return null;
        }
    }

    public int value() {
        return this.value;
    }
}
