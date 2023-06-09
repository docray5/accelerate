package utils;

//import game.Game;

public class FpsCounter {
    private long lastSecond;
    private long frameCount;

    public FpsCounter() {
        this.lastSecond = 0;
        this.frameCount = 0;
    }

    public void countAndPrint(long now) {
        if (now - lastSecond > 1_000_000_000L) {
            double fps = ((double) frameCount / (now - lastSecond)) * 1_000_000_000L; // type conversion is slow
            if (this.lastSecond == 0) fps = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDisplayMode().getRefreshRate();
            System.out.println(fps);

            // -------------Calc FPS ratio--------------
            // works kinda like delta time, but we just calculate the ratio instead
            //Game.FPS_RATIO = 143.0 / fps;

            lastSecond = now;
            frameCount = 0;
            //System.out.println("Total memory: " + Runtime.getRuntime().totalMemory()/1024 + ", Free memory: " + Runtime.getRuntime().freeMemory()/1024 + ", Diff: " + (Runtime.getRuntime().totalMemory()/1024 - Runtime.getRuntime().freeMemory()/1024));
        }
        frameCount++;
    }
}
