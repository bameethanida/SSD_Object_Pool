import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Game extends Observable {

    private int width = 600;
    private int height = 600;

    private List<Bullet> bulletPool;
    private List<Bullet> bullets;
    private Thread mainLoop;
    private boolean alive;

    public Game() {
        alive = true;

        bulletPool = new ArrayList<Bullet>();
        for (int i = 1; i <= 60; i++) {
            bulletPool.add(new Bullet(0,0,0,0));
        }

        bullets = new ArrayList<Bullet>();
        mainLoop = new Thread() {
            @Override
            public void run() {
                while(alive) {
                    tick();
                    setChanged();
                    notifyObservers();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mainLoop.start();
    }

    public void tick() {
        moveBullets();
        cleanupBullets();
    }

    private void moveBullets() {
        for(Bullet bullet : bullets) {
            bullet.move();
        }
    }

    private void cleanupBullets() {
        List<Bullet> toRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets) {
            if(bullet.getX() <= 0 ||
                    bullet.getX() >= width ||
                    bullet.getY() <= 0 ||
                    bullet.getY() >= height) {
                toRemove.add(bullet);
            }
        }
        for(Bullet bullet : toRemove) {
            bulletPool.add(bullet);
            bullets.remove(bullet);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }


    public void burstBullets(int x, int y) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx != 0 || dy != 0) {
                    Bullet bullet = bulletPool.get(0);
                    bullet.setCoord(x, y, dx, dy);
                    bullets.add(bullet);
                    bulletPool.remove(0);;
                }
            }
        }
    }

}
