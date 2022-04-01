package com.ms.resource.util;


import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class AudioUtil {

    public static final String AUDIO_TYPE = "audio/mpeg";

    /**
     * Return's boolean as equal type "audio/mpeg"
     *
     * @param file mp3 file
     *
     */
    public static boolean isMp3(File file) {
        Tika tika = new Tika();

        try {
            String type = tika.detect(file);
            return type.equals(AUDIO_TYPE);
        } catch (IOException e) {
            return false;
        }
    }
}
