package io.github.snek;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class InputBufferer implements InputProcessor {

    final int bufferMaxSize = 3; // Limits the max amount of inputs being queued in the buffer.

    private final HashMap<Integer, Direction> directionMap = new HashMap<Integer, Direction>() {{
        put(19, Direction.UP);
        put(20, Direction.DOWN);
        put(21, Direction.LEFT);
        put(22, Direction.RIGHT);
    }};
    private final HashMap<Direction, Direction> blacklisted = new HashMap<Direction, Direction>() {{
        put(Direction.UP, Direction.DOWN);
        put(Direction.DOWN, Direction.UP);
        put(Direction.LEFT, Direction.RIGHT);
        put(Direction.RIGHT, Direction.LEFT);
    }};

    public Array<Direction> inputBuffer = new Array<>();
    private Direction currentDirectionInput = Direction.RIGHT;

    // Only process the key down event, returns true to absorb the input.
    @Override
    public boolean keyDown(int keycode) {
        Direction inputDirection = directionMap.get(keycode);
        if (inputBuffer.size >= bufferMaxSize) {
            return true;
        }
        if (
            inputDirection != null &&
            inputDirection != currentDirectionInput &&
            blacklisted.get(currentDirectionInput) != inputDirection
        ) {
            currentDirectionInput = inputDirection;
            inputBuffer.add(inputDirection);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
