package com.poc.audioconversion;

import com.poc.audioconversion.service.AudioConversionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class AudioconversionApplication {

	private static final AudioConversionService audioConversionService = new AudioConversionService();

	public static void main(String[] args) {
		SpringApplication.run(AudioconversionApplication.class, args);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter file path: ");
		String filePath = scanner.nextLine();
		System.out.println("Given file path: " + filePath);
		byte[] wavByteArray = audioConversionService.convertToWav(filePath);
		audioConversionService.convertWavToRaw(wavByteArray, filePath);
	}

}
