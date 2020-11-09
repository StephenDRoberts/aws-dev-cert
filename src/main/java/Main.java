import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;

import java.util.Collections;

public class Main {

    public static void main(String[] args) throws JavaLayerException {

        String SAMPLE = "Hello AWS";

        PollyClient pollyClient = PollyClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.builder()
                    .profileName("default")
                    .build()
                )
                .build();

        ResponseInputStream speechStream = pollyClient.synthesizeSpeech(
                SynthesizeSpeechRequest.builder()
                        .languageCode("en-US")
                        .lexiconNames("awsLexicon")
                        .outputFormat("mp3")
                        .text(SAMPLE)
                        .voiceId("Salli")
                        .build()
        );

        AdvancedPlayer player = new AdvancedPlayer(speechStream,
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
                System.out.println(SAMPLE);
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }
        });

        // play it!
        player.play();

    }
}
