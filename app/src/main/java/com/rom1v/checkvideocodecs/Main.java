package com.rom1v.checkvideocodecs;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

import java.util.Arrays;

public class Main {

    public static void main(String... args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });

        testEncoderByType("video/avc");
        listCodecInfos();
    }

    private static void listCodecInfos() {
        MediaCodecList list = new MediaCodecList(MediaCodecList.ALL_CODECS);
        for (MediaCodecInfo codecInfo : list.getCodecInfos()) {
            if (codecInfo.isEncoder()) {
                printCodecInfo(codecInfo);
            }
        }
    }

    private static void printCodecInfo(MediaCodecInfo codecInfo) {
        System.out.println("\n=== " + codecInfo.getName() + " ===");
        String[] supportedTypes = codecInfo.getSupportedTypes();
        System.out.println("Supported types: " + Arrays.toString(supportedTypes));
        if (Arrays.asList(supportedTypes).contains("video/avc")) {
            MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType("video/avc");
            MediaCodecInfo.VideoCapabilities videoCapabilities = capabilities.getVideoCapabilities();
            System.out.println("video/avc capabilities:");
            System.out.println("    default format: " + capabilities.getDefaultFormat());
            System.out.println("    profile levels: " + getProfileLevelsAsString(capabilities.profileLevels));
            System.out.println("    color formats: " + Arrays.toString(capabilities.colorFormats));
            System.out.println("    bitrate: " + videoCapabilities.getBitrateRange());
            System.out.println("    framerate: " + videoCapabilities.getSupportedFrameRates());
            System.out.println("    widths: " + videoCapabilities.getSupportedWidths());
            System.out.println("    heights: " + videoCapabilities.getSupportedHeights());
            System.out.println("    w,h alignment: " + videoCapabilities.getWidthAlignment() + "," + videoCapabilities.getHeightAlignment());
            testCodecByName(codecInfo.getName());
        }
    }

    private static String getProfileLevelsAsString(MediaCodecInfo.CodecProfileLevel[] profileLevels) {
        String[] result = new String[profileLevels.length];
        for (int i = 0; i < profileLevels.length; ++i) {
            MediaCodecInfo.CodecProfileLevel profileLevel = profileLevels[i];
            result[i] = "0x" + Integer.toHexString(profileLevel.profile) + "/0x" + Integer.toHexString(profileLevel.level);
        }
        return Arrays.toString(result);
    }

    private static void testCodecByName(String codecName) {
        try {
            MediaCodec codec = MediaCodec.createByCodecName(codecName);
            codec.release();
            System.out.println("TEST OK");
        } catch (Exception e) {
            System.out.println("TEST KO");
            e.printStackTrace();
        }
    }

    private static void testEncoderByType(String type) {
        try {
            MediaCodec codec = MediaCodec.createEncoderByType(type);
            System.out.println("video/avc -> " + codec.getCodecInfo().getName());
            codec.release();
        } catch (Exception e) {
            System.out.println("Cannot create encoder by type");
            e.printStackTrace();
        }
    }
}
