package com.ccl.ccltools.platform;


public abstract class Platform implements PlatformInterface {

    public static Platform setPlatform(int platform){
        switch (platform){
            case PLATFORM_NETEASE:
                return Netease.getInstance();
            case PLATFORM_XIAMI:
                return Xiami.getInstance();
            case PLATFORM_QQ:
                return QQ.getInstance();
        }
        return null;
    }

    Platform(){

    }

}
