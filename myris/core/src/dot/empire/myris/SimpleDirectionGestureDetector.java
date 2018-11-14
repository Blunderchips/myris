package dot.empire.myris;

import com.badlogic.gdx.input.GestureDetector;

// https://stackoverflow.com/questions/15185799/libgdx-get-swipe-up-or-swipe-right-etc
public class SimpleDirectionGestureDetector extends GestureDetector {

    public SimpleDirectionGestureDetector(DirectionListener listener) {
        super(new DirectionGestureListener(listener));
    }

    public interface DirectionListener {

        /**
         * Invoked when a lift swipe is detected.
         */
        void onLeft();

        /**
         * Invoked when a right swipe is detected.
         */
        void onRight();

        /**
         * Invoked when a up swipe is detected.
         */
        void onUp();

        /**
         * Invoked when a down swipe is detected.
         */
        void onDown();
    }

    /**
     * Detects screen swipes.
     */
    private static class DirectionGestureListener extends GestureAdapter {

        final DirectionListener listener;

        public DirectionGestureListener(DirectionListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean fling(float xvel, float yvel, int btn) {
            if (Math.abs(xvel) > Math.abs(yvel)) {
                if (xvel > 0) {
                    listener.onRight();
                } else {
                    listener.onLeft();
                }
            } else {
                if (yvel > 0) {
                    listener.onDown();
                } else {
                    listener.onUp();
                }
            }
            return true;
        }
    }
}