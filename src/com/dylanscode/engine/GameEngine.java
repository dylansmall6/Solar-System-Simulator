package com.dylanscode.engine;


import com.dylanscode.engine.engine.game.MouseHandler;

public class GameEngine implements Runnable {

    private static final int TARGET_FPS = 75;

    private static final int TARGET_UPS = 30;

    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private final MouseHandler mouseHandler;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        mouseHandler = new MouseHandler();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        window.init();
        timer.init();
        mouseHandler.init(window);
        gameLogic.init(window);
    }

    private void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if ( !window.isvSync() ) {
                sync();
            }
        }
    }

    private void cleanup() {
        gameLogic.clean();                
    }
    
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void input() {
        mouseHandler.input(window);
        gameLogic.input(window,mouseHandler);
    }

    private void update(float interval) {
        gameLogic.update(interval,mouseHandler);
    }

    private void render() {
        gameLogic.render(window);
        window.update();
    }
}

