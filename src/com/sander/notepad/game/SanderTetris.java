package com.sander.notepad.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;

public class SanderTetris extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying = true;
    private SurfaceHolder holder;
    private Paint paint;
    
    private final int COLS = 10;
    private final int ROWS = 20;
    private int blockSize;
    private int[][] grid = new int[ROWS][COLS];
    
    private int currentX, currentY;
    private int[][] currentPiece;
    private Random random = new Random();
    
    private long lastDropTime = 0;
    private final long DROP_INTERVAL = 500; // ms

    public SanderTetris(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();
        resetPiece();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            try { Thread.sleep(16); } catch (InterruptedException e) {} // ~60 FPS
        }
    }

    private void update() {
        long now = System.currentTimeMillis();
        if (now - lastDropTime > DROP_INTERVAL) {
            if (canMove(currentX, currentY + 1)) {
                currentY++;
            } else {
                lockPiece();
            }
            lastDropTime = now;
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            blockSize = getWidth() / COLS;

            // Gambar grid
            paint.setColor(Color.DKGRAY);
            for (int i = 0; i <= COLS; i++) {
                canvas.drawLine(i * blockSize, 0, i * blockSize, ROWS * blockSize, paint);
            }
            for (int i = 0; i <= ROWS; i++) {
                canvas.drawLine(0, i * blockSize, COLS * blockSize, i * blockSize, paint);
            }

            // Gambar blok yang udah mati
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (grid[r][c] != 0) {
                        paint.setColor(grid[r][c]);
                        canvas.drawRect(c * blockSize, r * blockSize, 
                            (c + 1) * blockSize, (r + 1) * blockSize, paint);
                    }
                }
            }

            // Gambar blok aktif
            paint.setColor(Color.CYAN);
            for (int r = 0; r < currentPiece.length; r++) {
                for (int c = 0; c < currentPiece[0].length; c++) {
                    if (currentPiece[r][c] != 0) {
                        int px = (currentX + c) * blockSize;
                        int py = (currentY + r) * blockSize;
                        canvas.drawRect(px, py, px + blockSize, py + blockSize, paint);
                    }
                }
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void resetPiece() {
        int type = random.nextInt(4);
        if (type == 0) currentPiece = new int[][]{{1,1,1,1}}; // I
        else if (type == 1) currentPiece = new int[][]{{1,1},{1,1}}; // O
        else if (type == 2) currentPiece = new int[][]{{1,1,1},{0,1,0}}; // T
        else currentPiece = new int[][]{{1,1,0},{0,1,1}}; // Z

        currentX = COLS / 2 - currentPiece[0].length / 2;
        currentY = 0;
    }

    private boolean canMove(int x, int y) {
        for (int r = 0; r < currentPiece.length; r++) {
            for (int c = 0; c < currentPiece[0].length; c++) {
                if (currentPiece[r][c] != 0) {
                    int nx = x + c, ny = y + r;
                    if (nx < 0 || nx >= COLS || ny >= ROWS) return false;
                    if (ny >= 0 && grid[ny][nx] != 0) return false;
                }
            }
        }
        return true;
    }

    private void lockPiece() {
        for (int r = 0; r < currentPiece.length; r++) {
            for (int c = 0; c < currentPiece[0].length; c++) {
                if (currentPiece[r][c] != 0) {
                    grid[currentY + r][currentX + c] = Color.CYAN;
                }
            }
        }
        resetPiece();
    }

    // Kontrol Gerak (dipanggil dari UI)
    public void moveLeft() { if (canMove(currentX - 1, currentY)) currentX--; }
    public void moveRight() { if (canMove(currentX + 1, currentY)) currentX++; }
    public void rotate() {
        int[][] rotated = new int[currentPiece[0].length][currentPiece.length];
        for (int r = 0; r < currentPiece.length; r++) {
            for (int c = 0; c < currentPiece[0].length; c++) {
                rotated[c][currentPiece.length - 1 - r] = currentPiece[r][c];
            }
        }
        if (canMove(currentX, currentY)) currentPiece = rotated;
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isPlaying = false;
        try { thread.join(); } catch (Exception e) {}
    }
}
