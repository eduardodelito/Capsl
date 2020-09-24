package com.enaz.capsl.common.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.enaz.capsl.common.R
import com.enaz.capsl.common.config.CommonConfig
import com.enaz.capsl.common.config.EngineConfig
import com.enaz.capsl.common.handler.AgoraEventHandler
import com.enaz.capsl.common.util.getPreferences
import com.enaz.capsl.common.util.initializeLogFile
import dagger.Module
import dagger.Provides
import io.agora.rtc.Constants
import io.agora.rtc.RtcEngine
import javax.inject.Singleton

/**
 * Created by eduardo.delito on 9/24/20.
 */
@Module
class CommonModule(private val context: Context, private val application: Application) {
    private var mRtcEngine: RtcEngine? = null
    private val mHandler = AgoraEventHandler()
    @Provides
    @Singleton
    fun provideCommonConfig(): CommonConfig {
        return CommonConfig(application)
    }

    @Provides
    @Singleton
    fun provideAgoraEventHandler() : AgoraEventHandler = mHandler

    @Provides
    @Singleton
    fun provideRtcEngine(): RtcEngine? {
         try {
             mRtcEngine = RtcEngine.create(
                 context,
                 context.getString(R.string.private_app_id),
                 mHandler
             )
             // Sets the channel profile of the Agora RtcEngine.
             // The Agora RtcEngine differentiates channel profiles and applies different optimization algorithms accordingly. For example, it prioritizes smoothness and low latency for a video call, and prioritizes video quality for a video broadcast.
             mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
             mRtcEngine?.enableVideo()
             mRtcEngine?.setLogFile(initializeLogFile(context))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mRtcEngine
    }

    @Provides
    @Singleton
    fun provideEngineConfig() : EngineConfig {
        val pref: SharedPreferences = getPreferences(context, PREF_NAME)
        val engineConfig = EngineConfig()
        engineConfig.setVideoDimenIndex(
            pref.getInt(
                PREF_RESOLUTION_IDX, DEFAULT_PROFILE_IDX
            )
        )
//TODO: Comment for now
//        val showStats: Boolean = pref.getBoolean(PREF_ENABLE_STATS, false)
//        engineConfig.setIfShowVideoStats(showStats)
//        engineConfig.enableStats(showStats)

        engineConfig.setMirrorLocalIndex(pref.getInt(PREF_MIRROR_LOCAL, 0))
        engineConfig.setMirrorRemoteIndex(pref.getInt(PREF_MIRROR_REMOTE, 0))
        engineConfig.setMirrorEncodeIndex(pref.getInt(PREF_MIRROR_ENCODE, 0))
        return engineConfig
    }

    companion object {
        const val PREF_NAME = "io.agora.openlive"
        const val DEFAULT_PROFILE_IDX = 2
        const val PREF_RESOLUTION_IDX = "pref_profile_index"
//        const val PREF_ENABLE_STATS = "pref_enable_stats"
        const val PREF_MIRROR_LOCAL = "pref_mirror_local"
        const val PREF_MIRROR_REMOTE = "pref_mirror_remote"
        const val PREF_MIRROR_ENCODE = "pref_mirror_encode"
    }
}
