package com.example.demok.net

/**
 * @author wq
 * @date 2019/4/25 下午1:46
 * @desc ${设备信息}
 */
data class DeviceInfo(

    //客户端系统信息
    var os: String,
    //客户端系统版本
    var osVersion: String,
    //设备ID
    var deviceId: String?,
    //客户端版本
    var version: String,
    //客户端编译号
    var versionCode : Long,
    //设备型号
    var deviceMode: String,
    //设备类型 0 ios,1 android
    var deviceType: String = "1"
)