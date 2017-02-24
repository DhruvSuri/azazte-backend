package com.azazte.TvAnalytics;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.*;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;

public class VideoToImageConvertor {
    public static VideoToImageConvertor instance = new VideoToImageConvertor();
    public static double SECONDS_BETWEEN_FRAMES;
    public static int IMAGE_COUNTER;

    private static String INPUT_PATH;
    private static String OUTPUT_PATH;

    public static Queue QUEUE;

    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;

    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;

    public static long MICRO_SECONDS_BETWEEN_FRAMES;

    public static VideoToImageConvertor getInstance() {
        return instance;
    }

    public int convert(String inputPath, String outputPath, double secondsBetweenFrames, Queue optionalImageQueue) {
        OUTPUT_PATH = outputPath;
        INPUT_PATH = inputPath;
        SECONDS_BETWEEN_FRAMES = secondsBetweenFrames;
        MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
        QUEUE = optionalImageQueue;
        IMAGE_COUNTER = 1;
        IMediaReader mediaReader = ToolFactory.makeReader(inputPath);

        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        mediaReader.addListener(new ImageSnapListener());

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;
        return IMAGE_COUNTER;
    }

    private static class ImageSnapListener extends MediaListenerAdapter {

        public void onVideoPicture(IVideoPictureEvent event) {

            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                    // no need to show frames from this video stream
                else
                    return;
            }

            // if uninitialized, back date mLastPtsWrite to get the very first frame
            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

            // if it's time to write the next frame
            if (event.getTimeStamp() - mLastPtsWrite >=
                    MICRO_SECONDS_BETWEEN_FRAMES) {

                String outputFilename = dumpImageToFile(event.getImage());

                // indicate file written
                double seconds = ((double) event.getTimeStamp()) /
                        Global.DEFAULT_PTS_PER_SECOND;
                System.out.printf(
                        "at elapsed time of %6.3f seconds wrote: %s\n",
                        seconds, outputFilename);

                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }

        }

        private String dumpImageToFile(BufferedImage image) {
            try {
                String outputFilename = OUTPUT_PATH +
                        IMAGE_COUNTER + ".png";
                ImageIO.write(image, "png", new File(outputFilename));
                IMAGE_COUNTER++;
                if (QUEUE != null) {
                    QUEUE.add(outputFilename);
                }

                return outputFilename;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    public static void main(String args[]) {
        VideoToImageConvertor.getInstance().convert("/Users/dhruv.suri/Documents/project/azazte-backend/Videos/Ad Videos/Wild Stone Perfume New Ad.mp4", "/Users/dhruv.suri/Documents/project/azazte-backend/Videos/Ads/Wild_Stone/", 0.15, null);
    }

}
