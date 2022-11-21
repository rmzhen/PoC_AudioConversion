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
        File convertedWavBAFile = new File(filePath + "-WAV-BA.txt");
        File convertedWavB64File = new File(filePath + "-WAV-B64.txt");
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(sourceFile);
            AudioInputStream formattedAudioStream = AudioSystem.getAudioInputStream(wavResultFormat, audioStream);
            AudioInputStream lengthAddedAudioStream = new AudioInputStream(formattedAudioStream, wavResultFormat, sourceFile.length());
            ByteArrayOutputStream convertedOutputStream = new ByteArrayOutputStream();
            AudioSystem.write(lengthAddedAudioStream, AudioFileFormat.Type.WAVE, convertedOutputStream);
            writeFile(convertedOutputStream.toByteArray(), convertedWavBAFile, convertedWavB64File);
            return convertedOutputStream.toByteArray();
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void convertWavToRaw(byte[] wavByteArray, String filePath) {
        try {
            File convertedRawBAFile = new File(filePath + "-RAW-BA.txt");
            File convertedRawB64File = new File(filePath + "-RAW-B64.txt");
            byte[] rawByteArray = Arrays.copyOfRange(wavByteArray, 44, wavByteArray.length);
            writeFile(rawByteArray, convertedRawBAFile, convertedRawB64File);
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    static void writeFile(byte[] byteArray, File fileBA, File fileB64) throws IOException {
        OutputStream os = new FileOutputStream(fileBA);
        os.write(byteArray);
        os.close();
        String base64String = Base64.getEncoder().encodeToString(byteArray);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileB64), StandardCharsets.UTF_8);
        osw.write(base64String);
        osw.close();
    }
}
