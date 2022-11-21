package com.poc.audioconversion.service;

import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AudioConversionService {

    public AudioConversionService() {}
    private static final AudioFormat wavResultFormat = new AudioFormat(
            44100, 16, 1, true, false
            );

    public byte[] convertToWav(String filePath) {
        File sourceFile = new File(filePath);
        File convertedWavFile = new File(filePath + "-WAV.txt");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(sourceFile);
            AudioInputStream formattedAudioStream = AudioSystem.getAudioInputStream(wavResultFormat, audioStream);
            AudioInputStream lengthAddedAudioStream = new AudioInputStream(formattedAudioStream, wavResultFormat, sourceFile.length());
            ByteArrayOutputStream convertedOutputStream = new ByteArrayOutputStream();
            AudioSystem.write(lengthAddedAudioStream, AudioFileFormat.Type.WAVE, convertedOutputStream);
            writeFile(convertedOutputStream.toByteArray(), convertedWavFile);
            return convertedOutputStream.toByteArray();
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void convertWavToRaw(byte[] wavByteArray, String filePath) {
        try {
            File convertedRawFile = new File(filePath + "-RAW.txt");
            byte[] rawByteArray = Arrays.copyOfRange(wavByteArray, 44, wavByteArray.length);
            String base64String = Base64.getEncoder().encodeToString(rawByteArray);
            writeFile(base64String, convertedRawFile);
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    static void writeFile(byte[] byteArray, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.write(byteArray);
        os.close();
    }

    static void writeFile(String string, File file) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        osw.write(string);
        osw.close();
    }
}
