package com.enaz.capsl.common.config

/**
 * Created by eduardo.delito on 9/24/20.
 */
class EngineConfig {
    // private static final int DEFAULT_UID = 0;
    // private int mUid = DEFAULT_UID;
    private var mChannelName: String? = null
    private var mShowVideoStats = false
    private var mDimenIndex: Int = 2
    private var mMirrorLocalIndex = 0
    private var mMirrorRemoteIndex = 0
    private var mMirrorEncodeIndex = 0


    fun getVideoDimenIndex(): Int {
        return mDimenIndex
    }

    fun setVideoDimenIndex(index: Int) {
        mDimenIndex = index
    }

    fun getChannelName(): String? {
        return mChannelName
    }

    fun setChannelName(mChannel: String?) {
        mChannelName = mChannel
    }

    fun ifShowVideoStats(): Boolean {
        return mShowVideoStats
    }

    fun setIfShowVideoStats(show: Boolean) {
        mShowVideoStats = show
    }

    fun getMirrorLocalIndex(): Int {
        return mMirrorLocalIndex
    }

    fun setMirrorLocalIndex(index: Int) {
        mMirrorLocalIndex = index
    }

    fun getMirrorRemoteIndex(): Int {
        return mMirrorRemoteIndex
    }

    fun setMirrorRemoteIndex(index: Int) {
        mMirrorRemoteIndex = index
    }

    fun getMirrorEncodeIndex(): Int {
        return mMirrorEncodeIndex
    }

    fun setMirrorEncodeIndex(index: Int) {
        mMirrorEncodeIndex = index
    }
}
