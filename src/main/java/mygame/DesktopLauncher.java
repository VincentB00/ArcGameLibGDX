package mygame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import helper.EnumConstants;

public class DesktopLauncher 
{
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Arc hero");
        config.setWindowedMode(EnumConstants.screenW.ratio, EnumConstants.screenH.ratio);

        //full screen mode
        // config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        
        new Lwjgl3Application(new Boot(), config);

    }
}
