package dot.empire.myris;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Pseudo-random number distributor.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class SequenceGenerator {

    private final int[][] sequences;
    private int index;

    public SequenceGenerator(int max) {
        this.sequences = new int[max][max];
        for (int[] sequence : sequences) {
            this.index = sequences[0].length + 1;
            next();
        }
        this.index = -1;
    }

    public final int next() {
        if (index++ < sequences[0].length - 1) {
            return sequences[0][index];
        } else {
            System.arraycopy(sequences, 1, this.sequences, 0, sequences.length - 1);
            do {
                this.sequences[sequences.length - 1] = newSequence();
            } while (tuna());

            this.index = -1;
            return next();
        }
    }

    private boolean contains(int[] sequence) {
        for (int[] existing : sequences) {
            if (deepEquals(sequence, existing)) {
                return true;
            }
        }
        return false;
    }

    private boolean deepEquals(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private int[] newSequence() {
        int[] arr = new int[sequences[0].length];
        do {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = ham();
            }
        } while (this.contains(arr));
        return arr;
    }

    // TODO: 08 Nov 2018 Give propper name
    private int ham() {
        int[] frequency = new int[sequences[0].length];
        for (int[] sequence : sequences) {
            for (int i : sequence) {
                if (i != 0) {
                    frequency[i - 1]++;
                }
            }
        }

        // TODO: 08 Nov 2018 Set inital length of array list
        int total = sequences.length * sequences[0].length;
        final List<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < frequency.length; i++) {
            for (int j = 0; j < total - frequency[i]; j++) {
                nums.add(i + 1);
            }
        }

        return nums.get(MathUtils.random.nextInt(nums.size()));
    }

    // TODO: 08 Nov 2018 Give propper name
    private boolean tuna() {
        int[] frequency = new int[sequences[0].length];
        for (int[] sequence : sequences) {
            for (int i : sequence) {
                if (i != 0) {
                    frequency[i - 1]++;
                }
            }
        }

        float mean = 0;
        for (int a : frequency) {
            mean += a;
        }
        mean /= frequency.length;

        float std = 0;
        for (int a : frequency) {
            std += (mean - a) * (mean - a);
        }
        std = (float) Math.sqrt(std / frequency.length);

        for (int i : frequency) {
            if (mean - std < i || mean + std > i) {
                return true;
            }
        }
        return false;
    }
}
