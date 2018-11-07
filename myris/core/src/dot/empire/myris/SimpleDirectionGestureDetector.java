package dot.empire.myris;

import com.badlogic.gdx.input.GestureDetector;

// https://stackoverflow.com/questions/15185799/libgdx-get-swipe-up-or-swipe-right-etc
public class SimpleDirectionGestureDetector extends GestureDetector {

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    public interface DirectionListener {

        void onLeft();

        void onRight();

        void onUp();

        void onDown();
    }

    private static class DirectionGestureListener extends GestureAdapter {

        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener listener) {
            this.directionListener = listener;
        }

        @Override
        public boolean fling(float xvel, float yvel, int btn) {
            if (Math.abs(xvel) > Math.abs(yvel)) {
                if (xvel > 0) {
                    directionListener.onRight();
                } else {
                    directionListener.onLeft();
                }
            } else {
                if (yvel > 0) {
                    directionListener.onDown();
                } else {
                    directionListener.onUp();
                }
            }
            return super.fling(xvel, yvel, btn);
        }
    }
}