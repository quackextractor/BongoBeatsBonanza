package service;

import view.PNGImage;

import java.util.ArrayList;
import java.util.List;

public class PNGImagePool {
    private List<PNGImage> pool = new ArrayList<>();

    public PNGImage acquire(int lane) {
        for (PNGImage image : pool) {
            if (image.getLane() == lane) {
                pool.remove(image);
                return image;
            }
        }
        return new PNGImage(lane);
    }

    public void release(PNGImage image) {
        pool.add(image);
    }
}
